package com.colegio.gestionacademica.service;

import com.colegio.gestionacademica.common.BusinessRuleException;
import com.colegio.gestionacademica.entities.Asignatura;
import com.colegio.gestionacademica.entities.Evaluacion;
import com.colegio.gestionacademica.entities.Evaluacion.TipoEvaluacion;
import com.colegio.gestionacademica.factory.EvaluacionFactory;
import com.colegio.gestionacademica.factory.EvaluacionFactoryProvider;
import com.colegio.gestionacademica.repository.AsignaturaRepository;
import com.colegio.gestionacademica.repository.EvaluacionRepository;
import java.time.LocalDate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * Logica de negocio de evaluaciones. Demuestra el uso del patron Factory Method:
 * la creacion de la Evaluacion se delega al provider de fabricas segun el tipo.
 */
@Service
public class EvaluacionService {

    private final EvaluacionRepository evaluacionRepository;
    private final AsignaturaRepository asignaturaRepository;
    private final EvaluacionFactoryProvider factoryProvider;

    public EvaluacionService(EvaluacionRepository evaluacionRepository,
                             AsignaturaRepository asignaturaRepository,
                             EvaluacionFactoryProvider factoryProvider) {
        this.evaluacionRepository = evaluacionRepository;
        this.asignaturaRepository = asignaturaRepository;
        this.factoryProvider = factoryProvider;
    }

    /**
     * Crea y persiste una evaluacion usando el patron Factory Method.
     */
    public Evaluacion crearEvaluacion(Long asignaturaId, TipoEvaluacion tipo,
                                      String titulo, Double ponderacion, LocalDate fecha) {
        Asignatura asignatura = asignaturaRepository.findById(asignaturaId)
                .orElseThrow(() -> new BusinessRuleException(
                        "GA-404", HttpStatus.NOT_FOUND,
                        "No existe la asignatura con id " + asignaturaId));

        if (ponderacion != null && (ponderacion < 0 || ponderacion > 100)) {
            throw new BusinessRuleException(
                    "GA-422", HttpStatus.UNPROCESSABLE_ENTITY,
                    "La ponderacion debe estar entre 0 y 100");
        }

        // ---- Uso del patron Factory Method ----
        EvaluacionFactory factory = factoryProvider.getFactory(tipo);
        Evaluacion evaluacion = factory.construir(titulo, ponderacion, fecha);
        evaluacion.setAsignatura(asignatura);

        return evaluacionRepository.save(evaluacion);
    }
}
