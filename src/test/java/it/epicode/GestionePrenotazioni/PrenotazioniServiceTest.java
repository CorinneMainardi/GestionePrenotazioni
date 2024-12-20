package it.epicode.GestionePrenotazioni;

import it.epicode.GestionePrenotazioni.entities.Prenotazione;
import it.epicode.GestionePrenotazioni.entities.Utente;
import it.epicode.GestionePrenotazioni.entities.Postazione;
import it.epicode.GestionePrenotazioni.enums.TipoPostazione;
import it.epicode.GestionePrenotazioni.exceptions.ElementoNonTrovatoException;
import it.epicode.GestionePrenotazioni.exceptions.PrenotazioneNonValidaException;
import it.epicode.GestionePrenotazioni.repositories.PrenotazioneRepository;
import it.epicode.GestionePrenotazioni.services.PrenotazioneService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PrenotazioneServiceTest {

    @Mock
    private PrenotazioneRepository prenotazioneRepository;

    @InjectMocks
    private PrenotazioneService prenotazioneService;

    private Prenotazione prenotazione;
    private Utente utente;
    private Postazione postazione;

    @BeforeEach
    void setUp() {
        // Crea un oggetto Utente e Postazione validi
        utente = new Utente(1L, "username", "Nome Completo", "email@test.com");
        postazione = new Postazione(1L, "P01", "Postazione Test", TipoPostazione.PRIVATO, 4, null);

        // Crea una Prenotazione utilizzando Utente e Postazione validi
        prenotazione = new Prenotazione(1L, utente, postazione, LocalDate.now());
    }

    @Test
    void testCreaPrenotazioneSuccess() {
        Mockito.when(prenotazioneRepository.save(Mockito.any(Prenotazione.class))).thenReturn(prenotazione);

        Prenotazione result = prenotazioneService.creaPrenotazione(prenotazione);
        assertNotNull(result);
        assertEquals(LocalDate.now(), result.getDataPrenotazione());
    }

    @Test
    void testCreaPrenotazioneFailure() {
        Mockito.when(prenotazioneRepository.findByUtente_IdAndDataPrenotazione(Mockito.anyLong(), Mockito.any(LocalDate.class)))
                .thenReturn(Arrays.asList(prenotazione));

        assertThrows(PrenotazioneNonValidaException.class, () -> prenotazioneService.creaPrenotazione(prenotazione));
    }

    @Test
    void testEliminaPrenotazione() {
        Mockito.when(prenotazioneRepository.findById(1L)).thenReturn(java.util.Optional.of(prenotazione));
        Mockito.doNothing().when(prenotazioneRepository).delete(prenotazione);

        prenotazioneService.eliminaPrenotazione(1L);
        Mockito.verify(prenotazioneRepository, Mockito.times(1)).delete(prenotazione);
    }

    @Test
    void testEliminaPrenotazioneFailure() {
        Mockito.when(prenotazioneRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        assertThrows(ElementoNonTrovatoException.class, () -> prenotazioneService.eliminaPrenotazione(1L));
    }
}
