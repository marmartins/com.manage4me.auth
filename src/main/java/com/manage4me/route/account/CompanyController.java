package com.manage4me.route.account;

import com.manage4me.route.entities.Company;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/acc")
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping("/company/{id}")
    public ResponseEntity<Company> findById(@PathVariable String id) {
        return ResponseEntity.ok(companyService.findById(id));
    }

}
