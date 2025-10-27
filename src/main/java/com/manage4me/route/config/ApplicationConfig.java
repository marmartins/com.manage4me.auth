package com.manage4me.route.config;


import com.manage4me.route.auth.AuthorizeUserService;

import com.manage4me.route.commons.ControllerExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Locale;
import java.util.ResourceBundle;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class ApplicationConfig {

    private final AuthorizeUserService authorizeUserService;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(authorizeUserService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ResourceBundle resourceBundle() {
        return ResourceBundle.getBundle("messages", new Locale("pt", "BR"));
    }

    @Bean
    public ControllerExceptionHandler exceptionHandler(ResourceBundle resourceBundle){
        return new ControllerExceptionHandler(resourceBundle);
    }

}
