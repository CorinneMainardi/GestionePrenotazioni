package it.epicode.GestionePrenotazioni.services;

import it.epicode.GestionePrenotazioni.entities.Edificio;
import it.epicode.GestionePrenotazioni.exceptions.ElementoNonTrovatoException;
import it.epicode.GestionePrenotazioni.repositories.EdificioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EdificioService {
    @Autowired
    private EdificioRepository edificioRepository;


    public Edificio salvaEdificio(Edificio edificio) {
        return edificioRepository.save(edificio);
    }


    public Edificio trovaPerId(Long id) {
        return edificioRepository.findById(id)
                .orElseThrow(() -> new ElementoNonTrovatoException("Edificio non trovato con ID: " + id));
    }


    public List<Edificio> trovaTutti() {
        return edificioRepository.findAll();
    }


    public void eliminaPerId(Long id) {
        if (!edificioRepository.existsById(id)) {
            throw new ElementoNonTrovatoException("Edificio non trovato con ID: " + id);
        }
        edificioRepository.deleteById(id);
    }
}
