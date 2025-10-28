package com.manage4me.route.account;

import com.manage4me.route.commons.ControllerExceptionHandler;
import com.manage4me.route.entities.*;
import com.manage4me.route.subscription.ProductCompanyStatusService;
import com.manage4me.route.subscription.ProductService;
import com.manage4me.route.subscription.PurchaseRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


import static com.manage4me.route.commons.ErrorCodes.*;
import static java.util.Objects.isNull;
import static org.springframework.util.StringUtils.hasLength;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    @Value("${app.defaultPassword:cl@123}")
    private String defaultPassword;

    private final ControllerExceptionHandler exceptionHandler;

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final CompanyService companyService;
    private final ProductCompanyStatusService productCompanyStatusService;
    private final PurchaseRepository purchaseRepository;
    private final ProductService productService;

    public User register(User user) {
        log.info("Registering new account...");
        validateCreate(user);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        var company = companyService.save(user.getCompany());
        user.setCompany(company);
        var purchase = createPurchase(user.getCompany().getId());
        user.getCompany().setPurchase(purchase);

        var defaultSubscription = productService.getDefaultSubscription();
        companyService.saveCompanyProduct(company, purchase, defaultSubscription);

        user.setRole(Roles.ADMIN);
        user.setStatus(Status.ACTIVE);
        log.info("Registered new account successfully - {}", user);
        return accountRepository.save(user);
    }

    private Purchase createPurchase(String companyId) {
        Purchase purchase = new Purchase();
        Product product = productService.getDefaultSubscription();
        log.info("Product to be added :: {}", product);
        purchase.setProduct(product);
        purchase.setStatus(Status.ACTIVE);
        purchase.setPurchaseDate(DateTimeFormatter.ISO_INSTANT.format(Instant.now()));
        purchase.setCompanyId(companyId);
        Purchase persisted = purchaseRepository.save(purchase);
        log.info("Created purchase : {}", persisted);
        return persisted;
    }

    public User findById(String id) {
        if (!hasLength(id)) {
            exceptionHandler.throwException(APP_0002::name, "Id");
        }

        User user = accountRepository.findById(id).orElseThrow(
                () -> exceptionHandler.throwException(APP_0011::name, "ID"));

        user.getCompany().setPurchase(purchaseRepository.findByCompany(user.getCompany().getId()));

        return user;
    }

    private void validateCreate(User entity) {
        validate(entity);
        if (!isNull(accountRepository.findByEmail(entity.getEmail()))) {
            exceptionHandler.throwException(APP_0004::name, "Email");
        }
        if (!hasLength(entity.getPassword())) {
            exceptionHandler.throwException(APP_0002::name, "Password");
        }
    }

    private void validate(User entity) {
        if (!hasLength(entity.getName())) {
            exceptionHandler.throwException(APP_0002::name, "Name");
        }
        if (!hasLength(entity.getEmail())) {
            exceptionHandler.throwException(APP_0002::name, "Email");
        }
        if (!hasLength(entity.getCompany().getName())) {
            exceptionHandler.throwException(APP_0002::name, "Company name");
        }
    }

    public User getLoggedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            exceptionHandler.throwException(APP_0005::name);
        }
        User user = findById(authentication.getName());

        Purchase purchase = purchaseRepository.findByCompany(user.getCompany().getId());
        user.getCompany().setPurchase(purchase);

        return user;
    }
}
