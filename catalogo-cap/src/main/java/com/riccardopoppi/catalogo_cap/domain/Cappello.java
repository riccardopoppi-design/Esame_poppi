package com.riccardopoppi.catalogo_cap.domain;

import java.util.UUID;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cappelli")
public class Cappello {
    
    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Column(name = "codice")
    private String codice;

    @Column(name = "nome")
    private String nome;

    @Column(name = "marca")
    private String marca;

    @Column(name = "taglia")
    private String taglia;

    @Column(name = "anno")
    private int anno;

    @Column(name = "prezzo")
    private double prezzo;

    @Column(name = "immagine")
    private String immagine;
}