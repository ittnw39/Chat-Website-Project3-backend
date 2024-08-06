package com.elice.spatz.domain.user.controller;

import com.elice.spatz.config.CustomUserDetails;
import com.elice.spatz.domain.user.dto.*;
import com.elice.spatz.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;


    // 회원 가입 처리
    @PostMapping("/users")
    public ResponseEntity<UserRegisterResultDto> register(@RequestBody UserRegisterDto userRegisterDto) {
        String hashedPassword = passwordEncoder.encode(userRegisterDto.getPassword());
        userRegisterDto.setPassword(hashedPassword);

        UserRegisterResultDto result = userService.register(userRegisterDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }


    @PostMapping("/apiLogin")
    public ResponseEntity<SignInResponse> apiLogin(@RequestBody SignInRequest signInRequest) {

        SignInResponse signInResponse = userService.signIn(signInRequest);

        return ResponseEntity.status(HttpStatus.OK)
                .body(signInResponse);
    }

    @PatchMapping("/users/password")
    public ResponseEntity<String> updateUserInformation(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody PasswordChangeRequest passwordChangeRequest) {

        userService.changePassword(customUserDetails.getId(), passwordChangeRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("");
    }

}
