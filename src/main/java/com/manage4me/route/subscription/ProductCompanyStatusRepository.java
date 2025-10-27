package com.manage4me.route.subscription;

import com.manage4me.route.entities.CompanyProducts;
import com.manage4me.route.entities.ProductAction;
import com.manage4me.route.entities.ProductCompanyStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductCompanyStatusRepository extends CrudRepository<ProductCompanyStatus, String> {

    List<ProductCompanyStatus> findByCompanyProducts(CompanyProducts companyProducts);

    ProductCompanyStatus findByCompanyProductsAndProductAction(CompanyProducts companyProducts, ProductAction productAction);

    List<ProductCompanyStatus> findAllByProductAction(ProductAction productAction);

    @Modifying
    @Transactional
    @Query(value = "UPDATE app_product_company_status" +
            " SET" +
            " count_used = count_used + 1" +
            " WHERE company_product_id=:company_product_id AND product_action_id=:actionId", nativeQuery = true)
    void incrementValue(String company_product_id, String actionId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE app_product_company_status" +
            " SET" +
            " count_used = count_used - 1" +
            " WHERE company_product_id=:company_product_id AND product_action_id=:actionId", nativeQuery = true)
    void decrementValue(String company_product_id, String actionId);

}
