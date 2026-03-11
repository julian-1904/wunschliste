package wunschliste.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import wunschliste.benutzer.Benutzer;
import wunschliste.benutzer.BenutzerService;

@Controller
public class BenutzerController {

    private final BenutzerService service;

    public BenutzerController(BenutzerService service) {
        this.service = service;
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("benutzer", new Benutzer());
        return "registrieren";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute Benutzer benutzer, Model model) {

        try {
            service.registrieren(benutzer);
            return "redirect:/login";
        } catch (RuntimeException e) {
            model.addAttribute("fehler", e.getMessage());
            model.addAttribute("benutzer", benutzer);
            return "registrieren";
        }
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }


}
