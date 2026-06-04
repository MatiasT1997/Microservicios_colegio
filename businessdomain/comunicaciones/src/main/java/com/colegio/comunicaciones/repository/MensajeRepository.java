package com.colegio.comunicaciones.repository;

import com.colegio.comunicaciones.entities.Mensaje;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MensajeRepository extends JpaRepository<Mensaje, Long> {
    List<Mensaje> findByDestinatarioId(Long destinatarioId);
}
