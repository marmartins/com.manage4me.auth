package com.manage4me.route.subscription;

import com.manage4me.route.entities.AppAction;
import com.manage4me.route.entities.Product;
import com.manage4me.route.entities.ProductAction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductActionRepository extends CrudRepository<ProductAction, String> {

    ProductAction findAllByActionAndProduct(AppAction action, Product product);

    List<ProductAction> findAllByProduct(Product product);

    List<ProductAction> findAllByProductAndRenewable(Product product, boolean renewable);

}
