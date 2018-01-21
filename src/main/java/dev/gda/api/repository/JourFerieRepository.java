package dev.gda.api.repository;

import dev.gda.api.entite.JourFerie;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JourFerieRepository extends JpaRepository<JourFerie, Integer> {
  Optional<JourFerie> findByDate(LocalDate date);
}
