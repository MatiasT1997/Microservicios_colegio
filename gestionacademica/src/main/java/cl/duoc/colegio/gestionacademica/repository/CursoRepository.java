package cl.duoc.colegio.gestionacademica.repository;

import cl.duoc.colegio.gestionacademica.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Curso, Long> {
}