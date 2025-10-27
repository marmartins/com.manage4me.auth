package com.manage4me.route.auth;

import com.manage4me.route.account.AccountHelper;
import com.manage4me.route.account.AccountRequest;
import com.manage4me.route.account.AccountResponse;
import com.manage4me.route.account.AccountService;
import com.manage4me.route.entities.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AccountService accountService;
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AccountResponse> register(@RequestBody AccountRequest accountRequest) {
        User user = accountService.register(AccountHelper.getUserFromRequest().apply(accountRequest));
        return ResponseEntity.ok(AccountHelper.getAccountResponse().apply(user));
    }

    @PostMapping("/token")
    public ResponseEntity<AuthResponse> auth(@RequestBody AuthRequest authRequest) {
        return ResponseEntity.ok(authenticationService.authentication(authRequest));
    }

    @PostMapping("/forgot-password/{lang}")
    public ResponseEntity<Boolean> forgotPassword(@RequestBody AuthRequest authRequest,
                                                  @PathVariable String lang) {
        return ResponseEntity.ok(authenticationService.forgotPassword(authRequest.email(), lang));
    }

    @GetMapping("/user")
    public Principal getUser(Principal user) {
        return user;
    }


}
