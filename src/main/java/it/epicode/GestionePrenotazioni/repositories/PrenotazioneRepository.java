package it.epicode.GestionePrenotazioni.repositories;

import it.epicode.GestionePrenotazioni.entities.Prenotazione;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PrenotazioneRepository extends JpaRepository<Prenotazione, Long> {

    // Trova tutte le prenotazioni per un utente dato l'utenteId
    List<Prenotazione> findByUtente_Id(Long utenteId);

    // Trova tutte le prenotazioni per una postazione dato il postazioneId e la data
    List<Prenotazione> findByPostazione_IdAndDataPrenotazione(Long postazioneId, LocalDate dataPrenotazione);

    // Trova tutte le prenotazioni per un utente dato l'utenteId e la data di prenotazione
    List<Prenotazione> findByUtente_IdAndDataPrenotazione(Long utenteId, LocalDate dataPrenotazione);


}
