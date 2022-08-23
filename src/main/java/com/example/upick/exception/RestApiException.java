package com.example.upick.exception;

import lombok.*;
import org.springframework.http.HttpStatus;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RestApiException {
    private String errorMessage;
    private HttpStatus httpStatus;
}