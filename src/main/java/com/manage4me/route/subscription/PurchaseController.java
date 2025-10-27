package com.manage4me.route.subscription;

import com.manage4me.route.entities.Product;
import com.manage4me.route.entities.ProductAction;
import com.manage4me.route.entities.Purchase;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/rc/v1/acc")
public class PurchaseController {

    private final ProductService productService;
    private final PurchaseService purchaseService;

    @PostMapping("/purchase")
    public ResponseEntity<Purchase> findByName(@RequestBody Purchase purchase) {
        return ResponseEntity.ok(purchaseService.save(purchase));
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<Product> findById(@PathVariable String id){
        return ResponseEntity.ok(productService.findById(id));
    }

    @GetMapping("/product/statistic")
    public ResponseEntity<Product> findByProductStatistic(){
        return ResponseEntity.ok(productService.findByProductStatistic());
    }

    @GetMapping("/product-actions")
    public ResponseEntity<Map<String, List<ProductAction>>> findAllProductActions(){
        return ResponseEntity.ok(productService.findAllProductAction());
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> findAllProducts(){
        return ResponseEntity.ok(productService.findAllProducts());
    }

    @GetMapping("/product-by-sku/{sku}")
    public ResponseEntity<Product> findByCode(@PathVariable String sku){
        return ResponseEntity.ok(productService.findBySku(sku));
    }


}
