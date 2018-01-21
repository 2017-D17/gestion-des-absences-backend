package dev.gda.api.controller;

import dev.gda.api.entite.JourFerie;
import dev.gda.api.entite.JourFerieType;
import dev.gda.api.exception.JourFerieException;
import dev.gda.api.repository.JourFerieRepository;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
  

  /**
   * Cette mérhode permet d'ajouter un jour férié
   * 
   * @return
   *      le jour férié sauvegardé
   * @throws JourFerieException 
   *
   */
  @PostMapping
  public JourFerie ajouterJourFerie(@RequestBody @Valid JourFerie jourFerie, HttpServletResponse resp) throws JourFerieException {

    // un jour férié ne peut pas être saisi dans le passé
    if (jourFerie.getDate().isBefore(LocalDate.now())) {
      throw new JourFerieException("Date is in the past");
    }

    // * le commentaire est obligatoire pour les jours feriés.
    if (jourFerie.getType().equals(JourFerieType.JOUR_FERIE)) {

      if (jourFerie.getCommentaire() == null || jourFerie.getCommentaire().trim().isEmpty()) {
        throw new JourFerieException("A comment is required for this type");
      }
    }else {
      if (jourFerie.getDate().getDayOfWeek().equals(DayOfWeek.SATURDAY) || jourFerie.getDate().getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
        throw new JourFerieException("Date is invalid for this type");
      }
      
      // TODO (me)
      // * Si une RTT employeur est créée alors le système créé une demande d'abence au statut INITIALE.
      // Cette demande sera traitée lors du passage du batch de nuit.
      
    }
    // * il est interdit de saisir un jour férié à la même date qu'un autre jour férié
    if (this.jourFerieRepository.findByDate(jourFerie.getDate()).isPresent()) {
      throw new JourFerieException("Day off already exist for this date");
    }

    resp.setStatus(201);
    return this.jourFerieRepository.save(jourFerie);
  }

}
