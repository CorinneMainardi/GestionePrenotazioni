package it.epicode.GestionePrenotazioni;

import it.epicode.GestionePrenotazioni.entities.Edificio;
import it.epicode.GestionePrenotazioni.entities.Postazione;
import it.epicode.GestionePrenotazioni.entities.Utente;
import it.epicode.GestionePrenotazioni.entities.Prenotazione;
import it.epicode.GestionePrenotazioni.enums.TipoPostazione;
import it.epicode.GestionePrenotazioni.services.EdificioService;
import it.epicode.GestionePrenotazioni.services.PostazioneService;
import it.epicode.GestionePrenotazioni.services.UtenteService;
import it.epicode.GestionePrenotazioni.services.PrenotazioneService;
import it.epicode.GestionePrenotazioni.configurations.PostazioneBean;
import it.epicode.GestionePrenotazioni.configurations.EdificioBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class GestionePrenotazioniApplication  {


	public static void main(String[] args) {
		SpringApplication.run(GestionePrenotazioniApplication.class, args);
	}

}