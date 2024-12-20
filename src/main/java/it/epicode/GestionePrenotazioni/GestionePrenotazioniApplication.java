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
public class GestionePrenotazioniApplication implements CommandLineRunner {

	@Autowired
	private EdificioService edificioService;

	@Autowired
	private PostazioneService postazioneService;

	@Autowired
	private UtenteService utenteService;

	@Autowired
	private PrenotazioneService prenotazioneService;

	public static void main(String[] args) {
		SpringApplication.run(GestionePrenotazioniApplication.class, args);
	}

	@Override
	public void run(String... args) {
		Scanner scanner = new Scanner(System.in);

		try {
			while (true) {
				System.out.println("=== MENU PRINCIPALE ===");
				System.out.println("1. Gestione Edifici");
				System.out.println("2. Gestione Postazioni");
				System.out.println("3. Gestione Utenti");
				System.out.println("4. Gestione Prenotazioni");
				System.out.println("0. Esci");

				int scelta = scanner.nextInt();
				scanner.nextLine(); // Consuma la nuova linea

				switch (scelta) {
					case 1:
						gestisciEdifici(scanner);
						break;
					case 2:
						gestisciPostazioni(scanner);
						break;
					case 3:
						gestisciUtenti(scanner);
						break;
					case 4:
						gestisciPrenotazioni(scanner);
						break;
					case 0:
						System.out.println("Chiusura applicazione.");
						return; // Esce dal ciclo e termina l'applicazione
					default:
						System.out.println("Opzione non valida.");
				}
			}
		} finally {
			scanner.close(); // Chiudi lo Scanner quando il programma termina
			System.out.println("Scanner chiuso.");
		}
	}

	private void gestisciEdifici(Scanner scanner) {
		while (true) {
			System.out.println("=== GESTIONE EDIFICI ===");
			System.out.println("1. Aggiungi Edificio");
			System.out.println("2. Visualizza tutti gli Edifici");
			System.out.println("3. Modifica Edificio");

			System.out.println("0. Torna al menu principale");

			int scelta = scanner.nextInt();
			scanner.nextLine(); // Consuma la nuova linea

			switch (scelta) {
				case 1:
					aggiungiEdificio(scanner);
					break;
				case 2:
					visualizzaEdifici();
					break;
				case 3:
					modificaEdificio(scanner);
					break;

				case 0:
					return; // Torna al menu principale
				default:
					System.out.println("Opzione non valida.");
			}
		}
	}
 @Transactional
	private void aggiungiEdificio(Scanner scanner) {
		System.out.println("Inserisci il nome dell'edificio:");
		String nome = scanner.nextLine();
		System.out.println("Inserisci l'indirizzo dell'edificio:");
		String indirizzo = scanner.nextLine();
		System.out.println("Inserisci la città dell'edificio:");
		String citta = scanner.nextLine();

		EdificioBean edificioBean = new EdificioBean();
		edificioBean.setNome(nome);
		edificioBean.setIndirizzo(indirizzo);
		edificioBean.setCitta(citta);

		Edificio edificio = edificioBean.buildEdificio();

		edificioService.salvaEdificio(edificio);
		System.out.println("Edificio aggiunto con successo!");
	}

	private void visualizzaEdifici() {
		List<Edificio> edifici = edificioService.trovaTutti();
		edifici.forEach(System.out::println);
	}

	private void modificaEdificio(Scanner scanner) {
		System.out.println("Inserisci l'ID dell'edificio da modificare:");
		Long idModifica = scanner.nextLong();
		scanner.nextLine(); // Consuma la nuova linea
		System.out.println("Inserisci il nuovo nome dell'edificio:");
		String nuovoNome = scanner.nextLine();
		System.out.println("Inserisci il nuovo indirizzo dell'edificio:");
		String nuovoIndirizzo = scanner.nextLine();
		System.out.println("Inserisci la nuova città dell'edificio:");
		String nuovaCitta = scanner.nextLine();

		EdificioBean edificioBean = new EdificioBean();
		edificioBean.setNome(nuovoNome);
		edificioBean.setIndirizzo(nuovoIndirizzo);
		edificioBean.setCitta(nuovaCitta);

		Edificio edificio = edificioBean.buildEdificio();
		edificio.setId(idModifica); // Imposta l'ID per la modifica

		edificioService.salvaEdificio(edificio);
		System.out.println("Edificio modificato con successo!");
	}



	private void gestisciPostazioni(Scanner scanner) {
		while (true) {
			System.out.println("=== GESTIONE POSTAZIONI ===");
			System.out.println("1. Aggiungi Postazione");
			System.out.println("2. Visualizza tutte le Postazioni");
			System.out.println("0. Torna al menu principale");

			int scelta = scanner.nextInt();
			scanner.nextLine(); // Consuma la nuova linea

			switch (scelta) {
				case 1:
					aggiungiPostazione(scanner);
					break;
				case 2:
					visualizzaPostazioni();
					break;
				case 0:
					return; // Torna al menu principale
				default:
					System.out.println("Opzione non valida.");
			}
		}
	}

