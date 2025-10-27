package com.manage4me.route.auth;

import com.manage4me.route.commons.ControllerExceptionHandler;
import com.manage4me.route.entities.Company;
import com.manage4me.route.entities.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static com.manage4me.route.commons.ErrorCodes.APP_0005;

@FunctionalInterface
public interface AuthFunctions<T, R> {
    R apply(T t);

    static AuthFunctions<ControllerExceptionHandler, User> getLoggedUser() {
        return exceptionHandler -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                exceptionHandler.throwException(APP_0005::name);
            }
            User user = (User) authentication.getPrincipal();
            return user;
        };
    }

    static AuthFunctions<ControllerExceptionHandler, Company> getUserCompany() {
        return exceptionHandler -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                exceptionHandler.throwException(APP_0005::name);
            }
            User user = (User) authentication.getPrincipal();
            return user.getCompany();
        };
    }
}
