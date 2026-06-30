package cl.duoc.colegio.asistencia.repository;

import cl.duoc.colegio.asistencia.model.RegistroAsistencia;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RegistroAsistenciaRepository extends JpaRepository<RegistroAsistencia, Long> {
    List<RegistroAsistencia> findByCursoId(Long cursoId);
    List<RegistroAsistencia> findByEstudianteId(Long estudianteId);
}