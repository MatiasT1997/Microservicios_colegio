package com.colegio.asistencia.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
