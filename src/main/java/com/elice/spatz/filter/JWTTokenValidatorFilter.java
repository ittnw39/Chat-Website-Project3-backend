package com.elice.spatz.filter;

import com.elice.spatz.constants.ApplicationConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class JWTTokenValidatorFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 클라이언트가 보낸 access Token 추출하기
        String jwt = request.getHeader(ApplicationConstants.JWT_HEADER);

        // 만약 클라이언트가 JWT 토큰을 보내지 않았다면 필터링을 수행하지 않는다.
        if(jwt != null) {
            try {
                Environment env = getEnvironment();

                String secret = env.getProperty(ApplicationConstants.JWT_SECRET_KEY, ApplicationConstants.JWT_SECRET_DEFAULT_VALUE);
                SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));


                Claims claims = Jwts.parser()
                        .verifyWith(secretKey)
                        .build()// access JWT 토큰의 변조 여부 + 유효기간 만료 여부가 검사되는 부분
                        .parseSignedClaims(jwt)
                        .getPayload();

                // 만약 access Token 이 만료되었다면, refresh 토큰을 통하여 access Token 을 새로 발급받아야 한다.


                String username = String.valueOf(claims.get("username"));
                String authorities = String.valueOf(claims.get("authorities"));

                Authentication authentication = new UsernamePasswordAuthenticationToken(username, null,
                        AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));

                SecurityContextHolder.getContext().setAuthentication(authentication);


            } catch (ExpiredJwtException eje) {
                // Access Token 유효기간 만료 시 Refresh Token을 이용하여 액세스 토큰을 재발급하는 로직 작성하기
                System.out.println("eje = " + eje.getMessage());
            }
            catch (Exception e) {
                throw new BadCredentialsException("Token Tampered.");
            }
        }

        filterChain.doFilter(request, response);
    }

    // 로그인 시에는 JWT 토큰이 없을 것이므로
    // JWT Token 검증작업이 수행되면 안 된다.
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getServletPath().equals("/apiLogin");
    }
}
