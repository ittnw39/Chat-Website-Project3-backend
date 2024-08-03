package com.elice.spatz.domain.user.service;

import com.elice.spatz.constants.ApplicationConstants;
import com.elice.spatz.domain.user.dto.SignInRequest;
import com.elice.spatz.domain.user.dto.SignInResponse;
import com.elice.spatz.domain.user.dto.UserRegisterDto;
import com.elice.spatz.domain.user.dto.UserRegisterResultDto;
import com.elice.spatz.domain.user.entity.UserRefreshToken;
import com.elice.spatz.domain.user.entity.Users;
import com.elice.spatz.domain.user.repository.UserRefreshTokenRepository;
import com.elice.spatz.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;

import java.util.ArrayList;
import java.util.List;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserRefreshTokenRepository userRefreshTokenRepository;
    private final AuthenticationManager authenticationManager;
    private final Environment env;
    private final TokenProvider tokenProvider;

    @Transactional
    public SignInResponse signIn(SignInRequest signInRequest) {

        String accessJwtToken = "";
        String refreshJwtToken = "";

        // 사용자가 입력한 아이디와 비밀번호를 통해서 인증작업 진행
        UsernamePasswordAuthenticationToken authentication = UsernamePasswordAuthenticationToken.unauthenticated(signInRequest.getUsername(), signInRequest.getPassword());

        // 사용자가 입력한 데이터로 수행한 인증 결과를 반환한다.
        Authentication authenticationResponse = authenticationManager.authenticate(authentication);

        // 인증이 성공적으로 수행되었다면 다음 블록을 수행
        if(null != authenticationResponse && authenticationResponse.isAuthenticated()) {

            Users user = userRepository.findByEmail(authenticationResponse.getName())
                    .orElseThrow(() -> new IllegalStateException("입력한 이메일에 해당하는 사용자가 없습니다."));

            // JWT Access Token 생성
            accessJwtToken = tokenProvider.createAccessToken(
                    user.getId(),
                    authenticationResponse.getName(),
                    authenticationResponse.getAuthorities().stream().map(
                            GrantedAuthority::getAuthority).collect(Collectors.joining(",")));

            // JWT Refresh Token 생성
            String refreshToken = tokenProvider.createRefreshToken();
            refreshJwtToken = refreshToken;

            // 이미 DB에 저장중인 Refresh Token 이 있다면 갱신하고, 없다면 DB에 추가하기
            userRefreshTokenRepository.findByUser(user).ifPresentOrElse(
                    it -> it.updateRefreshToken(refreshToken),
                    () -> userRefreshTokenRepository.save(new UserRefreshToken(user, refreshToken))
            );
        }

        return new SignInResponse(signInRequest.getUsername(), accessJwtToken, refreshJwtToken);	// 생성자에 토큰 추가
    }

    public UserRegisterResultDto register(UserRegisterDto userRegisterDto) {
        Users newUser = new Users(userRegisterDto.getEmail(),
                userRegisterDto.getPassword(),
                userRegisterDto.getNickname(),
                null,
                false,
                "user", true);

        userRepository.save(newUser);

        return new UserRegisterResultDto(true, null);
    }

    // 이미 입력한 이메일에 해당하는 사용자가 존재하면 false 반환 >> 실패의 의미
    // 이미 입력한 이메일에 해당하는 사용자가 없다면 true 반환 >> 성공의 의미
    public boolean preCheckEmail(String email) {
        return userRepository.findUsersByEmail(email).isEmpty();
    }

}
