package com.riccardopoppi.catalogo_cap.repositories;

import com.riccardopoppi.catalogo_cap.domain.Cappello;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CappelloRepository extends JpaRepository<Cappello, UUID> {
    
    List<Cappello> findByNomeContainingIgnoreCase(String nome, Sort sort);

    // Nuovi metodi necessari per interrogare e rimuovere gli elementi via API REST tramite stringa codice
    Optional<Cappello> findByCodice(String codice);
    
    void deleteByCodice(String codice);
}