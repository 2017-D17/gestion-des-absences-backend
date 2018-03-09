package dev.gda.api.service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import dev.gda.api.entite.Collaborateur;

@Service
@Profile("dev")
public class InitialiserDonneesServiceDev implements InitialiserDonneesService {

	private final Logger LOG = LoggerFactory.getLogger(this.getClass());

	@Value("${gda.server.users.url}")
	private String server;

	public InitialiserDonneesServiceDev() {
	}

	@Override
	public void initialiser() {
		LOG.info("Retrieve the employees from " + server);
		setDefaultAdmin(null);
	}

	/**
	 * Cette méthode permet d'attribuer le role admin à un manager
	 * 
	 * @param collaborateurs la liste des collaborateurs
	 */
	private void setDefaultAdmin(List<Collaborateur> collaborateurs) {
		LOG.info("Set default admin");
	}
}
