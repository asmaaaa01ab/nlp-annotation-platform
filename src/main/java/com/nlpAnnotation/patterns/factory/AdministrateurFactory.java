package com.nlpAnnotation.patterns.factory;

import com.nlpAnnotation.models.Utilisateur;

public class AdministrateurFactory implements UtilisateurFactory{
	@Override
    public Utilisateur creer(String nom, String prenom, String login) {
        Utilisateur admin = new Utilisateur();
        admin.setNom(nom);
        admin.setPrenom(prenom);
        admin.setLogin(login);
        admin.setActif(true);
        return admin;
    }
}
