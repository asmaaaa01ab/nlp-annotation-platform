package com.nlpAnnotation.services;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.nlpAnnotation.models.Utilisateur;
import com.nlpAnnotation.repository.UtilisateurRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService{

    private final UtilisateurRepository utilisateurRepository;

    public CustomUserDetailsService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	System.out.println(">>> loadUserByUsername appelé pour: " + username);
        Utilisateur utilisateur = utilisateurRepository.findByLogin(username);

        if (utilisateur == null || !utilisateur.getActif()) {
        	System.out.println(">>> ERREUR: utilisateur null ou inactif");
            throw new UsernameNotFoundException( "Utilisateur non trouvé : " + username);
        }

        System.out.println(">>> Utilisateur trouvé: " + utilisateur.getLogin());
        System.out.println(">>> Password en base: " + utilisateur.getPassword());
        System.out.println(">>> Role: " + utilisateur.getRole().getNomRole());
        System.out.println(">>> Actif: " + utilisateur.getActif());
        
        UserDetails userDetails = User.builder()
            .username(utilisateur.getLogin())
            .password(utilisateur.getPassword())
            .authorities(utilisateur.getRole().getNomRole())
            .build();
            
        System.out.println(">>> UserDetails créé: " + userDetails.getUsername());
        System.out.println(">>> Password dans UserDetails: " + userDetails.getPassword());
        
        return userDetails;
    }
}
