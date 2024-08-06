package com.elice.spatz.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtCheckResultDto {
    private final boolean isValid;
}
