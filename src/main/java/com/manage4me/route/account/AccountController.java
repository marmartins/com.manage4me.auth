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

    @GetMapping("/account/{id}")
    public ResponseEntity<AccountResponse> findById(@PathVariable String id){
        return ResponseEntity.ok(
            getAccountResponse().apply(
                accountService.findById(id)));
    }

    @PostMapping("/account-by-name")
    public ResponseEntity<List<AccountResponse>> findByName(@RequestBody AccountRequest request){
        return ResponseEntity.ok(accountService.findByName(request)
                .stream()
                .map(user -> getAccountResponse().apply(user))
                .collect(toList()));
    }

    @GetMapping("/user")
    public ResponseEntity<AccountResponse> userDetail() {
        return ResponseEntity.ok(getAccountResponse().apply(accountService.getLoggedUser()));
    }

    @PutMapping("/account/{id}")
    public ResponseEntity<AccountResponse> update(@RequestBody User entity,
                                                  @PathVariable String id) {
        return ResponseEntity.ok(
                getAccountResponse().apply(
                        accountService.update(entity, id)));
    }

    @PostMapping("/account")
    public ResponseEntity<AccountResponse> create(@RequestBody User entity) {
        return ResponseEntity.ok(
                getAccountResponse().apply(
                        accountService.create(entity)));
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<AccountResponse>> getAll() {
        return ResponseEntity.ok(
                accountService.findAll()
                        .stream()
                        .map(user -> getAccountResponse().apply(user))
                        .collect(toList())
        );

    }

    @DeleteMapping("/account/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable String id) {
        return ResponseEntity.ok(accountService.delete(id));
    }

    @DeleteMapping("/delete-root-account/{id}")
    public ResponseEntity<Boolean> deleteRootAccount(@PathVariable String id) {
        return ResponseEntity.ok(accountService.deleteRootAccount(id));
    }

    @PostMapping("/account/update-password/{id}")
    public ResponseEntity<Boolean> updatePassword(
            @PathVariable String id,
            @RequestBody UpdatePasswordRequest request) {
        accountService.updatePassword(request, id);
        return ResponseEntity.ok(true);
    }

}
