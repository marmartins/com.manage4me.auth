package com.manage4me.route.subscription;

import com.manage4me.route.entities.Company;
import com.manage4me.route.entities.CompanyProducts;
import com.manage4me.route.entities.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyProductRepository extends CrudRepository<CompanyProducts, String> {

    List<CompanyProducts> findAllByCompany(Company company);

    List<CompanyProducts> findAllByProduct(Product product);

}
