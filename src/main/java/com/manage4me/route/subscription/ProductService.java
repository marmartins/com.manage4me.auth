package com.manage4me.route.subscription;

import com.manage4me.route.account.AccountRepository;
import com.manage4me.route.entities.*;
import com.manage4me.route.commons.ControllerExceptionHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.manage4me.route.commons.ErrorCodes.*;
import static org.springframework.util.StringUtils.hasLength;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    public static final String DEFAULT_FREE_SUBSCRIPTION_SKU = "free_subscription";

    private final ControllerExceptionHandler exceptionHandler;

    private final ProductRepository productRepository;
    private final ProductActionRepository productActionRepository;
    private final PurchaseService purchaseService;
    private final ProductCompanyStatusService productCompanyStatusService;
    private final AccountRepository accountRepository;

    public Product getDefaultSubscription() {
        log.info("Get default subscription with SKU {}", DEFAULT_FREE_SUBSCRIPTION_SKU);
        return findBySku(DEFAULT_FREE_SUBSCRIPTION_SKU);
    }

    public Map<String, List<ProductAction>> findAllProductAction() {
        Iterable<ProductAction> productActions = productActionRepository.findAll();
        return StreamSupport.stream(productActions.spliterator(), false)
                .filter(productAction -> productAction.getProduct().getStatus() == Status.ACTIVE)
                .collect(Collectors.groupingBy(productAction -> productAction.getProduct().getSku()));
    }

    public List<Product> findAllProducts() {
        return productRepository.findAllByOrderBySkuDesc()
                .stream()
                .filter(product -> product.getStatus() == Status.ACTIVE)
                .collect(Collectors.toList());
    }

    public Product findBySku(String productCode) {
        if (!hasLength(productCode)) {
            exceptionHandler.throwException(APP_0002::name, "productCode");
        }
        return productRepository.findBySku(productCode);
    }

    public Product findById(String id) {
        if (!hasLength(id)) {
            exceptionHandler.throwException(APP_0002::name, "Id");
        }
        return productRepository.findById(id).orElseThrow(
                () -> exceptionHandler.throwException(APP_0011::name, "Company")
        );
    }

    public Product findByProductStatistic() {
        Company company = getLoggedUser().getCompany();
        Purchase purchase = purchaseService.findByCompany(company.getId(), false);
        CompanyProducts companyProducts = productCompanyStatusService.getCompanyProducts(company, purchase);
        for (int i = 0; i < companyProducts.getProduct().getProductActionList().size(); i++) {
            ProductAction productAction = companyProducts.getProduct().getProductActionList().get(i);
            ProductCompanyStatus productActionStatus = productCompanyStatusService.findByCompanyProductsAndProductAction(companyProducts, productAction);
            productAction.setCountUsed(productActionStatus.getCountUsed());
        }
        return companyProducts.getProduct();
    }

    public User getLoggedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            exceptionHandler.throwException(APP_0005::name);
        }
        User user = accountRepository.findById(authentication.getName()).orElseThrow(
                () -> exceptionHandler.throwException(APP_0011::name, "User ID"));

        return user;
    }

}
