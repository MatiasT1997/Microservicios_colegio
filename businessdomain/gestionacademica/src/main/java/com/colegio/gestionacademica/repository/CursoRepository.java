package com.colegio.gestionacademica.repository;

import com.colegio.gestionacademica.entities.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** Patron Repository: acceso a datos de Curso vía Spring Data JPA. */
@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {
}
