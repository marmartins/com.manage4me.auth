package com.manage4me.route.account;

import com.manage4me.route.entities.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.manage4me.route.account.AccountHelper.getAccountResponse;
import static com.manage4me.route.account.AccountHelper.getUserFromRequest;
import static java.util.stream.Collectors.toList;

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
