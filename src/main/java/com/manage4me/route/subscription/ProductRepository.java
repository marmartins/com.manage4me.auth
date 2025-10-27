package com.manage4me.route.subscription;

import com.manage4me.route.entities.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends CrudRepository<Product, String> {

    Product findBySku(String sku);

    List<Product> findAllByOrderBySkuDesc();

    List<Product> findAllByRenewable(boolean renewable);

}
