package com.colegio.asistencia.repository;

import com.colegio.asistencia.entities.Anotacion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnotacionRepository extends JpaRepository<Anotacion, Long> {
    List<Anotacion> findByEstudianteId(Long estudianteId);
}
