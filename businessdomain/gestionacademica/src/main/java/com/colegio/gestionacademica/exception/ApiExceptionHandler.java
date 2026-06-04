package com.colegio.gestionacademica.exception;

import com.colegio.gestionacademica.common.BusinessRuleException;
import com.colegio.gestionacademica.common.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Manejo centralizado de excepciones (patron similar a un Error Handler/Advice).
 */
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ExceptionResponse> handleBusiness(BusinessRuleException ex) {
        ExceptionResponse body = new ExceptionResponse(
                "Negocio", "Regla de negocio no cumplida", ex.getCode(), ex.getMessage(), "");
        return ResponseEntity.status(ex.getHttpStatus()).body(body);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> handleIllegalArgument(IllegalArgumentException ex) {
        ExceptionResponse body = new ExceptionResponse(
                "Validacion", "Argumento invalido", "400", ex.getMessage(), "");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleGeneric(Exception ex) {
        ExceptionResponse body = new ExceptionResponse(
                "Tecnico", "Error interno", "500", ex.getMessage(), "");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
