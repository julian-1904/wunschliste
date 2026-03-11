package wunschliste.wunschliste;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WunschlisteMitgliedService {

    private final JdbcTemplate jdbcTemplate;

    public WunschlisteMitgliedService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean istMitglied(Long listeId, Long benutzerId) {

        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM wunschliste_mitglied WHERE liste_id = ? AND benutzer_id = ?",
                Integer.class,
                listeId,
                benutzerId
        );

        return count != null && count > 0;
    }

    public void alsMitgliedHinzufuegen(Long listeId, Long benutzerId) {

        if (istMitglied(listeId, benutzerId)) {
            return;
        }

        jdbcTemplate.update(
                "INSERT INTO wunschliste_mitglied (liste_id, benutzer_id, rolle) VALUES (?, ?, 'MITGLIED')",
                listeId,
                benutzerId
        );
    }

    public List<String> findeMitgliederNamenFuerListe(Long listeId) {
        return jdbcTemplate.query(
                """
                SELECT b.vorname || ' ' || b.nachname
                FROM wunschliste_mitglied wm
                JOIN benutzer b ON b.id = wm.benutzer_id
                WHERE wm.liste_id = ?
                ORDER BY b.vorname, b.nachname
                """,
                (rs, rowNum) -> rs.getString(1),
                listeId
        );
    }
}