package com.nlpAnnotation.exceptions;

public class TacheNotFoundException extends RuntimeException{
	public TacheNotFoundException(int id) {
        super("Tâche introuvable avec l'id : " + id);
    }
}
