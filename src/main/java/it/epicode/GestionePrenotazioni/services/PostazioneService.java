package it.epicode.GestionePrenotazioni.services;

import it.epicode.GestionePrenotazioni.configurations.PostazioneBean;
import it.epicode.GestionePrenotazioni.entities.Edificio;
import it.epicode.GestionePrenotazioni.entities.Postazione;
import it.epicode.GestionePrenotazioni.enums.TipoPostazione;
import it.epicode.GestionePrenotazioni.exceptions.ElementoNonTrovatoException;
import it.epicode.GestionePrenotazioni.repositories.EdificioRepository;
import it.epicode.GestionePrenotazioni.repositories.PostazioneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostazioneService {

    @Autowired
    private PostazioneRepository postazioneRepository;

    @Autowired
    private EdificioRepository edificioRepository;

    @Transactional
    public Postazione salvaPostazione(PostazioneBean postazioneBean) {
        // Costruisci la postazione tramite il bean
        Postazione postazione = postazioneBean.buildPostazione();

        // Verifica se l'Edificio associato alla Postazione è persistente
        if (postazione.getEdificio() != null && postazione.getEdificio().getId() != null) {
            // Verifica se l'Edificio esiste già nel database
            Edificio edificioPersistente = edificioRepository.findById(postazione.getEdificio().getId())
                    .orElseThrow(() -> new ElementoNonTrovatoException("Edificio non trovato con ID: " + postazione.getEdificio().getId()));

            // Imposta l'Edificio persistente nella Postazione
            postazione.setEdificio(edificioPersistente);
        } else {
            // Se l'Edificio è nuovo, lo salviamo prima
            Edificio edificioSalvato = edificioRepository.save(postazione.getEdificio());
            postazione.setEdificio(edificioSalvato);
        }

        // Ora puoi salvare la Postazione con l'Edificio persistente
        return postazioneRepository.save(postazione);
    }

    public List<Postazione> trovaTutteLePostazioni() {
        return postazioneRepository.findAll();
    }

    public Postazione trovaPostazionePerId(Long id) throws ElementoNonTrovatoException {
        return postazioneRepository.findById(id)
                .orElseThrow(() -> new ElementoNonTrovatoException("Postazione con ID " + id + " non trovata."));
    }

    public List<Postazione> trovaPostazioniPerTipoECitta(TipoPostazione tipo, String citta) throws ElementoNonTrovatoException {
        List<Postazione> postazioni = postazioneRepository.findByTipoAndEdificio_Citta(tipo, citta);
        if (postazioni == null || postazioni.isEmpty()) {
            throw new ElementoNonTrovatoException("Non ci sono postazioni del tipo " + tipo + " nella città " + citta + ".");
        }
        return postazioni;
    }

    public List<Postazione> trovaTutti() {
        return postazioneRepository.findAll();
    }

    public void eliminaPostazione(Long id) throws ElementoNonTrovatoException {
        Postazione postazione = trovaPostazionePerId(id); // Verifica l'esistenza
        postazioneRepository.delete(postazione);
    }
}
