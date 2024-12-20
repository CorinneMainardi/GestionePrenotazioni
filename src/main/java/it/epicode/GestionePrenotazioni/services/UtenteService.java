package it.epicode.GestionePrenotazioni.services;

import it.epicode.GestionePrenotazioni.entities.Utente;
import it.epicode.GestionePrenotazioni.exceptions.ElementoNonTrovatoException;
import it.epicode.GestionePrenotazioni.repositories.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UtenteService {
    @Autowired
    private UtenteRepository utenteRepository;

    // Salva un utente
    public Utente salvaUtente(Utente utente) {
        return utenteRepository.save(utente);
    }

    // Trova tutti gli utenti
    public List<Utente> trovaTutti() {
        return utenteRepository.findAll();
    }

    // Trova un utente per ID
    public Utente trovaPerId(Long id) {
        return utenteRepository.findById(id)
                .orElseThrow(() -> new ElementoNonTrovatoException("Utente con ID " + id + " non trovato."));
    }

    // Trova un utente per username
    public Utente trovaPerUsername(String username) {
        return utenteRepository.findByUsername(username)
                .orElseThrow(() -> new ElementoNonTrovatoException("Utente non trovato con username: " + username));
    }

    // Elimina un utente per ID
    public void eliminaPerId(Long id) {
        if (!utenteRepository.existsById(id)) {
            throw new ElementoNonTrovatoException("Utente con ID " + id + " non trovato.");
        }
        utenteRepository.deleteById(id);
    }
}

