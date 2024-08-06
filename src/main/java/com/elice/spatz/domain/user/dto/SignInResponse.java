package com.elice.spatz.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignInResponse {
    private String username;
    private String accessToken;
    private String refreshToken;
}
