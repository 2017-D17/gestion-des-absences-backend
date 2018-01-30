package dev.gda.api.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.CrossOrigin;

import dev.gda.api.entite.Absence;
import dev.gda.api.entite.AbsenceStatut;
import dev.gda.api.entite.AbsenceType;
import dev.gda.api.entite.Collaborateur;

@CrossOrigin
public interface AbsenceRepository extends JpaRepository<Absence, Integer> {

	List<Absence> findByCollaborateur(Collaborateur collaborateur);
	List<Absence> findByStatut(AbsenceStatut statut);
	List<Absence> findByStatutOrderByIdAsc(AbsenceStatut statut);
	List<Absence> findByTypeAndDateDebut(AbsenceType type, LocalDate dateDebut);

	@Query("select a from Absence a "
			+ "where a.collaborateur.matricule = :matricule and "
			+ "(a.dateDebut <= :dateDebut and a.dateFin >= :dateFin or "
			+ "a.dateDebut >= :dateDebut and a.dateDebut <= :dateFin or "
			+ "a.dateFin >= :dateDebut and a.dateFin <= :dateFin  ) ")
	List<Absence> findInvalidCreneaux(@Param("matricule") String matricule , @Param("dateDebut") LocalDate dateDebut, @Param("dateFin") LocalDate dateFin);

}
