package it.epicode.GestionePrenotazioni.services;

import it.epicode.GestionePrenotazioni.entities.Prenotazione;
import it.epicode.GestionePrenotazioni.entities.Postazione;
import it.epicode.GestionePrenotazioni.entities.Utente;
import it.epicode.GestionePrenotazioni.enums.TipoPostazione;
import it.epicode.GestionePrenotazioni.exceptions.ElementoNonTrovatoException;
import it.epicode.GestionePrenotazioni.exceptions.PrenotazioneNonValidaException;
import it.epicode.GestionePrenotazioni.repositories.PrenotazioneRepository;
import it.epicode.GestionePrenotazioni.repositories.PostazioneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PrenotazioneService {

    @Autowired
    private PrenotazioneRepository prenotazioneRepository;

    @Autowired
    private UtenteService utenteService;  // Aggiungi questa riga per iniettare UtenteService

    // Metodo per creare una prenotazione
    public Prenotazione creaPrenotazione(Prenotazione prenotazione) {
        // Verifica se l'utente ha già una prenotazione per quella data
        List<Prenotazione> prenotazioniUtente = prenotazioneRepository.findByUtente_IdAndDataPrenotazione(
                prenotazione.getUtente().getId(),
                prenotazione.getDataPrenotazione()
        );

        if (!prenotazioniUtente.isEmpty()) {
            throw new PrenotazioneNonValidaException("L'utente ha già una prenotazione per questa data.");
        }

        // Verifica se la postazione è già prenotata per quella data
        List<Prenotazione> prenotazioniPostazione = prenotazioneRepository.findByPostazione_IdAndDataPrenotazione(
                prenotazione.getPostazione().getId(),
                prenotazione.getDataPrenotazione()
        );

        if (!prenotazioniPostazione.isEmpty()) {
            throw new PrenotazioneNonValidaException("La postazione è già prenotata per questa data.");
        }

        // Salva la prenotazione
        return prenotazioneRepository.save(prenotazione);
    }

    // Metodo per trovare le prenotazioni per un utente
    public List<Prenotazione> trovaPrenotazioniPerUtente(Long utenteId) {
        return prenotazioneRepository.findByUtente_Id(utenteId);
    }
    // Metodo per trovare prenotazioni di un utente per una specifica data
    public List<Prenotazione> trovaPrenotazioniPerUtentePerData(Long utenteId, LocalDate dataPrenotazione) {
        return prenotazioneRepository.findByUtente_IdAndDataPrenotazione(utenteId, dataPrenotazione);
    }
    // Metodo per trovare prenotazioni per postazione e data
    public List<Prenotazione> trovaPrenotazioniPerPostazionePerData(Long postazioneId, LocalDate dataPrenotazione) {
        // Questo metodo interroga il repository per ottenere tutte le prenotazioni per una determinata postazione e data
        return prenotazioneRepository.findByPostazione_IdAndDataPrenotazione(postazioneId, dataPrenotazione);
    }

    // Metodo per trovare l'utente per username (usa UtenteService)
    public Utente trovaUtentePerUsername(String username) {
        return utenteService.trovaPerUsername(username);  // Usa il metodo di UtenteService
    }

    // Metodo per eliminare una prenotazione
    public void eliminaPrenotazione(Long id) {
        Prenotazione prenotazione = prenotazioneRepository.findById(id)
                .orElseThrow(() -> new ElementoNonTrovatoException("Prenotazione non trovata con ID: " + id));
        prenotazioneRepository.delete(prenotazione);
    }
}

