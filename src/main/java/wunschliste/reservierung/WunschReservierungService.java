package wunschliste.reservierung;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wunschliste.benutzer.Benutzer;
import wunschliste.wunsch.Wunsch;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class WunschReservierungService {

    private final WunschReservierungRepository repo;

    public WunschReservierungService(WunschReservierungRepository repo) {
        this.repo = repo;
    }

    public Optional<WunschReservierung> getAktiveReservierung(Long wunschId) {
        return repo.findByWunsch_IdAndAktivTrue(wunschId);
    }

    public boolean istReserviert(Long wunschId) {
        return repo.existsByWunsch_IdAndAktivTrue(wunschId);
    }

    public boolean istVonBenutzerReserviert(Long wunschId, Long benutzerId) {
        return repo.findByWunsch_IdAndReservierer_BenutzerIdAndAktivTrue(wunschId, benutzerId).isPresent();
    }

    public WunschReservierung reservieren(Wunsch wunsch, Benutzer reservierer) {
        if (repo.existsByWunsch_IdAndAktivTrue(wunsch.getId())) {
            throw new RuntimeException("Wunsch ist bereits reserviert.");
        }

        WunschReservierung reservierung = new WunschReservierung();
        reservierung.setWunsch(wunsch);
        reservierung.setReservierer(reservierer);
        reservierung.setReserviertAm(LocalDateTime.now());
        reservierung.setAktiv(true);

        return repo.save(reservierung);
    }

    public void aufheben(Long wunschId, Long benutzerId) {
        WunschReservierung reservierung = repo
                .findByWunsch_IdAndReservierer_BenutzerIdAndAktivTrue(wunschId, benutzerId)
                .orElseThrow(() -> new RuntimeException("Keine aktive Reservierung gefunden."));

        reservierung.setAktiv(false);
        repo.save(reservierung);
    }
}