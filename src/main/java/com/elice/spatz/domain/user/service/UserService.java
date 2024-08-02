package com.elice.spatz.domain.user.service;

import com.elice.spatz.constants.ApplicationConstants;
import com.elice.spatz.domain.user.dto.SignInRequest;
import com.elice.spatz.domain.user.dto.SignInResponse;
import com.elice.spatz.domain.user.dto.UserRegisterDto;
import com.elice.spatz.domain.user.dto.UserRegisterResultDto;
import com.elice.spatz.domain.user.entity.Users;
import com.elice.spatz.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final Environment env;

    @Transactional
    public SignInResponse signIn(SignInRequest signInRequest) {

        String jwt = "";
        UsernamePasswordAuthenticationToken authentication = UsernamePasswordAuthenticationToken.unauthenticated(signInRequest.getUsername(), signInRequest.getPassword());
        Authentication authenticationResponse = authenticationManager.authenticate(authentication);

        if(null != authenticationResponse && authenticationResponse.isAuthenticated()) {
            if (null != env) {
                // 서명용 키
                String secret = env.getProperty(ApplicationConstants.JWT_SECRET_KEY, ApplicationConstants.JWT_SECRET_DEFAULT_VALUE);

                // JWT Access Token 생성
                jwt = AccessTokenProvider.createAccessToken(secret,
                        authenticationResponse.getName(),
                        authenticationResponse.getAuthorities().stream().map(
                                GrantedAuthority::getAuthority).collect(Collectors.joining(",")));

            }
        }

        return new SignInResponse(signInRequest.getUsername(), jwt);	// 생성자에 토큰 추가
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
