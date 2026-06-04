package com.colegio.gestionacademica.factory;

import com.colegio.gestionacademica.entities.Evaluacion;

/** Creador concreto: produce evaluaciones de tipo CONTROL. */
public class ControlFactory extends EvaluacionFactory {
    @Override
    protected Evaluacion crearEvaluacion() {
        Evaluacion e = new Evaluacion();
        e.setTipo(Evaluacion.TipoEvaluacion.CONTROL);
        return e;
    }
}
