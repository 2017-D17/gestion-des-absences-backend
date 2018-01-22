package dev.gda.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.gda.api.entite.Absence;
import dev.gda.api.entite.AbsenceStatut;
import dev.gda.api.entite.Collaborateur;

public interface AbsenceRepository extends JpaRepository<Absence, Integer> {

	List<Absence> findByCollaborateur(Collaborateur collaborateur);
	List<Absence> findByStatut(AbsenceStatut statut);
}
