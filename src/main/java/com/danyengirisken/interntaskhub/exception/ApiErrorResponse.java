package com.danyengirisken.interntaskhub.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * REST hatalarinda donulen standart hata govdesi (carbon ApiErrorResponse stili).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiErrorResponse {
    private long timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
}
