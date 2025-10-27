package com.manage4me.route.account;

import com.manage4me.route.entities.ProductType;
import com.manage4me.route.entities.Status;

public record ProductResponse(String id, String sku, String description, String title, Status status, ProductType type) {
}
