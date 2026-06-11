package com.ProjetoExtensao.CoinEdu.exceptions;


import org.springframework.data.elasticsearch.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;

@RestControllerAdvice
public class ControllerAdvice {




@ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(IllegalArgumentException exception) {
    ErrorResponse errorResponse = new ErrorResponse(400 , exception.getMessage());
    return ResponseEntity.badRequest().body(errorResponse);
}

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleForbidden(AccessDeniedException exception) {
        ErrorResponse errorResponse = new ErrorResponse(403, "Acesso negado. Token inválido ou ausente.");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException exception) {
        ErrorResponse errorResponse = new ErrorResponse(404 , exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }


@ExceptionHandler(RuntimeException.class)
public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException exception) {
    ErrorResponse errorResponse = new ErrorResponse(503, exception.getMessage());
    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorResponse);
}


@ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception exception) {
    exception.printStackTrace();
    ErrorResponse errorResponse = new ErrorResponse(500, exception.getMessage());
    return ResponseEntity.internalServerError().body(errorResponse);
}


}


