package wunschliste.wunsch;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class WunschService {

    private final WunschRepository repo;

    public WunschService(WunschRepository repo) {
        this.repo = repo;
    }

    public Wunsch getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Wunsch nicht gefunden: " + id));
    }

    public List<Wunsch> getAlleFuerListe(Long listeId) {
        return repo.findByWunschliste_IdOrderByPrioritaetAscTitelAsc(listeId);
    }

    public Wunsch erstellen(Wunsch wunsch) {
        if (wunsch.getTitel() == null || wunsch.getTitel().isBlank()) {
            throw new RuntimeException("Titel darf nicht leer sein");
        }
        if (wunsch.getPrioritaet() == null) {
            throw new RuntimeException("Priorität muss gesetzt sein");
        }

        BigDecimal preis = wunsch.getPreis();
        if (preis != null) {
            wunsch.setPreis(preis.setScale(2, java.math.RoundingMode.HALF_UP));
        }

        if (wunsch.getErstelltAm() == null) {
            wunsch.setErstelltAm(LocalDateTime.now());
        }

        return repo.save(wunsch);
    }

    public Wunsch aktualisieren(Long id,
                                String titel,
                                String url,
                                String bildUrl,
                                Prioritaet prioritaet,
                                BigDecimal preis,
                                String beschreibung) {

        Wunsch w = getById(id);

        if (titel != null && !titel.isBlank()) {
            w.setTitel(titel);
        }

        w.setUrl(url);
        w.setBildUrl(bildUrl);

        if (prioritaet != null) {
            w.setPrioritaet(prioritaet);
        }

        w.setBeschreibung(beschreibung);

        if (preis != null) {
            w.setPreis(preis.setScale(2, java.math.RoundingMode.HALF_UP));
        } else {
            w.setPreis(null);
        }

        return repo.save(w);
    }

    public void loeschen(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Wunsch nicht gefunden: " + id);
        }
        repo.deleteById(id);
    }
}