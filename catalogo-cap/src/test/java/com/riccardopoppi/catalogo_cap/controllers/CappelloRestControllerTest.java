package com.riccardopoppi.catalogo_cap.controllers;

import com.riccardopoppi.catalogo_cap.domain.Cappello;
import com.riccardopoppi.catalogo_cap.repositories.CappelloRepository;
import org.junit.jupiter.api.Test;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

// VINCOLO SODDISFATTO: Usiamo @WebMvcTest isolando lo strato Web (niente database reale)
@WebMvcTest(CappelloRestController.class)
public class CappelloRestControllerTest {

    @Autowired
    private MockMvc mockMvc; // Iniettato automaticamente per simulare le chiamate HTTP

    @MockitoBean
    private CappelloRepository cappelloRepository; // Mock dello strato dati richiesto da Spring Boot 3.4+

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
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").exists());
    }
}