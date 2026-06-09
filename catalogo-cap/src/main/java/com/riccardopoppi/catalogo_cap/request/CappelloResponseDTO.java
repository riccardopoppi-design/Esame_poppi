package com.riccardopoppi.catalogo_cap.request;

import java.util.UUID;
import lombok.Data;

@Data
public class CappelloResponseDTO {
    private UUID id;
    private String codice;
    private String nome;
    private String marca;
    private String taglia;
    private int anno;
    private double prezzo;
    private String immagine;
}