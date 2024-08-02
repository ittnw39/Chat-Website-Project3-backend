package com.elice.spatz.domain.user.service;

import com.elice.spatz.constants.ApplicationConstants;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class AccessTokenProvider {

    public String createAccessToken(String secret, String username, String authorities) {
        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder().issuer("spatz").subject(username)
                .claim("username", username)
                .claim("authorities", authorities)
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + 50000)) // access token의 경우 유효시간을 30분으로 설정 1800000
                .signWith(secretKey).compact();
    }

}
