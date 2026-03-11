package wunschliste.benutzer;

import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class BenutzerService {

    private final BenutzerRepository repo;

    public BenutzerService(BenutzerRepository repo) {
        this.repo = repo;
    }

    public Benutzer getBenutzerById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Benutzer nicht gefunden"));
    }

    public Benutzer getBenutzerByEmail(String email) {
        return repo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Benutzer nicht gefunden"));
    }

    public Benutzer registrieren(Benutzer benutzer) {
        if (repo.findByEmail(benutzer.getEmail()).isPresent()) {
            throw new RuntimeException("Email bereits vergeben");
        }

        benutzer.setPasswort(sha1(benutzer.getPasswort()));
        return repo.save(benutzer);
    }

    private String sha1(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] hashBytes = md.digest(text.getBytes(StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-1 nicht verfügbar", e);
        }
    }
}
