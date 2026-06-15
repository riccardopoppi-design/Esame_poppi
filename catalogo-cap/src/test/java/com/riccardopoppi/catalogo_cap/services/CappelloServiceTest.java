package com.riccardopoppi.catalogo_cap.services;

import com.riccardopoppi.catalogo_cap.domain.Cappello;
import com.riccardopoppi.catalogo_cap.repositories.CappelloRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.UUID;

// Usiamo l'estensione Mockito per un test d'unità puro e velocissimo (senza caricare Spring)
@ExtendWith(MockitoExtension.class)
public class CappelloServiceTest {

    @Mock
    private CappelloRepository cappelloRepository;

    @InjectMocks
    private CappelloService cappelloService;

    @Test
    public void save_DeveAzzuerareIdEInvocareRepository_QuandoOggettoValido() {
        // 1. PREPARAZIONE (Arrange)
        Cappello inputCappello = new Cappello();
        inputCappello.setId(UUID.randomUUID()); // Impostiamo un ID fittizio per verificare che venga azzerato
        inputCappello.setCodice("CAP-NEW");
        inputCappello.setNome("Berretto Invernale");

        Cappello savedCappelloMock = new Cappello();
        savedCappelloMock.setId(UUID.randomUUID()); // Simula l'ID generato dal database dopo il salvataggio
        savedCappelloMock.setCodice("CAP-NEW");
        savedCappelloMock.setNome("Berretto Invernale");

        // Configura il mock per rispondere quando viene chiamato il save
        when(cappelloRepository.save(argThat(c -> c.getId() == null))).thenReturn(savedCappelloMock);

        // 2. ESECUZIONE (Act)
        Cappello risultato = cappelloService.save(inputCappello);

        // 3. VERIFICA DEI VINCOLI (Assert & Verify)
        assertNotNull(risultato);
        
        // VINCOLO SODDISFATTO: Verifica che la repository sia stata invocata con l'ID azzerato (null)
        verify(cappelloRepository).save(argThat(cappello -> {
            return cappello.getId() == null && "CAP-NEW".equals(cappello.getCodice());
        }));
    }
}