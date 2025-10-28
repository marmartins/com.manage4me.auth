package com.manage4me.route.subscription;

import com.manage4me.route.entities.CompanyProducts;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyProductRepository extends CrudRepository<CompanyProducts, String> {

}
