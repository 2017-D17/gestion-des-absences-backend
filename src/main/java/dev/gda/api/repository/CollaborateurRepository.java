package dev.gda.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dev.gda.api.entite.Collaborateur;

public interface CollaborateurRepository extends JpaRepository<Collaborateur, String> {
	Optional<Collaborateur> findByEmail(String email);
		
	@Query(value="select COLLABORATEUR_MATRICULE from COLLABORATEUR_SUBALTERNES where SUBALTERNES_MATRICULE=:matricule", nativeQuery=true)
	Optional<String> findManagerMatricule(@Param("matricule") String matricule);
}