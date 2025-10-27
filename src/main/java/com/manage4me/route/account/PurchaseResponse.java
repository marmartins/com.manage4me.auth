package com.manage4me.route.account;

import com.manage4me.route.entities.Status;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record PurchaseResponse(String id, Status status, String state, String platform, LocalDateTime createdDate, String purchaseDate, ProductResponse product) {
}
