package com.manage4me.route.account;

public record AccountRequest(String name, String password, String phone, String email, String address, String companyName, String companyRegistration) {
}
