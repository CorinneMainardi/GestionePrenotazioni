package it.epicode.GestionePrenotazioni.configurations;

import it.epicode.GestionePrenotazioni.entities.Edificio;
import it.epicode.GestionePrenotazioni.entities.Postazione;
import it.epicode.GestionePrenotazioni.enums.TipoPostazione;
import lombok.Setter;

@Setter
public class PostazioneBean {

    private String codice;
    private String descrizione;
    private TipoPostazione tipo;
    private int occupanti;
    private Edificio edificio;

    // Getter e Setter

    // Metodo per costruire la Postazione da questo bean
    public Postazione buildPostazione() {
        Postazione postazione = new Postazione();
        postazione.setCodice(this.codice);
        postazione.setDescrizione(this.descrizione);
        postazione.setTipo(this.tipo);
        postazione.setNumeroMassimoOccupanti(this.occupanti);
        postazione.setEdificio(this.edificio);
        return postazione;
    }
}
