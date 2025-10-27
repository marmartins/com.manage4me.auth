package com.manage4me.route.account;

public record CompanyResponse(String id, String name, String phone, String address, String documentRegistration, PurchaseResponse purchase) {
}
