package com.colegio.gestionacademica.factory;

import com.colegio.gestionacademica.entities.Evaluacion;
import java.time.LocalDate;

/**
 * Patron de diseno: FACTORY METHOD.
 *
 * Declara el metodo fabrica {@code crearEvaluacion} que las subclases concretas
 * implementan para producir el tipo especifico de Evaluacion. El codigo cliente
 * (servicio/controller) trabaja contra esta abstraccion y no necesita conocer la
 * clase concreta ni los valores por defecto de cada tipo de evaluacion.
 *
 * Justificacion (segun informe): desacopla la creacion de objetos del codigo
 * cliente, permitiendo agregar nuevos tipos de evaluacion sin modificar el
 * codigo existente (principio Abierto/Cerrado).
 */
public abstract class EvaluacionFactory {

    /** Metodo fabrica que cada creador concreto implementa. */
    protected abstract Evaluacion crearEvaluacion();

    /**
     * Metodo plantilla que usa el metodo fabrica. Aplica los datos comunes y
     * delega la construccion del tipo concreto a la subclase.
     */
    public Evaluacion construir(String titulo, Double ponderacion, LocalDate fecha) {
        Evaluacion evaluacion = crearEvaluacion();
        evaluacion.setTitulo(titulo);
        evaluacion.setPonderacion(ponderacion);
        evaluacion.setFecha(fecha != null ? fecha : LocalDate.now());
        return evaluacion;
    }
}
