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

}
