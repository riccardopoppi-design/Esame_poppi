package com.riccardopoppi.catalogo_cap.controllers;

import com.riccardopoppi.catalogo_cap.domain.Cappello;
import com.riccardopoppi.catalogo_cap.repositories.CappelloRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
public class CappelloRestControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockitoBean
    private CappelloRepository cappelloRepository;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void getByCodice_CasoSuccesso_DeveRitornareStrutturaAPIResponse200() throws Exception {
        String codiceTest = "CAP-123";
        Cappello cappelloMock = new Cappello();
        cappelloMock.setCodice(codiceTest);
        cappelloMock.setNome("Snapback Classic");

        when(cappelloRepository.findByCodice(codiceTest)).thenReturn(Optional.of(cappelloMock));

        mockMvc.perform(get("/api/cappelli/item/" + codiceTest)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                // Verifico la struttura del JSON di successo richiesta dalle specifiche
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data.codice").value("CAP-123"))
                .andExpect(jsonPath("$.data.nome").value("Snapback Classic"));
    }

    @Test
    public void getByCodice_CasoRisorsaNonTrovata_DeveRitornareStato404() throws Exception {
        String codiceInesistente = "CAP-NOT-FOUND";

        when(cappelloRepository.findByCodice(codiceInesistente)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/cappelli/item/" + codiceInesistente)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                // Verifico la struttura del JSON di errore generata dall'handler centralizzato
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").exists()); 
    }
}