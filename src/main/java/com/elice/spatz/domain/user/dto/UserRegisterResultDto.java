package com.elice.spatz.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserRegisterResultDto {

    private boolean isSuccess;
    private String errorCode;
}
