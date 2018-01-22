package dev.gda.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.gda.api.entite.Collaborateur;

public interface CollaborateurRepository extends JpaRepository<Collaborateur, String> {
	Optional<Collaborateur> findByMatricule(String matricule);
	Collaborateur findByEmail(String email);
}