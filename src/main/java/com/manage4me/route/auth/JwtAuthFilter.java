package com.manage4me.route.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.manage4me.route.auth.JwtTokenGenerator.*;
import static org.springframework.util.StringUtils.hasLength;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {


    public static final String AUTHORIZATION = "Authorization";
    public static final String SIGNATURE = "X-signature";
    public static final String BEARER_ = "Bearer ";

    private final AuthorizeUserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader(AUTHORIZATION);
        String authToken = request.getHeader("X-token");
        if (!hasLength(authHeader) || !authHeader.startsWith(BEARER_)) {
            if (request.getRequestURL().toString().contains("auth")) {
                log.warn("Request [{}] with no authorization header", "auth");
            } else {
                log.error("Invalid Request [{}] with no authorization header", request.getRequestURL().toString());
            }
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(BEARER_.length());
        String authSignature = request.getHeader(SIGNATURE);
        String userLogin;
        boolean isExpired;
        if (hasLength(authSignature) && hasLength(authToken)) {
            userLogin = getSubject(authSignature, authToken);
            isExpired = isExpired(authSignature, authToken);
        } else {
            userLogin = getSubject(AUTHENTICATION_KEY, token);
            isExpired = isExpired(AUTHENTICATION_KEY, token);
        }

        if (hasLength(userLogin) && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userService.loadUserByUsername(userLogin);
            if (!isExpired) {
                UsernamePasswordAuthenticationToken autheToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
                );
                autheToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(autheToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
