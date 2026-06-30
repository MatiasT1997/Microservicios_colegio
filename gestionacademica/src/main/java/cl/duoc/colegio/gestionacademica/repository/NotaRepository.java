package cl.duoc.colegio.gestionacademica.repository;

import cl.duoc.colegio.gestionacademica.model.Nota;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotaRepository extends JpaRepository<Nota, Long> {
    List<Nota> findByEstudianteId(Long estudianteId);
}