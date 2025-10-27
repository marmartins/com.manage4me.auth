package com.manage4me.route.account;

import com.manage4me.route.entities.Company;
import com.manage4me.route.entities.User;

@FunctionalInterface
public interface AccountHelper<T, R> {

    R apply(T t);

    static AccountHelper<User, AccountResponse> getAccountResponse() {
        return user -> new AccountResponse(
                user.getId(),
                user.getName(),
                new CompanyResponse(
                        user.getCompany().getId(),
                        user.getCompany().getName(),
                        user.getCompany().getPhone(),
                        user.getCompany().getAddress(),
                        user.getCompany().getDocumentRegistration(),
                        new PurchaseResponse(
                                user.getCompany().getPurchase().getId(),
                                user.getCompany().getPurchase().getStatus(),
                                user.getCompany().getPurchase().getState(),
                                user.getCompany().getPurchase().getPlatform(),
                                user.getCompany().getPurchase().getCreatedDate(),
                                user.getCompany().getPurchase().getPurchaseDate(),
                                new ProductResponse(
                                        user.getCompany().getPurchase().getProduct().getId(),
                                        user.getCompany().getPurchase().getProduct().getSku(),
                                        user.getCompany().getPurchase().getProduct().getDescription(),
                                        user.getCompany().getPurchase().getProduct().getTitle(),
                                        user.getCompany().getPurchase().getProduct().getStatus(),
                                        user.getCompany().getPurchase().getProduct().getType()
                                )
                        )
                ),
                user.getEmail(),
                user.getRole().name()
        );
    }

    static AccountHelper<AccountRequest, User> getUserFromRequest() {
        return request -> {
            User user = new User();
            user.setName(request.name());
            user.setPassword(request.password());
            user.setEmail(request.email());
            user.setCompany(new Company());
            user.getCompany().setName(request.companyName());
            user.getCompany().setAddress(request.address());
            user.getCompany().setDocumentRegistration(request.companyRegistration());
            user.getCompany().setPhone(request.phone());
            return user;
        };
    }

}
