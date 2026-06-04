package com.colegio.asistencia.repository;

import com.colegio.asistencia.entities.RegistroAsistencia;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistroAsistenciaRepository extends JpaRepository<RegistroAsistencia, Long> {
    List<RegistroAsistencia> findByEstudianteId(Long estudianteId);
    List<RegistroAsistencia> findByCursoId(Long cursoId);
}
