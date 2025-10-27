package com.manage4me.route.subscription;

import com.manage4me.route.account.AccountRepository;
import com.manage4me.route.account.CompanyService;
import com.manage4me.route.commons.ControllerExceptionHandler;
import com.manage4me.route.entities.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.manage4me.route.commons.ErrorCodes.*;
import static com.manage4me.route.entities.Status.ACTIVE;
import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Slf4j
@Service
@RequiredArgsConstructor
public class PurchaseService  {

    private final ControllerExceptionHandler exceptionHandler;

    private final PurchaseRepository purchaseRepository;
    private final ProductRepository productRepository;
    private final AccountRepository accountRepository;
    private final CompanyService companyService;

    public Purchase save(Purchase purchase) {
        log.info("Saving purchase {}", purchase);
        if (isNull(purchase.getProduct())) {
            exceptionHandler.throwException(APP_0002::name, "product");
        }
        if (isBlank(purchase.getProduct().getId()) && isBlank(purchase.getProduct().getSku())) {
            exceptionHandler.throwException(APP_0002::name, "productID");
        }
        if (isNotBlank(purchase.getProduct().getSku())) {
            purchase.setProduct(productRepository.findBySku(purchase.getProduct().getSku()));
        } else {
            purchase.setProduct(productRepository.findBySku(purchase.getProduct().getId()));
        }
        Company company = getLoggedUser().getCompany();
        if (isBlank(purchase.getCompanyId())) {
            purchase.setCompanyId(company.getId());
        }
        purchase.setStatus(Status.ACTIVE);
        Purchase savedPurchase = purchaseRepository.save(purchase);

        companyService.saveCompanyProduct(company, savedPurchase, purchase.getProduct());

        log.info("Saved purchase successfully - {}", purchase);
        return savedPurchase;
    }

    public void updateStatus(Purchase purchase, Status status) {
        log.info("Updating purchase [id = {}] Status to {}", purchase.getId(), status);
        purchase.setStatus(status);
        purchaseRepository.save(purchase);
    }

    public Purchase findByCompany(String companyId, boolean useDefaultSubscription) {
        log.info("Finding purchase by company {}", companyId);
        if (isBlank(companyId)) {
            exceptionHandler.throwException(APP_0002::name, "companyId");
        }
        List<Purchase> allPurchases;
        if (useDefaultSubscription) {
            log.info("Get default subscription with SKU {}", ProductService.DEFAULT_FREE_SUBSCRIPTION_SKU);
            Product defaultSubscription = productRepository.findBySku(ProductService.DEFAULT_FREE_SUBSCRIPTION_SKU);
            allPurchases = purchaseRepository.findAllByCompanyIdAndProductAndStatusOrderByCreatedDateDesc(companyId, defaultSubscription, ACTIVE);
        } else {
            allPurchases = purchaseRepository.findAllByCompanyIdAndStatusOrderByCreatedDateDesc(companyId, ACTIVE);
        }
        return allPurchases.getFirst();
    }

    public User getLoggedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            exceptionHandler.throwException(APP_0005::name);
        }
        User user =  accountRepository.findById(authentication.getName()).orElseThrow(
                () -> exceptionHandler.throwException(APP_0011::name, "Name")
        );
        return user;
    }
}
