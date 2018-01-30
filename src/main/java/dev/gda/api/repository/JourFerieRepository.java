package dev.gda.api.repository;

import dev.gda.api.entite.JourFerie;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
public interface JourFerieRepository extends JpaRepository<JourFerie, Integer> {
  List<JourFerie> findByDate(LocalDate date);
}
