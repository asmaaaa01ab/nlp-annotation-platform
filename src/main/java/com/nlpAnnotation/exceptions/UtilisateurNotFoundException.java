package com.nlpAnnotation.exceptions;

public class UtilisateurNotFoundException extends RuntimeException{
	public UtilisateurNotFoundException(int id) {
        super("Utilisateur introuvable avec l'id : " + id);
    }

    public UtilisateurNotFoundException(String login) {
        super("Utilisateur introuvable avec le login : " + login);
    }
}
