package it.epicode.GestionePrenotazioni.configurations;

import it.epicode.GestionePrenotazioni.entities.Edificio;
import lombok.Setter;

@Setter
public class EdificioBean {

    private String nome;
    private String indirizzo;
    private String citta;

    public Edificio buildEdificio() {
        Edificio edificio = new Edificio();
        edificio.setNome(this.nome);
        edificio.setIndirizzo(this.indirizzo);
        edificio.setCitta(this.citta);
        return edificio;
    }
}
