package it.epicode.GestionePrenotazioni.runners;

import com.github.javafaker.Faker;
import it.epicode.GestionePrenotazioni.configurations.PostazioneBean;
import it.epicode.GestionePrenotazioni.entities.Edificio;
import it.epicode.GestionePrenotazioni.entities.Postazione;
import it.epicode.GestionePrenotazioni.entities.Prenotazione;
import it.epicode.GestionePrenotazioni.entities.Utente;
import it.epicode.GestionePrenotazioni.enums.TipoPostazione;
import it.epicode.GestionePrenotazioni.services.EdificioService;
import it.epicode.GestionePrenotazioni.services.PostazioneService;
import it.epicode.GestionePrenotazioni.services.PrenotazioneService;
import it.epicode.GestionePrenotazioni.services.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Component
public class DataLoader {

    @Autowired
    private EdificioService edificioService;
    @Autowired
    private PostazioneService postazioneService;
    @Autowired
    private UtenteService utenteService;
    @Autowired
    private PrenotazioneService prenotazioneService;
    @Autowired
    private Faker faker;


    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("Inizio popolamento del database...");

        // Creazione edifici
        for (int i = 0; i < 5; i++) {
            System.out.println("Creando edificio " + i);
            Edificio edificio = new Edificio();
            edificio.setNome(faker.company().name());
            edificio.setIndirizzo(faker.address().streetAddress());
            edificio.setCitta(faker.address().city());
            edificioService.salvaEdificio(edificio);

            // Creazione postazioni
            for (int j = 0; j < 3; j++) {
                System.out.println("Creando postazione " + j);
                TipoPostazione tipo = TipoPostazione.values()[new Random().nextInt(TipoPostazione.values().length)];

                // Crea il PostazioneBean
                PostazioneBean postazioneBean = new PostazioneBean();
                postazioneBean.setCodice(faker.code().isbn10());
                postazioneBean.setDescrizione(faker.lorem().sentence());
                postazioneBean.setTipo(tipo);
                postazioneBean.setOccupanti(faker.number().numberBetween(1, 10));
                postazioneBean.setEdificio(edificio);

                // Passa il PostazioneBean al metodo salvaPostazione
                postazioneService.salvaPostazione(postazioneBean);
            }
        }

        // Creazione utenti
        for (int i = 0; i < 10; i++) {
            System.out.println("Creando utente " + i);
            Utente utente = new Utente();
            utente.setUsername(faker.name().username());
            utente.setNomeCompleto(faker.name().fullName());
            utente.setEmail(faker.internet().emailAddress());
            utenteService.salvaUtente(utente);
        }

        // Creazione prenotazioni
        List<Utente> utenti = utenteService.trovaTutti(); // Recupera tutti gli utenti creati
        List<Postazione> postazioni = postazioneService.trovaTutti(); // Recupera tutte le postazioni esistenti

        // Scegliamo una data fissa per tutte le prenotazioni
        LocalDate dataPrenotazione = LocalDate.now().plusDays(7); // Prenotazione per la stessa data tra 7 giorni

        // Creazione di una prenotazione unica per ogni utente e postazione alla stessa data
        for (Utente utente : utenti) {
            // Per ogni utente, crea una prenotazione per una sola postazione alla volta
            boolean prenotazioneEffettuata = false; // Flag per garantire una sola prenotazione per utente

            for (Postazione postazione : postazioni) {
                if (!prenotazioneEffettuata) {
                    // Verifica se l'utente ha già una prenotazione per questa data
                    List<Prenotazione> prenotazioniEsistentiPerUtente = prenotazioneService.trovaPrenotazioniPerUtentePerData(utente.getId(), dataPrenotazione);
                    // Verifica se la postazione è già prenotata per la data specifica
                    List<Prenotazione> prenotazioniEsistentiPerPostazione = prenotazioneService.trovaPrenotazioniPerPostazionePerData(postazione.getId(), dataPrenotazione);

                    if (prenotazioniEsistentiPerUtente.isEmpty() && prenotazioniEsistentiPerPostazione.isEmpty()) {
                        // Crea la prenotazione se non esiste già
                        Prenotazione prenotazione = new Prenotazione(null, utente, postazione, dataPrenotazione);
                        prenotazioneService.creaPrenotazione(prenotazione);
                        System.out.println("Prenotazione per l'utente " + utente.getUsername() + " alla postazione " + postazione.getDescrizione() + " per il " + dataPrenotazione);
                        prenotazioneEffettuata = true; // Imposta il flag a true per fermare la creazione di altre prenotazioni per lo stesso utente
                    } else {
                        // Se l'utente ha già prenotato o la postazione è già prenotata
                        if (!prenotazioniEsistentiPerUtente.isEmpty()) {
                            System.out.println("Prenotazione già esistente per l'utente " + utente.getUsername() + " alla data " + dataPrenotazione);
                        }
                        if (!prenotazioniEsistentiPerPostazione.isEmpty()) {
                            System.out.println("Postazione " + postazione.getDescrizione() + " già prenotata per la data " + dataPrenotazione);
                        }
                    }
                }
            }
        }

        System.out.println("Database popolato con successo!");
    }


}
