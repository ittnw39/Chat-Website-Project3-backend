package com.elice.spatz.domain.user.service;

import com.elice.spatz.constants.ApplicationConstants;
import com.elice.spatz.domain.user.entity.UserRefreshToken;
import com.elice.spatz.domain.user.entity.Users;
import com.elice.spatz.domain.user.repository.UserRefreshTokenRepository;
import com.elice.spatz.domain.user.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenProvider {

    private final int reIssueLimit = 5;
    private final UserRefreshTokenRepository userRefreshTokenRepository;
    private final UserRepository userRepository;
    private final Environment env;
    private final AccessTokenProvider accessTokenProvider;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public static String createRefreshToken(String secret) {
        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder().issuer("spatz")
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + 86400000)) // refresh token의 경우 24시간의 만료시간을 둔다.
                .signWith(secretKey).compact();
    }

    public String recreateAccessToken(String oldAccessToken) throws JsonProcessingException {
        String username = decodeJwtPayloadUsername(oldAccessToken);
        String authorities = decodeJwtPayloadAuthorities(oldAccessToken);

        Users user = userRepository.findByEmail(username).orElseThrow(() -> new IllegalStateException("해당하는 유저정보가 존재하지 않습니다"));

        userRefreshTokenRepository.findByUserAndReIssueCountLessThan(user, reIssueLimit)
                .ifPresentOrElse(
                        UserRefreshToken::increaseReIssueCount,
                        () -> {throw new ExpiredJwtException(null, null, "Refresh Token Expired");}
                );
        String secret = env.getProperty(ApplicationConstants.JWT_SECRET_KEY, ApplicationConstants.JWT_SECRET_DEFAULT_VALUE);

        return accessTokenProvider.createAccessToken(secret, username, authorities);

    }

    @Transactional
    public void validateRefreshToken(String refreshToken, String oldAccessToken) throws JsonProcessingException {
        validateAndParseToken(refreshToken);

        String username = decodeJwtPayloadUsername(oldAccessToken);

        Users user = userRepository.findByEmail(username).orElseThrow(() -> new IllegalStateException("해당하는 유저정보가 존재하지 않습니다"));

        userRefreshTokenRepository.findByUserAndReIssueCountLessThan(user, reIssueLimit)
                        .filter(userRefreshToken -> userRefreshToken.validateRefreshToken(refreshToken))
                                .orElseThrow(() -> new ExpiredJwtException(null, null, "Refresh token Expired!"));
    }

    private Claims validateAndParseToken(String refreshToken) {

        String secret = env.getProperty(ApplicationConstants.JWT_SECRET_KEY, ApplicationConstants.JWT_SECRET_DEFAULT_VALUE);
        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        return Jwts.parser()
                .verifyWith(secretKey)
                .build()// 실질적으로 access JWT 토큰의 변조 여부 + 유효기간 만료 여부가 검사되는 부분이다.
                .parseSignedClaims(refreshToken)
                .getPayload();
    }

    private String decodeJwtPayloadUsername(String oldAccessToken) throws JsonProcessingException {
        return objectMapper.readValue(
                new String(Base64.getDecoder().decode(oldAccessToken.split("\\.")[1]), StandardCharsets.UTF_8),
                Map.class
        ).get("username").toString();
    }

    private String decodeJwtPayloadAuthorities(String oldAccessToken) throws JsonProcessingException {
        return objectMapper.readValue(
                new String(Base64.getDecoder().decode(oldAccessToken.split("\\.")[1]), StandardCharsets.UTF_8),
                Map.class
        ).get("authorities").toString();
    }

}
