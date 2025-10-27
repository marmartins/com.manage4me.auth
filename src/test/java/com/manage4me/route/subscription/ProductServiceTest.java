package com.manage4me.route.subscription;

import com.manage4me.route.entities.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class ProductServiceTest {

    private static final String DEFAULT_FREE_SUBSCRIPTION = "free_subscription";


    @Autowired
    private ProductRepository productRepository;

//    @Test
    void createInsertScript() {
        Map<AppAction, ProductActionParam> actions = new HashMap<>();
        actions.put(AppAction.CLIENT, new ProductActionParam(false, 3));
        actions.put(AppAction.ORDER_SERVICE, new ProductActionParam(true, 200));
        actions.put(AppAction.REPORT_COMPLETE_PICTURES, new ProductActionParam(true, 10));
        actions.put(AppAction.REPORT_ORIGIN_PICTURES, new ProductActionParam(true, 10));
        actions.put(AppAction.REPORT_VIEW, new ProductActionParam(false, 0));
        actions.put(AppAction.USER, new ProductActionParam(false, 3));
        createInsertSubscriptionActionScript(DEFAULT_FREE_SUBSCRIPTION, true, actions, "Free Pan", "Allow to test and use for free", ProductType.SUBSCRIPTION);
    }

    void createInsertSubscriptionActionScript(String productCode, boolean renewable, Map<AppAction, ProductActionParam> actions, String title, String description, ProductType productType) {
        Product product = new Product();
        product.setStatus(Status.ACTIVE);
        product.setSku(productCode);
        product.setRenewable(renewable);
        product.setTitle(title);
        product.setDescription(description);
        product.setType(productType);

        product.setProductActionList(new ArrayList<>());
        for (Map.Entry<AppAction, ProductActionParam> entry : actions.entrySet()) {
            product.getProductActionList().add(createInsertSubscriptionActionScript(product, entry.getKey(), entry.getValue()));
        }

        productRepository.save(product);
    }

    private record ProductActionParam(boolean renewable, int allowedValue) {};

    private ProductAction createInsertSubscriptionActionScript(Product product, AppAction appAction, ProductActionParam productActionParam) {

        ProductAction productAction = new ProductAction();
        productAction.setAction(appAction);
        productAction.setAllowedValue(productActionParam.allowedValue);
        productAction.setRenewable(productActionParam.renewable);
        productAction.setProduct(product);

        return productAction;
    }

}