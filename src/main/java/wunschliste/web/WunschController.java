package wunschliste.web;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import wunschliste.benutzer.Benutzer;
import wunschliste.benutzer.BenutzerRepository;
import wunschliste.wunsch.Prioritaet;
import wunschliste.wunsch.Wunsch;
import wunschliste.wunsch.WunschService;
import wunschliste.wunschliste.Wunschliste;
import wunschliste.wunschliste.WunschlisteService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Controller
public class WunschController {

    private final WunschService wunschService;
    private final WunschlisteService wunschlisteService;
    private final BenutzerRepository benutzerRepository;

    public WunschController(WunschService wunschService,
                            WunschlisteService wunschlisteService,
                            BenutzerRepository benutzerRepository) {
        this.wunschService = wunschService;
        this.wunschlisteService = wunschlisteService;
        this.benutzerRepository = benutzerRepository;
    }

    @GetMapping("/wunschlisten/{listeId}/wuensche/neu")
    public String neuerWunschForm(@PathVariable Long listeId,
                                  Model model,
                                  Authentication authentication) {
        Wunschliste liste = wunschlisteService.getWunschlisteById(listeId);
        pruefeBesitzer(liste, authentication);

        Wunsch wunsch = new Wunsch();
        wunsch.setWunschliste(liste);

        model.addAttribute("liste", liste);
        model.addAttribute("wunsch", wunsch);
        model.addAttribute("prioritaeten", Prioritaet.values());

        return "wunsch_neu";
    }

    @PostMapping("/wunschlisten/{listeId}/wuensche/neu")
    public String neuenWunschSpeichern(@PathVariable Long listeId,
                                       @ModelAttribute Wunsch wunsch,
                                       @RequestParam(value = "bildDatei", required = false) MultipartFile bildDatei,
                                       Authentication authentication) {
        Wunschliste liste = wunschlisteService.getWunschlisteById(listeId);
        pruefeBesitzer(liste, authentication);

        if (bildDatei != null && !bildDatei.isEmpty()) {
            String dateiname = speichereBildDatei(bildDatei);
            wunsch.setBildDateiname(dateiname);
        }

        wunsch.setWunschliste(liste);
        wunschService.erstellen(wunsch);

        return "redirect:/wunschlisten/" + listeId;
    }

    @GetMapping("/wuensche/{id}/bearbeiten")
    public String wunschBearbeitenForm(@PathVariable Long id,
                                       Model model,
                                       Authentication authentication) {
        Wunsch wunsch = wunschService.getById(id);
        Wunschliste liste = wunsch.getWunschliste();

        pruefeBesitzer(liste, authentication);

        model.addAttribute("liste", liste);
        model.addAttribute("wunsch", wunsch);
        model.addAttribute("prioritaeten", Prioritaet.values());

        return "wunsch_bearbeiten";
    }

    @PostMapping("/wuensche/{id}/bearbeiten")
    public String wunschBearbeitenSpeichern(@PathVariable Long id,
                                            @ModelAttribute Wunsch formularWunsch,
                                            @RequestParam(value = "bildDatei", required = false) MultipartFile bildDatei,
                                            Authentication authentication) {
        Wunsch bestehenderWunsch = wunschService.getById(id);
        Wunschliste liste = bestehenderWunsch.getWunschliste();

        pruefeBesitzer(liste, authentication);

        String bildDateiname = bestehenderWunsch.getBildDateiname();

        if (bildDatei != null && !bildDatei.isEmpty()) {
            bildDateiname = speichereBildDatei(bildDatei);
        }

        bestehenderWunsch.setTitel(formularWunsch.getTitel());
        bestehenderWunsch.setUrl(formularWunsch.getUrl());
        bestehenderWunsch.setBildUrl(formularWunsch.getBildUrl());
        bestehenderWunsch.setBildDateiname(bildDateiname);
        bestehenderWunsch.setPrioritaet(formularWunsch.getPrioritaet());
        bestehenderWunsch.setPreis(formularWunsch.getPreis());
        bestehenderWunsch.setBeschreibung(formularWunsch.getBeschreibung());

        wunschService.erstellen(bestehenderWunsch);

        return "redirect:/wunschlisten/" + liste.getId();
    }

    @PostMapping("/wuensche/{id}/loeschen")
    public String wunschLoeschen(@PathVariable Long id,
                                 Authentication authentication) {
        Wunsch wunsch = wunschService.getById(id);
        Wunschliste liste = wunsch.getWunschliste();

        pruefeBesitzer(liste, authentication);

        Long listeId = liste.getId();
        wunschService.loeschen(id);

        return "redirect:/wunschlisten/" + listeId;
    }

    private void pruefeBesitzer(Wunschliste liste, Authentication authentication) {
        String aktuelleEmail = authentication.getName();

        Benutzer benutzer = benutzerRepository.findByEmail(aktuelleEmail)
                .orElseThrow();

        if (liste.getBesitzer() == null ||
                !liste.getBesitzer().getBenutzerId().equals(benutzer.getBenutzerId())) {
            throw new RuntimeException("Nur der Besitzer darf diese Aktion ausführen.");
        }
    }

    private String speichereBildDatei(MultipartFile bildDatei) {
        try {
            Path uploadPfad = Paths.get("uploads");

            if (!Files.exists(uploadPfad)) {
                Files.createDirectories(uploadPfad);
            }

            String originalDateiname = bildDatei.getOriginalFilename();
            String sichererOriginalname = originalDateiname != null ? originalDateiname : "bild";
            String neuerDateiname = UUID.randomUUID() + "_" + sichererOriginalname;

            Files.copy(
                    bildDatei.getInputStream(),
                    uploadPfad.resolve(neuerDateiname),
                    StandardCopyOption.REPLACE_EXISTING
            );

            return neuerDateiname;
        } catch (IOException e) {
            throw new RuntimeException("Bild konnte nicht gespeichert werden.");
        }
    }
}