package com.manage4me.route.auth;

import com.manage4me.route.account.AccountRepository;
import com.manage4me.route.entities.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.manage4me.route.auth.JwtTokenGenerator.createAuthToken;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AccountRepository accountRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse authentication(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        User user = accountRepository.findByEmail(request.email());
        return new AuthResponse(createAuthToken(user.getEmail()));
    }

    public boolean forgotPassword(String email, String lang) {
        if (isBlank(email)) {
            log.warn("Email is required");
            return false;
        }
        User user = accountRepository.findByEmail(email);
        if (user == null) {
            log.warn("User with email {} not found", email);
            return false;
        }

        String tempPassword = PasswordGenerator.generateRandomPassword(8);

        user.setPassword(passwordEncoder.encode(tempPassword));

        /*
        * TODO Implement logic to send an user email with the temporary password
        * */

        return true;
    }
}
