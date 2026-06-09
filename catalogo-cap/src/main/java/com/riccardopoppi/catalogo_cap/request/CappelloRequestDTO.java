package com.riccardopoppi.catalogo_cap.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CappelloRequestDTO {

    @NotBlank(message = "Il codice è obbligatorio")
    private String codice;

    @NotBlank(message = "Il nome è obbligatorio")
    private String nome;

    @NotBlank(message = "La marca è obbligatoria")
    private String marca;

    @NotBlank(message = "La taglia è obbligatoria")
    private String taglia;

    @NotNull(message = "L'anno è obbligatorio")
    @Min(value = 1900, message = "L'anno non può essere inferiore al 1900")
    @Max(value = 2026, message = "L'anno non può essere nel futuro")
    private Integer anno;

    @NotNull(message = "Il prezzo è obbligatorio")
    @Min(value = 0, message = "Il prezzo non può essere inferiore a 0")
    private Double prezzo;
}