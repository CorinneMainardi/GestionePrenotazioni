package it.epicode.GestionePrenotazioni;

import it.epicode.GestionePrenotazioni.entities.Edificio;
import it.epicode.GestionePrenotazioni.exceptions.ElementoNonTrovatoException;
import it.epicode.GestionePrenotazioni.repositories.EdificioRepository;
import it.epicode.GestionePrenotazioni.services.EdificioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EdificioServiceTest {

    @Mock
    private EdificioRepository edificioRepository;

    @InjectMocks
    private EdificioService edificioService;

    private Edificio edificio;

    @BeforeEach
    void setUp() {
        edificio = new Edificio(1L, "Edificio Test", "Via Test", "Test City");
    }

    @Test
    void testSalvaEdificio() {
        Mockito.when(edificioRepository.save(Mockito.any(Edificio.class))).thenReturn(edificio);

        Edificio result = edificioService.salvaEdificio(edificio);
        assertNotNull(result);
        assertEquals("Edificio Test", result.getNome());
    }

    @Test
    void testTrovaPerIdSuccess() {
        Mockito.when(edificioRepository.findById(1L)).thenReturn(java.util.Optional.of(edificio));

        Edificio result = edificioService.trovaPerId(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testTrovaPerIdFailure() {
        Mockito.when(edificioRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        assertThrows(ElementoNonTrovatoException.class, () -> edificioService.trovaPerId(1L));
    }

    @Test
    void testEliminaPerIdSuccess() {
        Mockito.when(edificioRepository.existsById(1L)).thenReturn(true);
        Mockito.doNothing().when(edificioRepository).deleteById(1L);

        edificioService.eliminaPerId(1L);
        Mockito.verify(edificioRepository, Mockito.times(1)).deleteById(1L);
    }

    @Test
    void testEliminaPerIdFailure() {
        Mockito.when(edificioRepository.existsById(1L)).thenReturn(false);

        assertThrows(ElementoNonTrovatoException.class, () -> edificioService.eliminaPerId(1L));
    }
}
