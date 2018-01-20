package dev.gda.api.repository;

import dev.gda.api.entite.JourFerie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JourFerieRepository extends JpaRepository<JourFerie, Integer> {
  
}
