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

    private final int reIssueLimit = 5;
    private final UserRefreshTokenRepository userRefreshTokenRepository;
    private final UserRepository userRepository;
    private final Environment env;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private Long accessTokenExpiration = 30000L;
    private Long refreshTokenExpiration = 86400000L;

    // Access Token 을 생성하는 메소드
    public String createAccessToken(String secret, String username, String authorities) {
        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder().issuer("spatz").subject(username)
                .claim("username", username)
                .claim("authorities", authorities)
                .issuedAt(new Date())
                .expiration(new Date((new Date().getTime() + accessTokenExpiration))) // access token의 경우 유효시간을 30분으로 설정 1800000
                .signWith(secretKey).compact();
    }

    // Refresh Token 을 발급하는 메소드
    public String createRefreshToken(String secret) {
        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder().issuer("spatz")
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + refreshTokenExpiration)) // refresh token의 경우 24시간의 만료시간을 둔다.
                .signWith(secretKey).compact();
    }

    @Transactional
    // 기존에 클라이언트가 보낸 만료된 oldAccessToken 을 이용하여 새 access Token 을 발급하는 과정이다.
    public String recreateAccessToken(String oldAccessToken) throws JsonProcessingException {
        String username = decodeJwtPayloadUsername(oldAccessToken);
        String authorities = decodeJwtPayloadAuthorities(oldAccessToken);

        Users user = userRepository.findByEmail(username).orElseThrow(() -> new IllegalStateException("해당하는 유저정보가 존재하지 않습니다"));

        UserRefreshToken userRefreshToken = userRefreshTokenRepository
                .findByUserAndReIssueCountLessThan(user, reIssueLimit)
                .orElseThrow(() -> new ExpiredJwtException(null, null, "Refresh Token Expired!"));

        userRefreshToken.increaseReIssueCount();

        String secret = env.getProperty(ApplicationConstants.JWT_SECRET_KEY, ApplicationConstants.JWT_SECRET_DEFAULT_VALUE);

        return createAccessToken(secret, username, authorities);

    }

    // 전달된 Refresh token 이 유효한 지 검사하는 메소드
    // 현재 유저가 보유하고 있는 Refresh token 이 맞는 지
    // 그리고 그 Refresh Token 이 access token 을 발급할 수 있는 상태인지(재발급 5회 이하)를 점검한다.
    @Transactional
    public void validateRefreshToken(String refreshToken, String oldAccessToken) throws JsonProcessingException {
        validateAndParseToken(refreshToken);

        String username = decodeJwtPayloadUsername(oldAccessToken);

        Users user = userRepository.findByEmail(username).orElseThrow(() -> new IllegalStateException("해당하는 유저정보가 존재하지 않습니다"));

        userRefreshTokenRepository.findByUserAndReIssueCountLessThan(user, reIssueLimit)
                        .filter(userRefreshToken -> userRefreshToken.validateRefreshToken(refreshToken))
                                .orElseThrow(() -> new ExpiredJwtException(null, null, "Refresh token Expired!"));
    }

    // 전해진 토큰을 점검(유효기간 만료 여부 + 변조 여부)하는 동시에, 해당 토큰의 페이로드(클레임)부분을 반환하는 메소드.
    private Claims validateAndParseToken(String refreshToken) {

        String secret = env.getProperty(ApplicationConstants.JWT_SECRET_KEY, ApplicationConstants.JWT_SECRET_DEFAULT_VALUE);
        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        return Jwts.parser()
                .verifyWith(secretKey)
                .build()// 실질적으로 access JWT 토큰의 변조 여부 + 유효기간 만료 여부가 검사되는 부분이다.
                .parseSignedClaims(refreshToken)
                .getPayload();
    }

    // 전달된 토큰에서 username 정보를 추출하는 함수
    private String decodeJwtPayloadUsername(String oldAccessToken) throws JsonProcessingException {
        return objectMapper.readValue(
                new String(Base64.getDecoder().decode(oldAccessToken.split("\\.")[1]), StandardCharsets.UTF_8),
                Map.class
        ).get("username").toString();
    }

    // 전달된 토큰에서 authorities 정보를 추출하는 함수
    private String decodeJwtPayloadAuthorities(String oldAccessToken) throws JsonProcessingException {
        return objectMapper.readValue(
                new String(Base64.getDecoder().decode(oldAccessToken.split("\\.")[1]), StandardCharsets.UTF_8),
                Map.class
        ).get("authorities").toString();
    }

}
