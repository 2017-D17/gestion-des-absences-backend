package dev.gda.api.controller;

import dev.gda.api.entite.JourFerie;
import dev.gda.api.exception.JourFerieException;
import dev.gda.api.exception.JourFerieNotFoundException;
import dev.gda.api.repository.JourFerieRepository;
import java.time.LocalDate;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
   * Cette méthode permet de supprimer un jour férié
   * 
   * 
   * @param jourFerieId
   *      l'id du jour férié
   * @param response
   * @throws JourFerieNotFoundException
   * @throws JourFerieException
   */
  @DeleteMapping("/{jourFerieId}")
  public void supprimerJoursFerie(@PathVariable Optional<Integer> jourFerieId, HttpServletResponse response) throws JourFerieException {
    JourFerie jf = jourFerieId.map(this.jourFerieRepository::findOne).orElseThrow(() -> new JourFerieNotFoundException("Day off not Found"));
    
    if(jf.getDate().isBefore(LocalDate.now())) {
      throw new JourFerieException("Day off is already past");
    }
//  TODO (me) il n'est pas possible de supprimer une RTT employeur validée
    
    this.jourFerieRepository.delete(jf.getId());
  }
}
