package com.nlpAnnotation.exceptions;

public class DatasetNotFoundException extends RuntimeException{
	public DatasetNotFoundException(int id) {
        super("Dataset introuvable avec l'id : " + id);
    }
}
