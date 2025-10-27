package com.manage4me.route.subscription;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/rc/v1/acc")
public class ProductCompanyStatusController {

    private final ProductCompanyStatusService productCompanyStatusService;

    @GetMapping("/subscription-company-status/increment/{companyId}/{action}")
    public ResponseEntity<Boolean> incrementValue(@PathVariable String companyId,
                                                  @PathVariable String action) {
        return ResponseEntity.ok(productCompanyStatusService.incrementValue(companyId, action));
    }

    @GetMapping("/subscription-company-status/allowed-value/{companyId}/{action}")
    public ResponseEntity<Integer> getAllowedValue(@PathVariable String companyId,
                                                  @PathVariable String action) {
        return ResponseEntity.ok(productCompanyStatusService.getAllowedValues(companyId, action));
    }

    @GetMapping("/subscription-company-status/decrement/{companyId}/{action}")
    public ResponseEntity<Boolean> decrementValue(@PathVariable String companyId,
                                                  @PathVariable String action) {
        return ResponseEntity.ok(productCompanyStatusService.decrementValue(companyId, action));
    }

    @GetMapping("/subscription-company-status/has-permission/{companyId}/{action}")
    public ResponseEntity<Boolean> hasPermission(@PathVariable String companyId,
                                                 @PathVariable String action) {
        return ResponseEntity.ok(productCompanyStatusService.hasPermission(companyId, action));
    }

    @GetMapping("/subscription-company-status/refresh")
    public ResponseEntity<Boolean> refresh() {
        return ResponseEntity.ok(productCompanyStatusService.refresh());
    }

}
