package com.elice.spatz.exception.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class UserErrorResponse {

    private String status;
    private String code;
    private String message;

}
