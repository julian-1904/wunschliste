package wunschliste.benutzer;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "benutzer")
public class Benutzer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long benutzerId;

    @Column(nullable = false)
    private String vorname;

    @Column(nullable = false)
    private String nachname;

    @Column(nullable = false)
    private String email;

    @Column(name = "passwort", nullable = false)
    private String passwort;
}
