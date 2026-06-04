package com.colegio.gestionacademica.repository;

import com.colegio.gestionacademica.entities.Evaluacion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EvaluacionRepository extends JpaRepository<Evaluacion, Long> {
    List<Evaluacion> findByAsignaturaId(Long asignaturaId);
}
