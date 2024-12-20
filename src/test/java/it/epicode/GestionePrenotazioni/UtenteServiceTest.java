package it.epicode.GestionePrenotazioni;



import it.epicode.GestionePrenotazioni.entities.Utente;
import it.epicode.GestionePrenotazioni.exceptions.ElementoNonTrovatoException;
import it.epicode.GestionePrenotazioni.repositories.UtenteRepository;
import it.epicode.GestionePrenotazioni.services.UtenteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UtenteServiceTest {

    @Mock
    private UtenteRepository utenteRepository;

    @InjectMocks
    private UtenteService utenteService;

    private Utente utente;

    @BeforeEach
    void setUp() {
        utente = new Utente(1L, "username", "Nome Completo", "email@test.com");
    }

    @Test
    void testSalvaUtente() {
        Mockito.when(utenteRepository.save(Mockito.any(Utente.class))).thenReturn(utente);

        Utente result = utenteService.salvaUtente(utente);
        assertNotNull(result);
        assertEquals("username", result.getUsername());
    }

    @Test
    void testTrovaPerIdSuccess() {
        Mockito.when(utenteRepository.findById(1L)).thenReturn(java.util.Optional.of(utente));

        Utente result = utenteService.trovaPerId(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testTrovaPerIdFailure() {
        Mockito.when(utenteRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        assertThrows(ElementoNonTrovatoException.class, () -> utenteService.trovaPerId(1L));
    }

    @Test
    void testEliminaPerIdSuccess() {
        Mockito.when(utenteRepository.existsById(1L)).thenReturn(true);
        Mockito.doNothing().when(utenteRepository).deleteById(1L);

        utenteService.eliminaPerId(1L);
        Mockito.verify(utenteRepository, Mockito.times(1)).deleteById(1L);
    }

    @Test
    void testEliminaPerIdFailure() {
        Mockito.when(utenteRepository.existsById(1L)).thenReturn(false);

        assertThrows(ElementoNonTrovatoException.class, () -> utenteService.eliminaPerId(1L));
    }
}
