package com.riccardopoppi.catalogo_cap.services;

import com.riccardopoppi.catalogo_cap.domain.Cappello;
import com.riccardopoppi.catalogo_cap.repositories.CappelloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CappelloService {

    @Autowired
    private CappelloRepository cappelloRepository;

    private final Path root = Paths.get("uploads");

    public List<Cappello> findAll(Sort sort) {
        return cappelloRepository.findAll(sort);
    }

    public List<Cappello> findByNomeContainingIgnoreCase(String nome, Sort sort) {
        return cappelloRepository.findByNomeContainingIgnoreCase(nome, sort);
    }

    public Optional<Cappello> findById(UUID id) {
        return cappelloRepository.findById(id);
    }

    public Cappello save(Cappello cappello) {
        // Vincolo richiesto dalla Task 4: se stiamo salvando un NUOVO cappello,
        // azzeriamo l'ID per evitare sovrascritture maliziose dall'esterno.
        if (cappello.getId() != null) {
            // Se non esiste sul DB, significa che qualcuno sta forzando un ID inventato
            if (!cappelloRepository.existsById(cappello.getId())) {
                cappello.setId(null);
            }
        }
        return cappelloRepository.save(cappello);
    }

    public void deleteById(UUID id) {
        cappelloRepository.deleteById(id);
    }

    public void deleteAll() {
        cappelloRepository.deleteAll();
    }

    public String saveImage(MultipartFile file) throws Exception {
        if (!Files.exists(root)) {
            Files.createDirectory(root);
        }
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Files.copy(file.getInputStream(), this.root.resolve(fileName));
        return fileName;
    }
}