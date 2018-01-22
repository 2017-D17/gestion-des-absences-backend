package dev.gda.api.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import dev.gda.api.repository.CollaborateurRepository;
import dev.gda.api.service.HttpService;
import dev.gda.api.service.InitialiserDonneesService;

@Component
public class StartupListener {

	@Autowired
	HttpService http;
	
	@Autowired
	CollaborateurRepository collabRepo;
	
	@Autowired
	private InitialiserDonneesService initialiserDonneesServiceDev;
	
	@EventListener(ContextRefreshedEvent.class)
	public void contextRefreshedEvent(){
		initialiserDonneesServiceDev.initialiser();
	}
}
