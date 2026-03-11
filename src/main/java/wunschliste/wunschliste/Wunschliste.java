package wunschliste.wunschliste;

import jakarta.persistence.*;
import lombok.Data;
import wunschliste.benutzer.Benutzer;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "wunschliste")
public class Wunschliste {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    private String titel;

    @ManyToOne(optional = false)
    @JoinColumn(name = "besitzer_id", nullable = false)
    private Benutzer besitzer;

    @Convert(converter = LocalDateTimeAttributeConverter.class)
    @Column(name = "erstellt_am", insertable = false, updatable = false)
    private LocalDateTime erstelltAm;

    @Convert(converter = LocalDateAttributeConverter.class)
    @Column(name = "eventdate")
    private LocalDate eventdate;

    @Column(name = "freigabe_token", nullable = false, unique = true, length = 100)
    private String freigabeToken;
}
