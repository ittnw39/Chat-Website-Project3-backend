package com.elice.spatz.domain.user.controller;

import com.elice.spatz.constants.ApplicationConstants;
import com.elice.spatz.domain.user.dto.*;
import com.elice.spatz.domain.user.service.AccessTokenProvider;
import com.elice.spatz.domain.user.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;


    @PostMapping("/register")
    public ResponseEntity<UserRegisterResultDto> processRegister(@RequestBody UserRegisterDto userRegisterDto) {
        String hashedPassword = passwordEncoder.encode(userRegisterDto.getPassword());
        userRegisterDto.setPassword(hashedPassword);

        UserRegisterResultDto result = userService.register(userRegisterDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/precheck-email")
    public ResponseEntity<UserRegisterResultDto> processRegister(@RequestParam String email) {

        boolean result = userService.preCheckEmail(email);
        HttpStatus code = result ? HttpStatus.OK : HttpStatus.CONFLICT;

        return ResponseEntity.status(code)
                .body(new UserRegisterResultDto(result, null));
    }

    @PostMapping("/apiLogin")
    public ResponseEntity<SignInResponse> apiLogin(@RequestBody SignInRequest signInRequest) {

        SignInResponse signInResponse = userService.signIn(signInRequest);

        return ResponseEntity.status(HttpStatus.OK).header(ApplicationConstants.JWT_HEADER, signInResponse.getJwtToken())
                .body(new SignInResponse(HttpStatus.OK.getReasonPhrase(), signInResponse.getJwtToken()));
    }


}
