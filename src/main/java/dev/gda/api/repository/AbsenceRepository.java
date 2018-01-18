package dev.gda.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import dev.gda.api.entite.Absence;
import dev.gda.api.entite.Collaborateur;
import dev.gda.api.entite.Statut;

public interface AbsenceRepository extends JpaRepository<Absence, Integer> {

	List<Absence> findByCollaborateur(Collaborateur collaborateur);
	List<Absence> findByCollaborateurAndStatut(Collaborateur collaborateur, Statut statut);
}
