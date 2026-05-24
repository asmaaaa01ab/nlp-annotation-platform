package com.nlpAnnotation.patterns.factory;

import com.nlpAnnotation.models.Utilisateur;

public class AnnotateurFactory implements UtilisateurFactory{
	@Override
    public Utilisateur creer(String nom, String prenom, String login) {
        Utilisateur annotateur = new Utilisateur();
        annotateur.setNom(nom);
        annotateur.setPrenom(prenom);
        annotateur.setLogin(login);
        annotateur.setActif(true);
        return annotateur;
    }
}
