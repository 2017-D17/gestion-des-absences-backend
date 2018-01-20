package dev.gda.api.controller;

import dev.gda.api.entite.JourFerie;
import dev.gda.api.repository.JourFerieRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlleur de la ressource jour férié
 */
@RestController
@RequestMapping(path = "/jours_feries")
@CrossOrigin
public class JourFerieController {

  @Autowired
  private JourFerieRepository jourFerieRepository;
  
  /**
   * Cette mérhode permet de lister tous les jours fériés
   * 
   * @return
   *      Une liste de tous les jours fériés si elle existe
   *      une liste vide sinon
   */
  @GetMapping
  public List<JourFerie> listerJoursFeries(){
    return this.jourFerieRepository.findAll();
  }
  
  
}
