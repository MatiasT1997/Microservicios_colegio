package cl.duoc.colegio.comunicaciones.repository;

import cl.duoc.colegio.comunicaciones.model.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MensajeRepository extends JpaRepository<Mensaje, Long> {
    List<Mensaje> findByEstudianteId(Long estudianteId);
}
