package dev.gda.api.controller;

import dev.gda.api.entite.Collaborateur;
import dev.gda.api.entite.RoleType;
import dev.gda.api.exception.CollaborateurException;
import dev.gda.api.exception.CollaborateurNotFoundException;
import dev.gda.api.repository.CollaborateurRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlleur de la ressource collaborateur
 *
 */
@RestController
@RequestMapping(path = "/collaborateurs")
@CrossOrigin
public class CollaborateurController {

	@Autowired
	private CollaborateurRepository collaborateurRepository;

	/**
	 * Cette méthode permet de lister tous les collaborateurs
	 * 
	 * @return la liste des collaborateurs
	 */
	@GetMapping
	@Secured("ROLE_MANAGER")
	public List<Collaborateur> listerCollaborateurs() {
		return this.collaborateurRepository.findAll();
	}

	/**
	 * Cette méthode permet de donner le rôle administrateur à un collaborateur
	 * 
	 * @param matricule le matricule du collaborateur
	 * @return le collaborateur
	 * @throws CollaborateurException
	 */
	@PatchMapping("update-to-admin/{matricule}")
	@Secured("ROLE_MANAGER")
	public Collaborateur donnerRoleAdmin(@PathVariable Optional<String> matricule)
			throws CollaborateurException {

		Collaborateur col = checkCollab(matricule,
				roles -> roles.contains(RoleType.ROLE_ADMIN),
				"Collaborateur is already admin");

		List<RoleType> roles = new ArrayList<>(col.getRoles());
		roles.add(RoleType.ROLE_ADMIN);
		col.setRoles(roles);

		this.collaborateurRepository.save(col);

		return col;
	}

	/**
	 * Cette méthode permet de retirer le rôle administrateur à un collaborateur
	 * 
	 * @param matricule le matricule du collaborateur
	 * @return le collaborateur
	 * @throws CollaborateurException
	 */
	@PatchMapping("remove-from-admin/{matricule}")
	@Secured("ROLE_MANAGER")
	public Collaborateur retirerRoleAdmin(@PathVariable Optional<String> matricule)
			throws CollaborateurException {

		Collaborateur col = checkCollab(matricule,
				roles -> !roles.contains(RoleType.ROLE_ADMIN),
				"Collaborateur is not admin");

		List<RoleType> roles = new ArrayList<>(col.getRoles());
		roles.remove(RoleType.ROLE_ADMIN);
		col.setRoles(roles);
		this.collaborateurRepository.save(col);

		return col;
	}

	private Collaborateur checkCollab(Optional<String> matricule,
			Predicate<List<RoleType>> predicate, String message)
			throws CollaborateurNotFoundException, CollaborateurException {

		Collaborateur col = matricule.map(this.collaborateurRepository::findOne)
				.orElseThrow(() -> new CollaborateurNotFoundException(
						"Collaborateur not found"));

		if (predicate.test(col.getRoles())) {
			throw new CollaborateurException(message);
		}
		return col;
	}

}
