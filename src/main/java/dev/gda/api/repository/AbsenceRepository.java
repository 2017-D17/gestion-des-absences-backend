package dev.gda.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import dev.gda.api.entite.Absence;

public interface AbsenceRepository extends JpaRepository<Absence, Integer> {
}
