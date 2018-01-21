package dev.gda.api.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import dev.gda.api.entite.Absence;
import dev.gda.api.entite.AbsenceType;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AbsenceValidatorTest {

  private Absence absence;
  
  @Autowired
  AbsenceValidator absenceValidator;
  
  @Before
  public void setup() {
    absence = null;
  }
  
  @Test(expected = Exception.class)
  public void test_absence_is_null() throws Exception {
    assertThat(absenceValidator.isValid(absence)).isTrue();
  }
  
  @Test(expected = Exception.class)
  public void test_absence_dateFin_before_dateDebut() throws Exception {
    absence = new Absence();
    absence.setDateDebut(LocalDate.now());
    absence.setDateFin(LocalDate.now().minusDays(1));
    assertThat(absenceValidator.isValid(absence)).isTrue();
  }
  
  @Test(expected = Exception.class)
  public void test_absence_motif_required() throws Exception {
    absence = new Absence();
    absence.setType(AbsenceType.CONGE_SANS_SOLDE);
    assertThat(absenceValidator.isValid(absence)).isTrue();
  }

}
