package it.epicode.GestionePrenotazioni;

import it.epicode.GestionePrenotazioni.configurations.PostazioneBean;
import it.epicode.GestionePrenotazioni.entities.Postazione;
import it.epicode.GestionePrenotazioni.entities.Edificio;
import it.epicode.GestionePrenotazioni.enums.TipoPostazione;
import it.epicode.GestionePrenotazioni.exceptions.ElementoNonTrovatoException;
import it.epicode.GestionePrenotazioni.repositories.PostazioneRepository;
import it.epicode.GestionePrenotazioni.services.EdificioService;
import it.epicode.GestionePrenotazioni.services.PostazioneService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostazioneServiceTest {

    @Mock
    private PostazioneRepository postazioneRepository;

    @Mock
    private EdificioService edificioService;

    @InjectMocks
    private PostazioneService postazioneService;

    private Postazione postazione;

    @BeforeEach
    void setUp() {
        Edificio edificio = new Edificio(1L, "Edificio Test", "Via Test", "Test City");
        postazione = new Postazione(1L, "P01", "Postazione Test", TipoPostazione.PRIVATO, 4, edificio);
    }


    @Test
    void testSalvaPostazione() {
        // Crea il PostazioneBean
        PostazioneBean postazioneBean = new PostazioneBean();
        postazioneBean.setCodice("ABC123");
        postazioneBean.setDescrizione("Postazione Test");
        postazioneBean.setTipo(TipoPostazione.OPEN_SPACE); // Assicurati di scegliere un tipo valido
        postazioneBean.setOccupanti(5);

        // Crea un edificio fittizio per associarlo al PostazioneBean
        Edificio edificio = new Edificio();
        edificio.setNome("Edificio Test");
        edificio.setIndirizzo("Via Test");
        edificio.setCitta("Test City");
        postazioneBean.setEdificio(edificio);

        // Mock del repository
        Mockito.when(postazioneRepository.save(Mockito.any(Postazione.class))).thenReturn(new Postazione());

        // Chiamata al servizio con il PostazioneBean
        Postazione result = postazioneService.salvaPostazione(postazioneBean);

        // Verifica che il risultato non sia null
        assertNotNull(result);

        // Verifica che la descrizione della postazione sia quella che abbiamo impostato nel bean
        assertEquals("Postazione Test", result.getDescrizione());
    }


    @Test
    void testTrovaPostazionePerIdSuccess() {
        Mockito.when(postazioneRepository.findById(1L)).thenReturn(java.util.Optional.of(postazione));

        Postazione result = postazioneService.trovaPostazionePerId(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testTrovaPostazionePerIdFailure() {
        Mockito.when(postazioneRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        assertThrows(ElementoNonTrovatoException.class, () -> postazioneService.trovaPostazionePerId(1L));
    }

    @Test
    void testEliminaPostazioneSuccess() {
        Mockito.when(postazioneRepository.findById(1L)).thenReturn(java.util.Optional.of(postazione));
        Mockito.doNothing().when(postazioneRepository).delete(postazione);

        postazioneService.eliminaPostazione(1L);
        Mockito.verify(postazioneRepository, Mockito.times(1)).delete(postazione);
    }

    @Test
    void testEliminaPostazioneFailure() {
        Mockito.when(postazioneRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        assertThrows(ElementoNonTrovatoException.class, () -> postazioneService.eliminaPostazione(1L));
    }
}
