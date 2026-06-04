package com.colegio.gestionacademica.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Cuerpo estandar de respuesta de error. */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionResponse {
    private String tipo;
    private String titulo;
    private String code;
    private String detalle;
    private String instancia;
}
