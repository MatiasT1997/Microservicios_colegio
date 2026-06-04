package com.colegio.gestionacademica.factory;

import com.colegio.gestionacademica.entities.Evaluacion;

/** Creador concreto: produce evaluaciones de tipo TAREA. */
public class TareaFactory extends EvaluacionFactory {
    @Override
    protected Evaluacion crearEvaluacion() {
        Evaluacion e = new Evaluacion();
        e.setTipo(Evaluacion.TipoEvaluacion.TAREA);
        return e;
    }
}
