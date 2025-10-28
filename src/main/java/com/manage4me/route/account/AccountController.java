package com.manage4me.route.account;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.manage4me.route.account.AccountHelper.getAccountResponse;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/acc")
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/user")
    public ResponseEntity<AccountResponse> userDetail() {
        return ResponseEntity.ok(getAccountResponse().apply(accountService.getLoggedUser()));
    }

}
