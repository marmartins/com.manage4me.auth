package com.manage4me.route.subscription;

import com.manage4me.route.account.CompanyRepository;
import com.manage4me.route.commons.ControllerExceptionHandler;
import com.manage4me.route.entities.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.manage4me.route.commons.ErrorCodes.*;
import static com.manage4me.route.subscription.ProductService.DEFAULT_FREE_SUBSCRIPTION_SKU;
import static org.springframework.util.StringUtils.hasLength;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductCompanyStatusService {

    private final ControllerExceptionHandler exceptionHandler;

    private final PurchaseService purchaseService;
    private final ProductCompanyStatusRepository productCompanyStatusRepository;
    private final ProductActionRepository productActionRepository;
    private final CompanyRepository companyRepository;
    private final CompanyProductRepository companyProductRepository;
    private final ProductRepository productRepository;

    public ProductCompanyStatus save(ProductCompanyStatus entity) {
        validate(entity);
        log.info("Save productCompanyStatusRepository : {}", entity);
        return productCompanyStatusRepository.save(entity);
    }

    private void validate(ProductCompanyStatus entity) {
        if (entity.getCompanyProducts().getCompany() == null) {
            exceptionHandler.throwException(APP_0002::name, "Company");
        }
        if (entity.getProductAction() == null) {
            exceptionHandler.throwException(APP_0002::name, "ProductAction");
        }
    }

    public boolean incrementValue(String companyId, String action) {
        log.info("Increment Value to Company : {} - Action : {}", companyId, action);
        Company company = getCompany(companyId);
        Purchase purchase = purchaseService.findByCompany(companyId, false);
        CompanyProducts companyProducts = getCompanyProducts(company, purchase);
        if (companyProducts == null) {
            exceptionHandler.throwException(APP_0300::name);
            return false;
        }
        ProductAction productAction = getProductAction(companyProducts.getProduct(), action);
        if (productAction == null) {
            log.warn("ProductAction is null, trying to search on FREE SUBSCRIPTION");
            productAction = getProductAction(productRepository.findBySku(DEFAULT_FREE_SUBSCRIPTION_SKU), action);
            log.info("Increment to Product  {}", productAction.getProduct().getSku());
            if (productAction == null) {
                log.warn("ProductAction is null to Product");
                return false;
            } else {
                companyProducts = getCompanyProducts(company, purchaseService.findByCompany(companyId, true));
                log.warn("ProductAction will be incremented to Product [{}] - Action [{}]", productAction.getProduct().getSku(), action);
            }
        }
        log.info("Increment to ProductAction Product [{}] - Action [{}]", companyProducts.getProduct().getSku(), action);
        productCompanyStatusRepository.incrementValue(companyProducts.getId(), productAction.getId());
        log.info("Increment Value Success complete");
        return true;
    }

    public boolean decrementValue(String companyId, String action) {
        log.info("Decrementing Value to Company : {} - Action : {}", companyId, action);
        Company company = getCompany(companyId);
        Purchase purchase = purchaseService.findByCompany(companyId, false);
        CompanyProducts companyProducts = getCompanyProducts(company, purchase);
        if (companyProducts == null) {
            exceptionHandler.throwException(APP_0300::name);
            return false;
        }
        ProductAction productAction = getProductAction(companyProducts.getProduct(), action);

        if (productAction.getAllowedValue() == 0) {
            log.info("The ACTION [{}] is unlimited to company [{} - {}]", productAction, company.getId(), company.getName());
            return true;
        }

        productCompanyStatusRepository.decrementValue(companyProducts.getId(), productAction.getId());
        log.info("Decrement Value Success complete");
        return true;
    }

    public int getAllowedValues(String companyId, String action) {
        log.info("Get Allowed Values to Company : {} - Action : {}", companyId, action);
        Purchase purchase = purchaseService.findByCompany(companyId, false);
        int allowedValue = 0;
        if (!purchase.getProduct().getSku().equals(DEFAULT_FREE_SUBSCRIPTION_SKU)) {
            log.info("For different subscription the system will sum the values for the DEFAULT_FREE_SUBSCRIPTION_SKU");
            Purchase purchaseFree = purchaseService.findByCompany(companyId, true);
            ProductCompanyStatus productCompanyStatusFree = getProductCompanyStatus(companyId, action, purchaseFree);
            allowedValue = productCompanyStatusFree.getProductAction().getAllowedValue();
            log.info("Allowed Values initial value [{}]", allowedValue);
        }
        ProductCompanyStatus productCompanyStatus = getProductCompanyStatus(companyId, action, purchase);
        log.info("Adding Allowed Values for purchase : {}", purchase.getProduct().getSku());
        allowedValue += productCompanyStatus.getProductAction().getAllowedValue();
        log.info("Allowed Values final value [{}]", allowedValue);
        return allowedValue;
    }

    public boolean hasPermission(String companyId, String action) {
        Purchase purchase = purchaseService.findByCompany(companyId, false);
        ProductCompanyStatus productCompanyStatus = getProductCompanyStatus(companyId, action, purchase);
        boolean hasPermission = hasPermission(productCompanyStatus);
        if (!hasPermission) {
            log.warn("The company [{}] has no permission to action [{}]", companyId, action);
            log.info("Check Permission with DEFAULT_FREE_SUBSCRIPTION_SKU");

            if (!purchase.getProduct().getSku().equals(DEFAULT_FREE_SUBSCRIPTION_SKU)) {
                log.warn("As a different subscription has no permission, the system will check the status");
                checkPurchaseIsValid(companyId);
                // Check if the company has a purchase with the default subscription
                purchase = purchaseService.findByCompany(companyId, true);
                productCompanyStatus = getProductCompanyStatus(companyId, action, purchase);
                hasPermission = hasPermission(productCompanyStatus);
                if (!hasPermission) {
                    log.warn("The company [{}] has no permission to action [{}] in DEFAULT_SUBSCRIPTION", companyId, action);
                    exceptionHandler.throwException(APP_0300::name);
                }
            }
        }
        return hasPermission;
    }

    public boolean hasPermission(ProductCompanyStatus productCompanyStatus) {
        if (productCompanyStatus == null) {
            log.warn("ProductCompanyStatus is null");
            return false;
        }
        log.info("Check Permission to Company : {} - Action : {} - purchase : {}",
                productCompanyStatus.getCompanyProducts().getCompany().getName()
                , productCompanyStatus.getProductAction().getAction(),
                productCompanyStatus.getCompanyProducts().getPurchase().getId());


        log.info("ProductCompanyStatus = {} , isValid:{}", productCompanyStatus.getCompanyProducts().getProduct(), productCompanyStatus.isValid());
        return productCompanyStatus.isValid();
    }

    private ProductCompanyStatus getProductCompanyStatus(String companyId, String action, Purchase purchase) {
        Company company = getCompany(companyId);
        CompanyProducts companyProducts = getCompanyProducts(company, purchase);
        if (companyProducts == null) {
            exceptionHandler.throwException(APP_0300::name);
            return null;
        }
        ProductAction productAction = getProductAction(companyProducts.getProduct(), action);
        if (productAction == null) {
            log.warn("ProductAction is null the value FALSE will be returned");
            return null;
        }

        return productCompanyStatusRepository.findByCompanyProductsAndProductAction(companyProducts, productAction);
    }

    public void checkPurchaseIsValid(String companyId) {
        Company company = getCompany(companyId);
        List<CompanyProducts> allProducts = companyProductRepository.findAllByCompany(company);
        if (allProducts != null && !allProducts.isEmpty()) {
            for (CompanyProducts companyProducts : allProducts) {
                boolean purchaseIsValid = false;
                for (int i = 0; i < companyProducts.getProduct().getProductActionList().size(); i++) {
                    ProductAction productAction = companyProducts.getProduct().getProductActionList().get(i);
                    if(productAction.getAllowedValue() == 0) {
                        log.info("Unlimited to ProductAction [{} - {}]", productAction.getProduct().getSku(), productAction.getAction().name());
                        continue;
                    }
                    ProductCompanyStatus productCompanyStatus = getProductCompanyStatus(companyId, productAction.getAction().name(), companyProducts.getPurchase());
                    purchaseIsValid = hasPermission(productCompanyStatus);
                }
                if (!purchaseIsValid) {
                    log.error("Purchase is not valid to company [{} - {}]", company.getId(), company.getName());
                    purchaseService.updateStatus( companyProducts.getPurchase(), companyProducts.getProduct().getType() == ProductType.CONSUMABLE ? Status.CONSUMED : Status.ON_HOLD);
                } else {
                    log.info("Purchase is valid to company [{} - {}]", company.getId(), company.getName());
                }
            }
        }
    }


    private ProductAction getProductAction(Product product, String action) {
        if (!hasLength(action)) {
            exceptionHandler.throwException(APP_0002::name, "action");
        }
        ProductAction productAction = productActionRepository.findAllByActionAndProduct(AppAction.valueOf(action), product);
        if (productAction == null) {
            log.warn("ProductAction is null to Product [{}] - Action [{}]", product.getSku(), action);
            return null;
        }
        return productAction;
    }


    public CompanyProducts getCompanyProducts(Company company, Purchase purchases) {

        List<CompanyProducts> allProducts = companyProductRepository.findAllByCompany(company);
        // Verify that there is an ACTIVE purchase for the company
        // and if it's in the company product list
        if (purchases != null) {

            List<String> skus = allProducts.stream().map(companyProducts -> companyProducts.getProduct().getSku()).toList();
            if (!skus.contains(purchases.getProduct().getSku())) {
                log.error("Purchase SKU is not listed in the company products :: Company: {} - Purchase :: {} - SKU :: {}", company.getId(), purchases.getId(), purchases.getProduct().getSku());
                exceptionHandler.throwException(APP_0301::name, ("company" + company.getName()));
            }

            log.info("The company [{} - {}] have [{}] ACTIVE purchase", company.getId(), company.getName(), purchases);
            if (allProducts.size() == 1) {
                log.info("The company [{} - {}] have only one product - {}", company.getId(), company.getName(), allProducts.getFirst());
                return allProducts.getFirst();
            } else if (allProducts.size() > 1){
                return allProducts.stream()
                        .filter(p -> p.getProduct().getSku().equals(purchases.getProduct().getSku()))
                        .findFirst().get();
            }
        }
        log.debug("The company [{} - {}] have no subscription", company.getId(), company.getName());
        return null;
    }

    public ProductCompanyStatus findByCompanyProductsAndProductAction(CompanyProducts companyProducts, ProductAction productAction) {
        return productCompanyStatusRepository.findByCompanyProductsAndProductAction(companyProducts, productAction);
    }

    private Company getCompany(String companyId) {
        if (!hasLength(companyId)) {
            exceptionHandler.throwException(APP_0002::name, "companyId");
        }

        Company company = companyRepository.findById(companyId).get();
        if (company == null) {
            exceptionHandler.throwException(APP_0003::name, "Company", companyId);
        }
        return company;
    }

    public boolean refresh() {
        log.info("Starting Refresh");
        // List all renewable products
        List<Product> allProduct = productRepository.findAllByRenewable(true);
        allProduct.forEach(product -> {
            // List all renewable Product Actions
            List<ProductAction> allProductActions = productActionRepository.findAllByProductAndRenewable(product, true);
            allProductActions.forEach(productAction -> {
                // Not renew the values to Unlimited productAction
                if (productAction.getAllowedValue() == 0) {
                    log.info("Unlimited to ProductAction [{} - {}]", productAction.getProduct().getSku(), productAction.getAction().name());
                    return;
                }
                // List all Company Status for the actions
                List<ProductCompanyStatus> allByCompany = productCompanyStatusRepository.findAllByProductAction(productAction);
                allByCompany.forEach(o -> {
                    log.info("Refreshing: {}", o);
                    o.setCountUsed(0);
                    productCompanyStatusRepository.save(o);
                });
            });
        });
        log.info("Refresh Success complete");
        return true;
    }

}
