package com.manage4me.route.account;

public record AccountResponse(String id, String name, CompanyResponse company, String email, String role) {
}
