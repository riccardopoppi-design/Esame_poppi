package com.riccardopoppi.catalogo_cap.services;

import com.riccardopoppi.catalogo_cap.domain.Cappello;
import com.riccardopoppi.catalogo_cap.repositories.CappelloRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private final CappelloRepository repo;

    // Iniettiamo il repository nel costruttore
    public DataLoader(CappelloRepository repo) {
        this.repo = repo;
    }

    @Override
    public void run(String... args) throws Exception {
        
        // Controlliamo se il DB è già pieno per non duplicare i dati a ogni riavvio
        if (repo.count() == 0) {
            
            Cappello c1 = new Cappello(
                null, // L'ID UUID viene generato automaticamente
                "SNAP-001", 
                "9Fifty NY", 
                "New Era", 
                "M/L", 
                2023, 
                35.00, null
            );

            Cappello c2 = new Cappello(
                null, 
                "BEAN-002", 
                "Berretto Classico", 
                "Carhartt", 
                "Unica", 
                2024, 
                19.99, null
            );

            Cappello c3 = new Cappello(
                null, 
                "FED-003", 
                "Indiana Jones", 
                "Borsalino", 
                "L", 
                2022, 
                250.00, null
            );

            repo.saveAll(List.of(c1, c2, c3));
            System.out.println("--- Dati di prova inseriti nel database ---");
        }
    }
}