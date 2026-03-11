package wunschliste.reservierung;

import jakarta.persistence.*;
import lombok.Data;
import wunschliste.benutzer.Benutzer;
import wunschliste.wunsch.Wunsch;
import wunschliste.wunschliste.LocalDateTimeAttributeConverter;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "wunsch_reservierung")
public class WunschReservierung {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "wunsch_id", nullable = false)
    private Wunsch wunsch;

    @ManyToOne(optional = false)
    @JoinColumn(name = "reservierer_id", nullable = false)
    private Benutzer reservierer;

    @Convert(converter = LocalDateTimeAttributeConverter.class)
    @Column(name = "reserviert_am", nullable = false)
    private LocalDateTime reserviertAm;

    @Column(name = "aktiv", nullable = false)
    private Boolean aktiv;
}