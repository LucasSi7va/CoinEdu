package com.ProjetoExtensao.CoinEdu.Exceptions;


import org.springframework.data.elasticsearch.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

@ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException exception) {
    ErrorResponse errorResponse = new ErrorResponse(404 , exception.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

@ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(IllegalArgumentException exception) {
    ErrorResponse errorResponse = new ErrorResponse(400 , exception.getMessage());
    return ResponseEntity.badRequest().body(errorResponse);
}


@ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception exception) {
    ErrorResponse errorResponse = new ErrorResponse(500 , "Erro interno do servidor");
    return ResponseEntity.internalServerError().body(errorResponse);
}


}


