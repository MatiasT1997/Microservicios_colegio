package com.colegio.gestionacademica.factory;

import com.colegio.gestionacademica.entities.Evaluacion.TipoEvaluacion;
import java.util.EnumMap;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * Punto de acceso al patron Factory Method.
 *
 * Mantiene un registro de creadores concretos indexados por {@link TipoEvaluacion}
 * y entrega la fabrica adecuada segun el tipo solicitado. De este modo el codigo
 * cliente solo conoce el enum y esta interfaz, nunca las clases concretas.
 */
@Component
public class EvaluacionFactoryProvider {

    private final Map<TipoEvaluacion, EvaluacionFactory> factories = new EnumMap<>(TipoEvaluacion.class);

    public EvaluacionFactoryProvider() {
        factories.put(TipoEvaluacion.PRUEBA, new PruebaFactory());
        factories.put(TipoEvaluacion.TAREA, new TareaFactory());
        factories.put(TipoEvaluacion.EXAMEN, new ExamenFactory());
        factories.put(TipoEvaluacion.CONTROL, new ControlFactory());
    }

    public EvaluacionFactory getFactory(TipoEvaluacion tipo) {
        EvaluacionFactory factory = factories.get(tipo);
        if (factory == null) {
            throw new IllegalArgumentException("Tipo de evaluacion no soportado: " + tipo);
        }
        return factory;
    }
}
