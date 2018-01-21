package dev.gda.api.repository;

import dev.gda.api.entite.JourFerie;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JourFerieRepository extends JpaRepository<JourFerie, Integer> {
  List<JourFerie> findByDate(LocalDate date);
}
