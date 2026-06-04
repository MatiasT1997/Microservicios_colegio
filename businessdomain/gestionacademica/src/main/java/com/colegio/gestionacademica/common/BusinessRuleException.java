package com.colegio.gestionacademica.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Excepcion de negocio estandarizada para Gestion Academica.
 */
@Getter
public class BusinessRuleException extends RuntimeException {

    private final String code;
    private final HttpStatus httpStatus;

    public BusinessRuleException(String code, HttpStatus httpStatus, String mensaje) {
        super(mensaje);
        this.code = code;
        this.httpStatus = httpStatus;
    }
}
