package com.colegio.asistencia.exception;

import com.colegio.asistencia.common.BusinessRuleException;
import com.colegio.asistencia.common.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ExceptionResponse> handleBusiness(BusinessRuleException ex) {
        ExceptionResponse body = new ExceptionResponse(
                "Negocio", "Regla de negocio no cumplida", ex.getCode(), ex.getMessage(), "");
        return ResponseEntity.status(ex.getHttpStatus()).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleGeneric(Exception ex) {
        ExceptionResponse body = new ExceptionResponse(
                "Tecnico", "Error interno", "500", ex.getMessage(), "");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
