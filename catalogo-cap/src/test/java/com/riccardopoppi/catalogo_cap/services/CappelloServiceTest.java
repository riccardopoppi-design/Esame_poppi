package com.riccardopoppi.catalogo_cap.services;

import com.riccardopoppi.catalogo_cap.domain.Cappello;
import com.riccardopoppi.catalogo_cap.repositories.CappelloRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CappelloServiceTest {

    @Mock
    private CappelloRepository cappelloRepository;

    @InjectMocks
    private CappelloService cappelloService;

    @Test
    public void salvaCappello_DeveGarantireAzzeramentoID_PerPrevenireScrittureMaliziose() {
        // GIVEN: Il client tenta di forzare un ID esistente
        Cappello cappelloInviato = new Cappello();
        UUID idForzato = UUID.randomUUID();
        cappelloInviato.setId(idForzato); 
        cappelloInviato.setNome("Cap Test");

        // Prepariamo l'oggetto che simula l'output del DB (con un NUOVO id generato dal sistema)
        Cappello cappelloSalvatoNelDb = new Cappello();
        UUID idNuovo = UUID.randomUUID();
        cappelloSalvatoNelDb.setId(idNuovo);
        cappelloSalvatoNelDb.setNome("Cap Test");

        // Stubbiato in modo generico per evitare conflitti di mutazione dell'oggetto durante l'esecuzione
        when(cappelloRepository.save(any(Cappello.class))).thenReturn(cappelloSalvatoNelDb);

        // WHEN: Eseguiamo la logica di business
        Cappello risultato = cappelloService.save(cappelloInviato);

        // THEN: Verifiche sul risultato
        assertNotNull(risultato);
        assertEquals(idNuovo, risultato.getId());

        // VERIFY & ARGUMENT MATCHER: Dimostriamo al QA che il Service ha azzerato l'id PRIMA di toccare il DB
        verify(cappelloRepository, times(1)).save(argThat(cappello -> cappello.getId() == null));
    }
}