package com.manage4me.route.subscription;

import com.manage4me.route.entities.ProductCompanyStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCompanyStatusRepository extends CrudRepository<ProductCompanyStatus, String> {

}
