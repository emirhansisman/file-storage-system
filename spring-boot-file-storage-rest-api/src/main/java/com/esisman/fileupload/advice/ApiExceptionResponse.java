package com.esisman.fileupload.advice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
public class ApiExceptionResponse {
    private final String message;
    private final HttpStatus httpStatus;
    private final LocalDate timestamp;
}
