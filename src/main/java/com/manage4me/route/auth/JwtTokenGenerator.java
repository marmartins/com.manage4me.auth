package com.manage4me.route.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.InvalidClaimException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.security.SecureRandom;
import java.util.Date;
import java.util.function.Function;

public interface JwtTokenGenerator {

    String AUTHENTICATION_KEY = "ETjQhAfYbQZbiryQD9Um0oullLVnKhOFC5PJcRSk2H4=";

    static String createAPIKey() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        SecretKey key = Jwts.SIG.HS256.key().build();
        return Encoders.BASE64.encode(key.getEncoded());
    }

    static String createAuthToken(String login) {
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(AUTHENTICATION_KEY));
        return Jwts.builder()
            .subject(login)
            .signWith(key)
            .compact();
    }

    static String createToken(String login, String companyName, String apiKey) {
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(apiKey));
        return Jwts.builder()
            .subject(login)
            .issuer(companyName != null ? companyName : "")
            .signWith(key)
            .compact();
    }

    static boolean isValid(String claim, String apiKey, String token) throws InvalidClaimException {
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(apiKey));
        try {
            Jwts.parser()
                .verifyWith(key)
                .requireSubject(claim)
                .build()
                .parse(token);
        } catch (InvalidClaimException e ) {
            throw e;
        }
        return true;
    }

    static Jws<Claims> getAllClaims(String apiKey, String token) {
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(apiKey));
        try {
            return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);
        } catch (InvalidClaimException e ) {
            throw e;
        }
    }

    static boolean isExpired(String apiKey, String token) {
        return
            getExpiration(apiKey, token) != null && getExpiration(apiKey, token).after(new Date());
    }

    static String getSubject(String apiKey, String token) {
        return extractClaim(apiKey, token, Claims::getSubject);
    }

    static Date getExpiration(String apiKey, String token) {
        return extractClaim(apiKey, token, Claims::getExpiration);
    }

    static <T> T extractClaim(String apiKey, String token, Function<Claims, T> resolver) {
        Jws<Claims> claims = getAllClaims(apiKey, token);
        return resolver.apply(claims.getPayload());
    }

}
