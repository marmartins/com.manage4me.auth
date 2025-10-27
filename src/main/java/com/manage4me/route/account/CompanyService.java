package com.manage4me.route.account;

import com.manage4me.route.commons.ControllerExceptionHandler;
import com.manage4me.route.entities.*;
import com.manage4me.route.subscription.CompanyProductRepository;
import com.manage4me.route.subscription.ProductActionRepository;
import com.manage4me.route.subscription.ProductCompanyStatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.manage4me.route.commons.ErrorCodes.APP_0002;
import static com.manage4me.route.commons.ErrorCodes.APP_0011;
import static java.util.Objects.isNull;
import static org.springframework.util.StringUtils.hasLength;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyService {

    private final ControllerExceptionHandler exceptionHandler;

    private final CompanyRepository companyRepository;
    private final ProductCompanyStatusRepository productCompanyStatusRepository;
    private final ProductActionRepository productActionRepository;
    private final CompanyProductRepository companyProductRepository;

    public Company save(Company entity) {
        log.info("Saving company");
        validate(entity);
        entity.setStatus(Status.ACTIVE);
        Company company = companyRepository.save(entity);
        log.info("Saved Company successfully - {}", company);
        return company;
    }

    public void saveCompanyProduct(Company company, Purchase purchase, Product product) {
        log.info("Saving Company Product link");
        if (isNull(company)) {
            exceptionHandler.throwException(APP_0002::name, "Company");
        }
        if (isNull(purchase)) {
            exceptionHandler.throwException(APP_0002::name, "Purchase");
        }
        if (isNull(product)) {
            exceptionHandler.throwException(APP_0002::name, "Product");
        }
        CompanyProducts companyProducts = new CompanyProducts();
        companyProducts.setCompany(company);
        companyProducts.setProduct(product);
        companyProducts.setPurchase(purchase);
        companyProductRepository.save(companyProducts);
        log.info("Saving Product Company Action Status link");
        productActionRepository
                .findAllByProduct(product)
                .forEach(action -> {
                    ProductCompanyStatus productCompanyStatus = new ProductCompanyStatus();
                    productCompanyStatus.setCompanyProducts(companyProducts);
                    productCompanyStatus.setCountUsed(0);
                    productCompanyStatus.setProductAction(action);
                    productCompanyStatusRepository.save(productCompanyStatus);
                });
        log.info("Saved Company Product link successfully - {}", companyProducts);
    }

    public Company update(Company entity) {
        validate(entity);
        Company old = findById(entity.getId());

        old.setName(entity.getName());
        old.setDocumentRegistration(entity.getDocumentRegistration());
        old.setAddress(entity.getAddress());
        old.setPhone(entity.getPhone());

        log.info("Company Updated successfully");
        return companyRepository.save(old);
    }

    public Company findById(String id) {
        if (!hasLength(id)) {
            exceptionHandler.throwException(APP_0002::name, "Id");
        }
        return companyRepository.findById(id).orElseThrow(
                () -> exceptionHandler.throwException(APP_0011::name, "Company")
        );
    }

    private void validate(Company entity) {
        if (!hasLength(entity.getDocumentRegistration())) {
            exceptionHandler.throwException(APP_0002::name, "Registration");
        }

        if (!hasLength(entity.getName())) {
            exceptionHandler.throwException(APP_0002::name, "Name");
        }
    }
}
