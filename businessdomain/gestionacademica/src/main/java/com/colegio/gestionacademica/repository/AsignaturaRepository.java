package com.colegio.gestionacademica.repository;

import com.colegio.gestionacademica.entities.Asignatura;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AsignaturaRepository extends JpaRepository<Asignatura, Long> {
    List<Asignatura> findByCursoId(Long cursoId);
}
