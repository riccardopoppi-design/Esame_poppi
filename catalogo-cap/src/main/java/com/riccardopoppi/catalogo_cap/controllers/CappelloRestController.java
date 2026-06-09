package com.riccardopoppi.catalogo_cap.controllers;

import com.riccardopoppi.catalogo_cap.request.CappelloRequestDTO;
import com.riccardopoppi.catalogo_cap.request.CappelloResponseDTO;
import com.riccardopoppi.catalogo_cap.dto.response.APIResponse;
import com.riccardopoppi.catalogo_cap.domain.Cappello;
import com.riccardopoppi.catalogo_cap.repositories.CappelloRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cappelli")
public class CappelloRestController {

    private final CappelloRepository cappelloRepository;

    public CappelloRestController(CappelloRepository cappelloRepository) {
        this.cappelloRepository = cappelloRepository;
    }

    @GetMapping
    public APIResponse<List<CappelloResponseDTO>> getAll() {
        List<CappelloResponseDTO> list = cappelloRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
        return APIResponse.success(list);
    }

    @GetMapping("/item/{codice}")
    public APIResponse<CappelloResponseDTO> getByCodice(@PathVariable String codice) {
        Cappello cappello = cappelloRepository.findByCodice(codice)
                .orElseThrow(() -> new NoSuchElementException("Cappello non trovato con codice: " + codice));
        return APIResponse.success(convertToResponseDTO(cappello));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public APIResponse<CappelloResponseDTO> create(@Valid @RequestBody CappelloRequestDTO requestDTO) {
        // Vincolo validazione: se i dati non sono validi, l'esecuzione non entra nemmeno nel metodo
        // grazie a @Valid, e viene gestita direttamente dall'ExceptionHandler in automatico.
        Cappello cappello = new Cappello();
        BeanUtils.copyProperties(requestDTO, cappello);
        
        Cappello saved = cappelloRepository.save(cappello);
        return APIResponse.success(convertToResponseDTO(saved));
    }

    @DeleteMapping("/item/{codice}")
    @Transactional
    public APIResponse<String> deleteByCodice(@PathVariable String codice) {
        cappelloRepository.findByCodice(codice)
                .orElseThrow(() -> new NoSuchElementException("Impossibile eliminare: codice " + codice + " inesistente."));
        
        cappelloRepository.deleteByCodice(codice);
        return APIResponse.success("Elemento rimosso correttamente dal catalogo.");
    }

    private CappelloResponseDTO convertToResponseDTO(Cappello cappello) {
        CappelloResponseDTO dto = new CappelloResponseDTO();
        BeanUtils.copyProperties(cappello, dto);
        return dto;
    }
}