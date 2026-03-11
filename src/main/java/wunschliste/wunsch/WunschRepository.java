package wunschliste.wunsch;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WunschRepository extends JpaRepository<Wunsch, Long> {

    List<Wunsch> findByWunschliste_IdOrderByPrioritaetAscTitelAsc(Long listeId);

}