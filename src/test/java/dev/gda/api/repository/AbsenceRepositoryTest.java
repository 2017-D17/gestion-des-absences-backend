package dev.gda.api.repository;

import java.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

import dev.gda.api.entite.Absence;
import dev.gda.api.entite.Collaborateur;
import dev.gda.api.entite.Statut;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AbsenceRepositoryTest {

  @Autowired
  private AbsenceRepository absenceRepository;
  @Autowired
  private CollaborateurRepository collaborateurRepository;
  
  private Collaborateur jean;
  
  private LocalDate debut;
  private LocalDate fin;

  @Before
  public void setup() {
    jean = new Collaborateur();
    jean.setMatricule("UUID3");
    jean.setNom("jean");
    jean.setPrenom("dort");
    this.collaborateurRepository.save(jean);
    
    Absence absence = new Absence();
    absence.setDateDebut(LocalDate.now().plusDays(2));
    absence.setDateFin(LocalDate.now().plusDays(4));
    absence.setStatut(Statut.INITIALE);
    absence.setCollaborateur(jean);
    this.absenceRepository.save(absence);
  }

  @After
  public void tearDown() {
    debut = null;
    fin = null;
  }
  
  /*
   *  ici, on teste le cas où la date de début se trouve au milieu
   *  
   *  ex: 
   *  en base on a :
   *                22-01-2018 -> 24-01-2018
   *                            ^
   *  et notre recherche   23-01-2018 -> 30-01-2018
   */
  @Test
  public void test_findInvalidCreneaux_with_dateDebut_in_middle() {
    debut = LocalDate.now().plusDays(3);
    fin = LocalDate.now().plusDays(7);
    assertThat(this.absenceRepository.findInvalidCreneaux(jean.getMatricule(), debut, fin)).isNotEmpty();
  }
  
  /*
   *  ici, on teste le cas où la date de fin se trouve au milieu
   *  
   *  ex: 
   *  en base on a :
   *                            22-01-2018 -> 24-01-2018
   *                                        ^
   *  et notre recherche   21-01-2018 -> 23-01-2018
   */
  @Test
  public void test_findInvalidCreneaux_with_dateFin_in_middle() {
    debut = LocalDate.now().plusDays(1);
    fin = LocalDate.now().plusDays(3);
    assertThat(this.absenceRepository.findInvalidCreneaux(jean.getMatricule(), debut, fin)).isNotEmpty();
  }
  
  /*
   *  ici, on teste le cas où les dates début et fin se trouvent à l'extrémité
   *  
   *  ex: 
   *  en base on a :
   *                            22-01-2018 -> 24-01-2018
   *                                        
   *  et notre recherche   21-01-2018 -> 28-01-2018
   */
  @Test
  public void test_findInvalidCreneaux_with_dateDebut_and_dateFin_out() {
    debut = LocalDate.now().plusDays(1);
    fin = LocalDate.now().plusDays(7);
    assertThat(this.absenceRepository.findInvalidCreneaux(jean.getMatricule(), debut, fin)).isNotEmpty();
  }
  
  /*
   *  ici, on teste le cas où les dates choisies sont correctes
   *  
   *  ex: 
   *  en base on a :
   *          22-01-2018 -> 24-01-2018
   *          
   *  et notre recherche   31-01-2018 -> 02-02-2018
   */
  @Test
  public void test_findInvalidCreneaux_empty() {
    debut = LocalDate.now().plusDays(10);
    fin = LocalDate.now().plusDays(12);
    assertThat(this.absenceRepository.findInvalidCreneaux(jean.getMatricule(), debut, fin)).isEmpty();
  }


}
