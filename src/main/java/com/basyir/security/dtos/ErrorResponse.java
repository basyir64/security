package com.basyir.security.dtos;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Setter
@Getter
public class ErrorResponse {

    private final String message;
    private final String code;

}
