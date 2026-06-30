package cl.duoc.colegio.gestionacademica.factory;

import cl.duoc.colegio.gestionacademica.model.TipoEvaluacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Component
public class EvaluacionFactoryProvider {

    private final Map<TipoEvaluacion, EvaluacionFactory> factories = new HashMap<>();

    @Autowired
    public EvaluacionFactoryProvider(PruebaFactory pruebaFactory,
                                      TareaFactory tareaFactory,
                                      ProyectoFactory proyectoFactory,
                                      ExamenFactory examenFactory) {
        factories.put(TipoEvaluacion.PRUEBA, pruebaFactory);
        factories.put(TipoEvaluacion.TAREA, tareaFactory);
        factories.put(TipoEvaluacion.PROYECTO, proyectoFactory);
        factories.put(TipoEvaluacion.EXAMEN, examenFactory);
    }

    public EvaluacionFactory getFactory(TipoEvaluacion tipo) {
        return factories.get(tipo);
    }
}