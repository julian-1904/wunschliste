package wunschliste.web;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import wunschliste.benutzer.Benutzer;
import wunschliste.benutzer.BenutzerRepository;
import wunschliste.reservierung.WunschReservierung;
import wunschliste.reservierung.WunschReservierungService;
import wunschliste.wunsch.Wunsch;
import wunschliste.wunsch.WunschService;
import wunschliste.wunschliste.Wunschliste;
import wunschliste.wunschliste.WunschlisteService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class WunschlisteController {

    private final WunschlisteService service;
    private final BenutzerRepository benutzerRepository;
    private final WunschService wunschService;
    private final WunschReservierungService reservierungService;

    public WunschlisteController(WunschlisteService service,
                                 BenutzerRepository benutzerRepository,
                                 WunschService wunschService,
                                 WunschReservierungService reservierungService) {
        this.service = service;
        this.benutzerRepository = benutzerRepository;
        this.wunschService = wunschService;
        this.reservierungService = reservierungService;
    }

    @GetMapping("/wunschlisten")
    public String wunschlisten(Model model, Authentication authentication) {

        String email = authentication.getName();

        Benutzer benutzer = benutzerRepository.findByEmail(email)
                .orElseThrow();

        List<Wunschliste> listen = service.getWunschlistenVonBenutzer(benutzer.getBenutzerId());

        List<MeineReservierungEintrag> meineReservierungen = new ArrayList<>();
        Map<Long, Integer> reservierungsAnzahlProListe = new HashMap<>();
        Map<Long, Double> reservierungsSummeProListe = new HashMap<>();

        double meinGesamtbetrag = 0.0;

        for (Wunschliste liste : listen) {
            List<Wunsch> wuensche = wunschService.getAlleFuerListe(liste.getId());

            int anzahlInListe = 0;
            double summeInListe = 0.0;

            for (Wunsch wunsch : wuensche) {
                WunschReservierung reservierung = reservierungService
                        .getAktiveReservierung(wunsch.getId())
                        .orElse(null);

                if (reservierung != null
                        && reservierung.getReservierer() != null
                        && reservierung.getReservierer().getEmail() != null
                        && reservierung.getReservierer().getEmail().equals(email)) {

                    double preis = 0.0;
                    if (wunsch.getPreis() != null) {
                        preis = wunsch.getPreis().doubleValue();
                    }

                    meineReservierungen.add(new MeineReservierungEintrag(
                            liste.getId(),
                            liste.getTitel(),
                            wunsch.getId(),
                            wunsch.getTitel(),
                            preis
                    ));

                    anzahlInListe++;
                    summeInListe += preis;
                    meinGesamtbetrag += preis;
                }
            }

            reservierungsAnzahlProListe.put(liste.getId(), anzahlInListe);
            reservierungsSummeProListe.put(liste.getId(), summeInListe);
        }

        model.addAttribute("listen", listen);
        model.addAttribute("aktuelleEmail", email);
        model.addAttribute("meineReservierungen", meineReservierungen);
        model.addAttribute("meinGesamtbetrag", meinGesamtbetrag);
        model.addAttribute("reservierungsAnzahlProListe", reservierungsAnzahlProListe);
        model.addAttribute("reservierungsSummeProListe", reservierungsSummeProListe);

        return "wunschlisten";
    }

    @GetMapping("/wunschlisten/{id}")
    public String wunschliste(@PathVariable Long id,
                              Model model,
                              Authentication authentication,
                              HttpServletRequest request) {

        String aktuelleEmail = authentication.getName();

        Benutzer benutzer = benutzerRepository.findByEmail(aktuelleEmail)
                .orElseThrow();

        if (!service.hatZugriff(id, benutzer.getBenutzerId())) {
            throw new RuntimeException("Kein Zugriff auf diese Wunschliste.");
        }

        Wunschliste liste = service.getWunschlisteById(id);
        List<Wunsch> wuensche = wunschService.getAlleFuerListe(id);
        List<String> mitglieder = service.getMitgliederNamenFuerListe(id);

        Map<Long, WunschReservierung> reservierungen = new HashMap<>();
        List<MeineReservierungEintrag> meineReservierungenInListe = new ArrayList<>();
        double meinBetragInListe = 0.0;

        for (Wunsch wunsch : wuensche) {
            WunschReservierung reservierung = reservierungService
                    .getAktiveReservierung(wunsch.getId())
                    .orElse(null);

            reservierungen.put(wunsch.getId(), reservierung);

            if (reservierung != null
                    && reservierung.getReservierer() != null
                    && reservierung.getReservierer().getEmail() != null
                    && reservierung.getReservierer().getEmail().equals(aktuelleEmail)) {

                double preis = 0.0;
                if (wunsch.getPreis() != null) {
                    preis = wunsch.getPreis().doubleValue();
                }

                meineReservierungenInListe.add(new MeineReservierungEintrag(
                        liste.getId(),
                        liste.getTitel(),
                        wunsch.getId(),
                        wunsch.getTitel(),
                        preis
                ));

                meinBetragInListe += preis;
            }
        }

        boolean istBesitzer = liste.getBesitzer() != null
                && liste.getBesitzer().getBenutzerId().equals(benutzer.getBenutzerId());

        String shareLink = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/einladung/")
                .path(liste.getFreigabeToken())
                .toUriString();

        model.addAttribute("liste", liste);
        model.addAttribute("wuensche", wuensche);
        model.addAttribute("mitglieder", mitglieder);
        model.addAttribute("aktuelleEmail", aktuelleEmail);
        model.addAttribute("reservierungen", reservierungen);
        model.addAttribute("istBesitzer", istBesitzer);
        model.addAttribute("shareLink", shareLink);
        model.addAttribute("meineReservierungenInListe", meineReservierungenInListe);
        model.addAttribute("meinBetragInListe", meinBetragInListe);

        return "wunschliste";
    }

    @GetMapping("/einladung/{token}")
    public String einladungAnnehmen(@PathVariable String token,
                                    Authentication authentication,
                                    Model model) {

        String aktuelleEmail = authentication.getName();

        Benutzer benutzer = benutzerRepository.findByEmail(aktuelleEmail)
                .orElseThrow();

        try {
            Wunschliste liste = service.getWunschlisteByFreigabeToken(token);

            service.fuegeMitgliedHinzuWennNoetig(liste, benutzer.getBenutzerId());

            return "redirect:/wunschlisten/" + liste.getId();
        } catch (RuntimeException e) {
            model.addAttribute("fehlermeldung",
                    "Dieser Einladungslink ist ungültig oder existiert nicht mehr.");
            return "einladung_ungueltig";
        }
    }

    @GetMapping("/wunschlisten/neu")
    public String neueWunschlisteForm(Model model) {
        model.addAttribute("wunschliste", new Wunschliste());
        return "wunschliste_neu";
    }

    @PostMapping("/wunschlisten/neu")
    public String neueWunschlisteSpeichern(@ModelAttribute Wunschliste wunschliste,
                                           Authentication authentication) {

        String email = authentication.getName();

        Benutzer benutzer = benutzerRepository.findByEmail(email)
                .orElseThrow();

        wunschliste.setBesitzer(benutzer);

        service.createWunschliste(wunschliste);

        return "redirect:/wunschlisten";
    }

    @PostMapping("/wunschlisten/{id}/loeschen")
    public String wunschlisteLoeschen(@PathVariable Long id,
                                      Authentication authentication) {

        String email = authentication.getName();

        Benutzer benutzer = benutzerRepository.findByEmail(email)
                .orElseThrow();

        Wunschliste liste = service.getWunschlisteById(id);

        if (liste.getBesitzer() == null ||
                !liste.getBesitzer().getBenutzerId().equals(benutzer.getBenutzerId())) {
            throw new RuntimeException("Nur der Besitzer darf die Wunschliste löschen.");
        }

        service.deleteWunschliste(id);

        return "redirect:/wunschlisten";
    }
}