package com.example.userauthentication.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseError {

    private LocalDateTime timestamp;
    private int status;
    private String message;
    private String path;
}
