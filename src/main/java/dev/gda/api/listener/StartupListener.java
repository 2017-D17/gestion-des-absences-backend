package dev.gda.api.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import dev.gda.api.service.InitialiserDonneesService;

@Component
public class StartupListener {

	@Autowired
	private InitialiserDonneesService initialiserDonneesServiceDev;
	
	@EventListener(ContextRefreshedEvent.class)
	public void contextRefreshedEvent(){
		initialiserDonneesServiceDev.initialiser();
	}
}
