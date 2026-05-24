package com.nlpAnnotation.patterns.factory;

import com.nlpAnnotation.models.Utilisateur;

public interface UtilisateurFactory {
	Utilisateur creer(String nom, String prenom, String login);
}
