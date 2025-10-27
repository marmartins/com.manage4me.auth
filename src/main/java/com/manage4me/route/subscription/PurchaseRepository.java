package com.manage4me.route.subscription;

import com.manage4me.route.entities.Product;
import com.manage4me.route.entities.Purchase;
import com.manage4me.route.entities.Status;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseRepository extends CrudRepository<Purchase, String> {

    @Query(value = """
        SELECT * FROM app_purchase
        WHERE status='ACTIVE'
        AND company_id=:companyId
        ORDER BY created_date DESC
        LIMIT 1
    """, nativeQuery = true)
    Purchase findByCompany(String companyId);

    @Query(value = """
        SELECT purchase.* FROM app_purchase purchase
        LEFT JOIN app_product product on product.id=purchase.product_id
        WHERE purchase.status='ACTIVE'
        AND product.sku!='free_subscription'
        AND product.type='SUBSCRIPTION';
    """, nativeQuery = true)
    List<Purchase> findAllByStatusAndProductType();

    List<Purchase> findAllByCompanyIdAndStatusOrderByCreatedDateDesc(String companyId, Status status);

    List<Purchase> findAllByCompanyIdAndProductAndStatusOrderByCreatedDateDesc(String companyId, Product product, Status status);
}
