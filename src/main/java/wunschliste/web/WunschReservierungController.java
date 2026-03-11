package wunschliste.web;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import wunschliste.benutzer.Benutzer;
import wunschliste.benutzer.BenutzerRepository;
import wunschliste.reservierung.WunschReservierungService;
import wunschliste.wunsch.Wunsch;
import wunschliste.wunsch.WunschService;
import wunschliste.wunschliste.Wunschliste;

@Controller
public class WunschReservierungController {

    private final WunschReservierungService reservierungService;
    private final WunschService wunschService;
    private final BenutzerRepository benutzerRepository;

    public WunschReservierungController(WunschReservierungService reservierungService,
                                        WunschService wunschService,
                                        BenutzerRepository benutzerRepository) {
        this.reservierungService = reservierungService;
        this.wunschService = wunschService;
        this.benutzerRepository = benutzerRepository;
    }

    @PostMapping("/wuensche/{id}/reservieren")
    public String reservieren(@PathVariable Long id, Authentication authentication) {
        Wunsch wunsch = wunschService.getById(id);
        Wunschliste liste = wunsch.getWunschliste();

        Benutzer benutzer = benutzerRepository.findByEmail(authentication.getName())
                .orElseThrow();

        if (liste.getBesitzer() != null &&
                liste.getBesitzer().getBenutzerId().equals(benutzer.getBenutzerId())) {
            throw new RuntimeException("Der Besitzer darf nicht reservieren.");
        }

        reservierungService.reservieren(wunsch, benutzer);

        return "redirect:/wunschlisten/" + liste.getId();
    }

    @PostMapping("/wuensche/{id}/reservierung-aufheben")
    public String reservierungAufheben(@PathVariable Long id, Authentication authentication) {
        Wunsch wunsch = wunschService.getById(id);
        Benutzer benutzer = benutzerRepository.findByEmail(authentication.getName())
                .orElseThrow();

        reservierungService.aufheben(id, benutzer.getBenutzerId());

        return "redirect:/wunschlisten/" + wunsch.getWunschliste().getId();
    }
}