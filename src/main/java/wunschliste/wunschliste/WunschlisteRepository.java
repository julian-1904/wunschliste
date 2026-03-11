package wunschliste.wunschliste;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WunschlisteRepository extends JpaRepository<Wunschliste, Long> {

    Optional<Wunschliste> findByFreigabeToken(String freigabeToken);

}