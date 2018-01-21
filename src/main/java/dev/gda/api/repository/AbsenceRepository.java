package dev.gda.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import dev.gda.api.entite.Absence;
import java.time.LocalDate;
import java.util.List;

public interface AbsenceRepository extends JpaRepository<Absence, Integer> {

  @Query("select a from Absence a "
    + "where a.collaborateur.matricule = :matricule and "
    + "(a.dateDebut <= :dateDebut and a.dateFin >= :dateFin or "
    + "a.dateDebut >= :dateDebut and a.dateDebut <= :dateFin or "
    + "a.dateFin >= :dateDebut and a.dateFin <= :dateFin )")
  List<Absence> findInvalidCreneaux(@Param("matricule") String matricule , @Param("dateDebut") LocalDate dateDebut, @Param("dateFin") LocalDate dateFin);

}
