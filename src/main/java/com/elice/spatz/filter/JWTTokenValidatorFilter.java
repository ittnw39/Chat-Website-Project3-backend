package com.elice.spatz.filter;

import com.elice.spatz.constants.ApplicationConstants;
import com.elice.spatz.domain.user.repository.UserRefreshTokenRepository;
import com.elice.spatz.domain.user.repository.UserRepository;
import com.elice.spatz.domain.user.service.AccessTokenProvider;
import com.elice.spatz.domain.user.service.RefreshTokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JWTTokenValidatorFilter extends OncePerRequestFilter {

    private boolean isExecutedBefore = false;

    private final AccessTokenProvider accessTokenProvider;
    private final RefreshTokenProvider refreshTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 클라이언트가 보낸 access Token 추출하기
        // Bearer 공백 이후의 문자열을 추출한다.
        String jwt = parseBearerToken(request, ApplicationConstants.JWT_HEADER);

        Environment env = getEnvironment();
        String secret = env.getProperty(ApplicationConstants.JWT_SECRET_KEY, ApplicationConstants.JWT_SECRET_DEFAULT_VALUE);
        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        // 만약 클라이언트가 JWT 토큰을 보내지 않았다면 필터링을 수행하지 않는다.
        if(jwt != null) {
            try {

                // 토큰 유효성 검사 이후 페이로드 부분을 추출
                Claims claims = Jwts.parser()
                        .verifyWith(secretKey)
                        .build()
                        .parseSignedClaims(jwt) // 실질적으로 access JWT 토큰의 변조 여부 + 유효기간 만료 여부가 검사되는 부분이다.
                        .getPayload();

                String username = String.valueOf(claims.get("username"));
                String authorities = String.valueOf(claims.get("authorities"));

                // 인증을 마치고 인증정보를 SecurityContext에 담는 과정
                Authentication authentication = new UsernamePasswordAuthenticationToken(username, null,
                        AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));

                SecurityContextHolder.getContext().setAuthentication(authentication);


            } catch (ExpiredJwtException e) {
                // 클라이언트가 보낸 Access Token 유효기간 만료 시 실행되는 블록
                reissueAccessToken(request, response, e);
            }
            catch (SignatureException e) {
                // 변조된 JWT 토큰을 보냈을 시 실행되는 블록
                throw new BadCredentialsException("Token Tampered.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        filterChain.doFilter(request, response);
    }

    // 클라이언트가 보낸 Refresh-Token 을 바탕으로 Access Token 을 재 발급하여 response 의 Header 에 넣는다.
    private void reissueAccessToken(HttpServletRequest request, HttpServletResponse response, ExpiredJwtException exception) {

        try {
            String refreshToken = parseBearerToken(request, "Refresh-Token");

            if (refreshToken == null) {
                throw exception;
            }

            String oldAccessToken = parseBearerToken(request, ApplicationConstants.JWT_HEADER);

            refreshTokenProvider.validateRefreshToken(refreshToken, oldAccessToken);

            String newAccessToken = refreshTokenProvider.recreateAccessToken(oldAccessToken);

            Environment env = getEnvironment();
            String secret = env.getProperty(ApplicationConstants.JWT_SECRET_KEY, ApplicationConstants.JWT_SECRET_DEFAULT_VALUE);
            SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

            Claims newClaim = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()// 실질적으로 access JWT 토큰의 변조 여부 + 유효기간 만료 여부가 검사되는 부분이다.
                    .parseSignedClaims(newAccessToken)
                    .getPayload();

            String username = String.valueOf(newClaim.get("username"));
            String authorities = String.valueOf(newClaim.get("authorities"));

            Authentication authentication = new UsernamePasswordAuthenticationToken(username, null,
                    AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            response.setHeader("New-Access-Token", newAccessToken);
        } catch (Exception e) {
            request.setAttribute("exception", e);
        }
    }

    // 클라이언트 요청으로부터, Access Token 과 Refresh-Token 을 String 형태로 추출하는 메소드.
    // Access Token 과 Refresh-Token 중 어느 것을 추출할 것인지는 두 번째 인자로 전달하여 명시
    private String parseBearerToken(HttpServletRequest request, String headerName) {
        return Optional.ofNullable(request.getHeader(headerName))
                .filter(headerValue -> headerValue.substring(0, 6).equalsIgnoreCase("Bearer"))
                .map(headerValue -> headerValue.substring(7))
                .orElse(null);
    }

    // 로그인 시에는 JWT 토큰이 없을 것이므로
    // JWT Token 검증작업이 수행되면 안 된다.
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getServletPath().equals("/apiLogin");
    }
}
