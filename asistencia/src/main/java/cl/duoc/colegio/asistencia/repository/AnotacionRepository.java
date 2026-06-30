package cl.duoc.colegio.asistencia.repository;

import cl.duoc.colegio.asistencia.model.Anotacion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AnotacionRepository extends JpaRepository<Anotacion, Long> {
    List<Anotacion> findByEstudianteId(Long estudianteId);
}