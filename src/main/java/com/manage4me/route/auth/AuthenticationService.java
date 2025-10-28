package com.manage4me.route.auth;

import com.manage4me.route.account.AccountRepository;
import com.manage4me.route.entities.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import static com.manage4me.route.auth.JwtTokenGenerator.createAuthToken;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AccountRepository accountRepository;
    private final AuthenticationManager authenticationManager;

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
}
