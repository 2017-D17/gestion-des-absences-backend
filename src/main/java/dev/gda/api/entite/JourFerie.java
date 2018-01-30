package dev.gda.api.entite;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 * Cette classe represente l'entité jour férié
 * 
 * Un jour férié se définit par :
 *    - une date 
 *    - un type 
 *    - un commentaire
 * 
 * @see TypeJourFerie
 */

@Entity
public class JourFerie {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @NotNull
  private LocalDate date;
  @NotNull
  private JourFerieType type;
  @Column
  private String commentaire;
  
  public JourFerie() {
    super();
  }
  
  /**
   * @return the id
   */
  public Integer getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(Integer id) {
    this.id = id;
  }



  /**
   * @return the date
   */
  public LocalDate getDate() {
    return date;
  }

  /**
   * @param date the date to set
   */
  public void setDate(LocalDate date) {
    this.date = date;
  }

  /**
   * @return the type
   */
  public JourFerieType getType() {
    return type;
  }

  /**
   * @param type the type to set
   */
  public void setType(JourFerieType type) {
    this.type = type;
  }

  /**
   * @return the commentaire
   */
  public String getCommentaire() {
    return commentaire;
  }

  /**
   * @param commentaire the commentaire to set
   */
  public void setCommentaire(String commentaire) {
    this.commentaire = commentaire;
  }
  
  
  
}
