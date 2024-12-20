package it.epicode.GestionePrenotazioni.entities;

import it.epicode.GestionePrenotazioni.enums.TipoPostazione;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;




@Entity
@Data
@NoArgsConstructor
public class Postazione {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String codice;

    private String descrizione;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoPostazione tipo;

    @Column(name = "numero_massimo_occupanti", nullable = false)
    private int numeroMassimoOccupanti;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "edificio_id", nullable = false)
    private Edificio edificio;


}
