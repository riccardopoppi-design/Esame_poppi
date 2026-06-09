package com.riccardopoppi.catalogo_cap.services;

import com.riccardopoppi.catalogo_cap.domain.Cappello;
import com.riccardopoppi.catalogo_cap.repositories.CappelloRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.nio.file.*;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CappelloService {

    @Autowired
    private CappelloRepository cappelloRepository;

    public List<Cappello> findAll() {
        return cappelloRepository.findAll();
    }

    public List<Cappello> findAll(Sort sort) {
        return cappelloRepository.findAll(sort);
    }

    public List<Cappello> findByNomeContainingIgnoreCase(String nome, Sort sort) {
        return cappelloRepository.findByNomeContainingIgnoreCase(nome, sort);
    }

    // Teniamo solo questo: salva e restituisce l'oggetto con l'ID generato
    public Cappello save(Cappello cappello) {
        return cappelloRepository.save(cappello);
    }

    public void deleteAll() {
        cappelloRepository.deleteAll();
    }

    public Optional<Cappello> findById(UUID id) {
        return cappelloRepository.findById(id);
    }

    public void deleteById(UUID id) {
    cappelloRepository.deleteById(id);
    }

    private final Path root = Paths.get("uploads"); // Cartella dove salverai le foto

    public String saveImage(MultipartFile file) throws Exception {
        // Crea la cartella se non esiste
        if (!Files.exists(root)) Files.createDirectory(root);
        
        // Genera un nome unico per evitare sovrascritture
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Files.copy(file.getInputStream(), this.root.resolve(fileName));
        
        return fileName;
    }
}