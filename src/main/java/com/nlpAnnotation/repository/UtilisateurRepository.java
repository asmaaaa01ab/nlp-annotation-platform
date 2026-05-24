package com.nlpAnnotation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nlpAnnotation.models.Utilisateur;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Integer>{
	// Pour l'authentification Spring Security
    Utilisateur findByLogin(String login);

    // Pour vérifier si un login existe déjà
    boolean existsByLogin(String login);

    // Pour lister seulement les annotateurs actifs
    List<Utilisateur> findByActifTrue();

    // Pour la suppression logique (désactiver sans supprimer)
    List<Utilisateur> findByActifFalse();
}
