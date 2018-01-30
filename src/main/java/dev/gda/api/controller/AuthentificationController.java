package dev.gda.api.controller;

import dev.gda.api.auth.AuthCredentials;
import dev.gda.api.entite.Collaborateur;
import dev.gda.api.exception.AbsenceException;
import dev.gda.api.modelview.AuthenticationResponse;
import dev.gda.api.repository.CollaborateurRepository;
import dev.gda.api.util.JwtTokenUtil;
import dev.gda.api.util.ModelViewUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthentificationController {

  @Autowired
  private CollaborateurRepository collaborateurRepository;
    
  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private JwtTokenUtil jwtTokenUtil;

  @PostMapping("/login")
  public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthCredentials authCredentials) throws AuthenticationException, AbsenceException {

   final Authentication authentication = authenticationManager.authenticate(
           new UsernamePasswordAuthenticationToken(
             authCredentials.getEmail(),
             authCredentials.getPassword()
           )
   );
   SecurityContextHolder.getContext().setAuthentication(authentication);

   Collaborateur c = collaborateurRepository.findByEmail(authCredentials.getEmail()).orElseThrow(() -> new AbsenceException("No such user"));
   
   final String token = jwtTokenUtil.generateToken(c);
   
   return ResponseEntity.ok(new AuthenticationResponse(token,  ModelViewUtils.CollaborateurToCollaborateurView(c)));
   
  }
	
}
