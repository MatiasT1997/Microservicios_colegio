package com.colegio.gestionacademica.repository;

import com.colegio.gestionacademica.entities.Nota;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotaRepository extends JpaRepository<Nota, Long> {
    List<Nota> findByEvaluacionId(Long evaluacionId);
    List<Nota> findByEstudianteId(Long estudianteId);
}
