package wunschliste.wunschliste;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class WunschlisteService {

    private final WunschlisteRepository repo;
    private final WunschlisteMitgliedService mitgliedService;

    public WunschlisteService(WunschlisteRepository repo,
                              WunschlisteMitgliedService mitgliedService) {
        this.repo = repo;
        this.mitgliedService = mitgliedService;
    }

    public Wunschliste createWunschliste(Wunschliste wunschliste) {
        if (wunschliste.getFreigabeToken() == null || wunschliste.getFreigabeToken().isBlank()) {
            wunschliste.setFreigabeToken(UUID.randomUUID().toString());
        }
        return repo.save(wunschliste);
    }

    public Wunschliste getWunschlisteById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Wunschliste nicht gefunden"));
    }

    public Wunschliste getWunschlisteByFreigabeToken(String freigabeToken) {
        return repo.findByFreigabeToken(freigabeToken)
                .orElseThrow(() -> new RuntimeException("Freigabelink ungültig"));
    }

    public List<Wunschliste> getWunschlistenVonBenutzer(Long benutzerId) {
        return repo.findAll();
    }

    public boolean hatZugriff(Long listeId, Long benutzerId) {
        Wunschliste liste = getWunschlisteById(listeId);

        if (liste.getBesitzer() != null
                && liste.getBesitzer().getBenutzerId().equals(benutzerId)) {
            return true;
        }

        return mitgliedService.istMitglied(listeId, benutzerId);
    }

    public void fuegeMitgliedHinzuWennNoetig(Wunschliste liste, Long benutzerId) {
        if (liste.getBesitzer() != null
                && liste.getBesitzer().getBenutzerId().equals(benutzerId)) {
            return;
        }

        mitgliedService.alsMitgliedHinzufuegen(liste.getId(), benutzerId);
    }

    public List<String> getMitgliederNamenFuerListe(Long listeId) {
        return mitgliedService.findeMitgliederNamenFuerListe(listeId);
    }

    public void deleteWunschliste(Long id) {
        repo.deleteById(id);
    }
}