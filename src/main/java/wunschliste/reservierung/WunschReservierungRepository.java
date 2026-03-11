package wunschliste.reservierung;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WunschReservierungRepository extends JpaRepository<WunschReservierung, Long> {

    Optional<WunschReservierung> findByWunsch_IdAndAktivTrue(Long wunschId);

    boolean existsByWunsch_IdAndAktivTrue(Long wunschId);

    Optional<WunschReservierung> findByWunsch_IdAndReservierer_BenutzerIdAndAktivTrue(Long wunschId, Long benutzerId);
}