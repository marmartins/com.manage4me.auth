package com.manage4me.route.account;

public record UpdatePasswordRequest(String newPassword, String confirmPassword) {
}
