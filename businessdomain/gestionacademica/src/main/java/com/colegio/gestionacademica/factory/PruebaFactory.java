package com.colegio.gestionacademica.factory;

import com.colegio.gestionacademica.entities.Evaluacion;

/** Creador concreto: produce evaluaciones de tipo PRUEBA. */
public class PruebaFactory extends EvaluacionFactory {
    @Override
    protected Evaluacion crearEvaluacion() {
        Evaluacion e = new Evaluacion();
        e.setTipo(Evaluacion.TipoEvaluacion.PRUEBA);
        return e;
    }
}
