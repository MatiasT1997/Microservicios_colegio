package cl.duoc.colegio.gestionacademica.repository;

import cl.duoc.colegio.gestionacademica.model.Asignatura;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AsignaturaRepository extends JpaRepository<Asignatura, Long> {
    List<Asignatura> findByCursoId(Long cursoId);
}