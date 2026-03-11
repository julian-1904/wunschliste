package wunschliste.wunsch;

import jakarta.persistence.*;
import lombok.Data;
import wunschliste.wunschliste.LocalDateTimeAttributeConverter;
import wunschliste.wunschliste.Wunschliste;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "wunsch")
public class Wunsch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "liste_id")
    private Wunschliste wunschliste;

    @Column(nullable = false, length = 120)
    private String titel;

    @Column(length = 400)
    private String url;

    @Column(name = "bild_url", length = 500)
    private String bildUrl;

    @Column(name = "bild_dateiname", length = 255)
    private String bildDateiname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Prioritaet prioritaet;

    @Column(precision = 10, scale = 2)
    private BigDecimal preis;

    @Column(length = 500)
    private String beschreibung;

    @Convert(converter = LocalDateTimeAttributeConverter.class)
    @Column(name = "erstellt_am", nullable = false)
    private LocalDateTime erstelltAm;
}
