package com.manage4me.route.subscription;

import com.manage4me.route.commons.ControllerExceptionHandler;
import com.manage4me.route.entities.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.manage4me.route.commons.ErrorCodes.APP_0002;
import static org.springframework.util.StringUtils.hasLength;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    public static final String DEFAULT_FREE_SUBSCRIPTION_SKU = "free_subscription";

    private final ControllerExceptionHandler exceptionHandler;

    private final ProductRepository productRepository;

    public Product getDefaultSubscription() {
        log.info("Get default subscription with SKU {}", DEFAULT_FREE_SUBSCRIPTION_SKU);
        return findBySku(DEFAULT_FREE_SUBSCRIPTION_SKU);
    }

    public Product findBySku(String productCode) {
        if (!hasLength(productCode)) {
            exceptionHandler.throwException(APP_0002::name, "productCode");
        }
        return productRepository.findBySku(productCode);
    }
}
