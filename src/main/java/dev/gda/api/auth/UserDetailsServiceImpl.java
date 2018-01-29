package dev.gda.api.auth;

import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import dev.gda.api.entite.Collaborateur;
import dev.gda.api.repository.CollaborateurRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  @Autowired  
  private CollaborateurRepository collaborateurRepository;


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      Optional<Collaborateur> collab = collaborateurRepository.findByEmail(username);
      
      return collab.map(c -> new User(
              c.getEmail(), 
              c.getPassword(), 
              c.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.name())).collect(Collectors.toList())
          ))
      .orElseThrow(() -> new UsernameNotFoundException(String.format("The username %s doesn't exist", username)));
    }

    
    
}