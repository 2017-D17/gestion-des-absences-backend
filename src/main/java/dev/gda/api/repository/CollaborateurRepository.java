package dev.gda.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

import dev.gda.api.entite.Collaborateur;

@CrossOrigin
public interface CollaborateurRepository extends JpaRepository<Collaborateur, String> {
	Optional<Collaborateur> findByEmail(String email);
}