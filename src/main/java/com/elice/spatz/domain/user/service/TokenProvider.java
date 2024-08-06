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
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TokenProvider {

    private final UserRefreshTokenRepository userRefreshTokenRepository;
    private final UserRepository userRepository;
    private final Environment env;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private SecretKey secretKey = null;

    // Bean 으로 등록이 된 이후에 실행되도록 하여, secret Key 를 초기화 시킨다.
    @PostConstruct
    public void initializeTokenProvider() {
        String secret = env.getProperty(ApplicationConstants.JWT_SECRET_KEY, ApplicationConstants.JWT_SECRET_DEFAULT_VALUE);
        secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }


    // Access Token 을 생성하는 메소드
    public String createAccessToken(Long userId, String username, String authorities) {

        return Jwts.builder().issuer("spatz").subject(username)
                .claim("userId", userId)
                .claim("username", username)
                .claim("authorities", authorities)
                .issuedAt(new Date())
                .expiration(new Date((new Date().getTime() + ApplicationConstants.ACCESS_TOKEN_EXPIRATION)))
                .signWith(secretKey).compact();
    }

    // Refresh Token 을 발급하는 메소드
    public String createRefreshToken() {

        return Jwts.builder().issuer("spatz")
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + ApplicationConstants.REFRESH_TOKEN_EXPIRATION))
                .signWith(secretKey)
                .compact();
    }

    // 기존에 클라이언트가 보낸 만료된 oldAccessToken 을 이용하여 새 access Token 을 발급하는 과정이다.
    @Transactional
    public String recreateAccessToken(String oldAccessToken) throws JsonProcessingException {
        String username = getPayloadFromJWTToken(oldAccessToken).get("username").toString();
        String authorities = getPayloadFromJWTToken(oldAccessToken).get("authorities").toString();

        Users user = userRepository.findByEmail(username).orElseThrow(() -> new IllegalStateException("해당하는 유저정보가 존재하지 않습니다"));

        // 현재 클라이언트가 보낸 Refresh Token 의 주인이 맞는 지 검증 + 재발급 횟수가 남아있는 지 검증
        UserRefreshToken userRefreshToken = userRefreshTokenRepository
                .findByUserAndReIssueCountLessThan(user, ApplicationConstants.REFRESH_TOKEN_RE_ISSUE_LIMIT)
                .orElseThrow(() -> new ExpiredJwtException(null, null, "Refresh Token Expired!"));

        // Access Token 재발급 시 reIssue count를 증가시킨다.
        userRefreshToken.increaseReIssueCount();

        String secret = env.getProperty(ApplicationConstants.JWT_SECRET_KEY, ApplicationConstants.JWT_SECRET_DEFAULT_VALUE);

        return createAccessToken(user.getId(), username, authorities);

    }

    // 전달된 Refresh token 이 유효한 지 검사하는 메소드
    // 현재 유저가 보유하고 있는 Refresh token 이 맞는 지
    // 그리고 그 Refresh Token 이 access token 을 발급할 수 있는 상태인지(재발급 5회 이하)를 점검한다.
    @Transactional
    public void validateRefreshToken(String refreshToken, String oldAccessToken) throws JsonProcessingException {
        validateTokenIsExpiredOrTampered(refreshToken);

        String username = getPayloadFromJWTToken(oldAccessToken).get("username").toString();

        Users user = userRepository.findByEmail(username).orElseThrow(() -> new IllegalStateException("해당하는 유저정보가 존재하지 않습니다"));

        userRefreshTokenRepository.findByUserAndReIssueCountLessThan(user, ApplicationConstants.REFRESH_TOKEN_RE_ISSUE_LIMIT)
                        .filter(userRefreshToken -> userRefreshToken.validateRefreshToken(refreshToken))
                                .orElseThrow(() -> new ExpiredJwtException(null, null, "Refresh token Expired!"));
    }

    public void validateTokenIsExpiredOrTampered (String token) {
        Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token) // 실질적으로 access JWT 토큰의 변조 여부 + 유효기간 만료 여부가 검사되는 부분이다.
                .getPayload();

    }

    public Map getPayloadFromJWTToken (String token) throws JsonProcessingException {
        return objectMapper.readValue(
                new String(Base64.getDecoder().decode(token.split("\\.")[1]), StandardCharsets.UTF_8),
                Map.class
        );
    }

}
