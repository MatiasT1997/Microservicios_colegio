package cl.duoc.colegio.gestionacademica.repository;

import cl.duoc.colegio.gestionacademica.model.Evaluacion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EvaluacionRepository extends JpaRepository<Evaluacion, Long> {
    List<Evaluacion> findByAsignaturaId(Long asignaturaId);
}