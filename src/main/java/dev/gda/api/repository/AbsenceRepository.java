package dev.gda.api.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import dev.gda.api.entite.Absence;
import dev.gda.api.entite.Collaborateur;
import dev.gda.api.entite.Statut;

public interface AbsenceRepository extends JpaRepository<Absence, Integer> {

	List<Absence> findByCollaborateur(Collaborateur collaborateur);
	List<Absence> findByCollaborateurAndStatut(Collaborateur collaborateur, Statut statut);
	
	@Query("select a from Absence a where a.dateDebut < ?1 and a.dateFin > ?2 or a.dateDebut > ?1 and a.dateFin > ?2 ")
	List<Absence> findByDateDebutAndDateFin(LocalDate dateDebut, LocalDate dateFin);
}
