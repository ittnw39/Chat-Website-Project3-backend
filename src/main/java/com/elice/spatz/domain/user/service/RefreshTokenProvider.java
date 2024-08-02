package com.elice.spatz.domain.user.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class RefreshTokenProvider {

    public static String createRefreshToken(String secret) {
        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder().issuer("spatz")
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + 86400000)) // refresh token의 경우 24시간의 만료시간을 둔다.
                .signWith(secretKey).compact();
    }

}
