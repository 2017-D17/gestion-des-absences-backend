package dev.gda.api.repository;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import dev.gda.api.entite.Collaborateur;

@DataJpaTest
@RunWith(SpringRunner.class)
public class CollaborateurRepositoryTest {

 
  @Autowired
  private CollaborateurRepository collaborateurRepository;
  

  @Before
  public void setup() {
	  Collaborateur jean = new Collaborateur();
    jean.setMatricule("UUID3");
    jean.setNom("jean");
    jean.setPrenom("dort");
    this.collaborateurRepository.save(jean);
	  Collaborateur albert = new Collaborateur();
	  albert.setMatricule("UUID2");
	  albert.setNom("albert");
	  albert.setPrenom("sereveille");
    this.collaborateurRepository.save(albert);
    jean.getSubalternes().add(albert);
    this.collaborateurRepository.save(jean);
  }
  
  @Test
  public void test() {
	  Optional<String> collab = collaborateurRepository.findManagerMatricule("UUID2");
	  assert collab.isPresent():"Should not be null";
	  
	  
	  Optional<String> collab1 = collaborateurRepository.findManagerMatricule("UUIDdd2");
	  assert !collab1.isPresent(): "Should be null";
  }

 

}