	@Transactional
	private void aggiungiPostazione(Scanner scanner) {
		System.out.println("Inserisci il codice della postazione:");
		String codice = scanner.nextLine();
		System.out.println("Inserisci la descrizione della postazione:");
		String descrizione = scanner.nextLine();
		System.out.println("Inserisci il tipo della postazione (PRIVATO, OPEN_SPACE, SALA_RIUNIONI):");
		TipoPostazione tipo = TipoPostazione.valueOf(scanner.nextLine().toUpperCase());
		System.out.println("Inserisci il numero massimo di occupanti:");
		int occupanti = scanner.nextInt();
		System.out.println("Inserisci l'ID dell'edificio associato:");
		Long edificioId = scanner.nextLong();

		Edificio edificio = edificioService.trovaPerId(edificioId);

		PostazioneBean postazioneBean = new PostazioneBean();
		postazioneBean.setCodice(codice);
		postazioneBean.setDescrizione(descrizione);
		postazioneBean.setTipo(tipo);
		postazioneBean.setOccupanti(occupanti);
		postazioneBean.setEdificio(edificio);

		postazioneService.salvaPostazione(postazioneBean);
		System.out.println("Postazione aggiunta con successo!");
	}

	private void visualizzaPostazioni() {
		List<Postazione> postazioni = postazioneService.trovaTutti();
		postazioni.forEach(System.out::println);
	}

	private void gestisciUtenti(Scanner scanner) {
		while (true) {
			System.out.println("=== GESTIONE UTENTI ===");
			System.out.println("1. Aggiungi Utente");
			System.out.println("2. Visualizza tutti gli Utenti");
			System.out.println("0. Torna al menu principale");

			int scelta = scanner.nextInt();
			scanner.nextLine(); // Consuma la nuova linea

			switch (scelta) {
				case 1:
					aggiungiUtente(scanner);
					break;
				case 2:
					visualizzaUtenti();
					break;
				case 0:
					return; // Torna al menu principale
				default:
					System.out.println("Opzione non valida.");
			}
		}
	}

	private void aggiungiUtente(Scanner scanner) {
		System.out.println("Inserisci il username dell'utente:");
		String username = scanner.nextLine();
		System.out.println("Inserisci il nome completo dell'utente:");
		String nomeCompleto = scanner.nextLine();
		System.out.println("Inserisci l'email dell'utente:");
		String email = scanner.nextLine();

		Utente utente = new Utente(null, username, nomeCompleto, email);
		utenteService.salvaUtente(utente);
		System.out.println("Utente aggiunto con successo!");
	}

	private void visualizzaUtenti() {
		List<Utente> utenti = utenteService.trovaTutti();
		utenti.forEach(System.out::println);
	}

	private void gestisciPrenotazioni(Scanner scanner) {
		while (true) {
			System.out.println("=== GESTIONE PRENOTAZIONI ===");
			System.out.println("1. Prenota una Postazione");
			System.out.println("2. Visualizza Prenotazioni dell'Utente");
			System.out.println("0. Torna al menu principale");

			int scelta = scanner.nextInt();
			scanner.nextLine(); // Consuma la nuova linea

			switch (scelta) {
				case 1:
					prenotaPostazione(scanner);
					break;
				case 2:
					visualizzaPrenotazioni(scanner);
					break;
				case 0:
					return; // Torna al menu principale
				default:
					System.out.println("Opzione non valida.");
			}
		}
	}

	private void prenotaPostazione(Scanner scanner) {
		System.out.println("Inserisci lo username dell'utente che prenota:");
		String username = scanner.nextLine();
		Utente utente = utenteService.trovaPerUsername(username);

		System.out.println("Inserisci il tipo di postazione (PRIVATO, OPEN_SPACE, SALA_RIUNIONI):");
		TipoPostazione tipo = TipoPostazione.valueOf(scanner.nextLine().toUpperCase());
		System.out.println("Inserisci la città di interesse:");
		String citta = scanner.nextLine();

		List<Postazione> postazioni = postazioneService.trovaPostazioniPerTipoECitta(tipo, citta);
		if (postazioni.isEmpty()) {
			System.out.println("Nessuna postazione disponibile per i criteri specificati.");
			return;
		}

		System.out.println("Seleziona l'ID della postazione da prenotare:");
		postazioni.forEach(postazione -> System.out.println(postazione.getId() + ": " + postazione.getDescrizione()));
		Long postazioneId = scanner.nextLong();
		scanner.nextLine(); // Consuma la nuova linea

		Postazione postazione = postazioneService.trovaPostazionePerId(postazioneId);

		System.out.println("Inserisci la data della prenotazione (YYYY-MM-DD):");
		String dataString = scanner.nextLine();
		LocalDate data = LocalDate.parse(dataString);

		Prenotazione prenotazione = new Prenotazione(null, utente, postazione, data);
		prenotazioneService.creaPrenotazione(prenotazione);
		System.out.println("Prenotazione effettuata con successo!");
	}

	private void visualizzaPrenotazioni(Scanner scanner) {
		System.out.println("Inserisci lo username dell'utente per visualizzare le sue prenotazioni:");
		String username = scanner.nextLine();
		Utente utente = utenteService.trovaPerUsername(username);

		// Usa l'ID dell'utente (utente.getId()) per recuperare le prenotazioni
		List<Prenotazione> prenotazioni = prenotazioneService.trovaPrenotazioniPerUtente(utente.getId());
		prenotazioni.forEach(System.out::println);
	}
}
