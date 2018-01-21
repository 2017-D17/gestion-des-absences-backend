package dev.gda.api.controller;

import dev.gda.api.entite.JourFerie;
import dev.gda.api.entite.JourFerieType;
import dev.gda.api.exception.JourFerieException;
import dev.gda.api.exception.JourFerieNotFoundException;
import dev.gda.api.repository.JourFerieRepository;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
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
//  Cette méthode permet de modifier un jour férié

  /**
   * 
   * @param jourFerieId
   * @param jourFerie
   * @param response
   * @return
   * @throws JourFerieException
   */
  @PutMapping("/{jourFerieId}")
  public JourFerie modifierJourFerie(@PathVariable Optional<Integer> jourFerieId, @RequestBody @Valid JourFerie jourFerie) throws JourFerieException {

    checkIfJourFerieValidToModify(jourFerie);

    JourFerie jourFerieToModify = jourFerieId.map(this.jourFerieRepository::findOne)
      .orElseThrow(() -> new JourFerieNotFoundException("Day off not found"));

    // TODO (me) Il n'est pas possible de modifier une RTT employeur VALIDEE
    
    jourFerieToModify.setDate(jourFerie.getDate());
    jourFerieToModify.setType(jourFerie.getType());
    jourFerieToModify.setCommentaire(jourFerie.getCommentaire());
    return this.jourFerieRepository.save(jourFerieToModify);
  }

  /**
   * Cette méthode permet de verifier la validité du jour férié
   * 
   * @param jourFerie
   *      Le jour férié à modifier
   *      
   * @throws JourFerieException
   */
  private void checkIfJourFerieValidToModify(JourFerie jourFerie) throws JourFerieException {

    // un jour férié ne peut pas être saisi dans le passé
    if (jourFerie.getDate().isBefore(LocalDate.now())) {
      throw new JourFerieException("Date is in the past");
    }

    // * le commentaire est obligatoire pour les jours feriés.
    if (jourFerie.getType().equals(JourFerieType.JOUR_FERIE)) {

      if (jourFerie.getCommentaire() == null || jourFerie.getCommentaire().trim().isEmpty()) {
        throw new JourFerieException("A comment is required for this type");
      }
    } else {
      if (jourFerie.getDate().getDayOfWeek().equals(DayOfWeek.SATURDAY) || jourFerie.getDate().getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
        throw new JourFerieException("Date is invalid for this type");
      }
    }
    // * il est interdit de saisir un jour férié à la même date qu'un autre jour férié
    List<JourFerie> jfs = this.jourFerieRepository.findByDate(jourFerie.getDate());
    if (jfs.size() > 1) {
      throw new JourFerieException("Day off already exist for this date");
    }
    
  }
}
