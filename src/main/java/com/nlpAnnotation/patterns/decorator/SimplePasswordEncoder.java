package com.nlpAnnotation.patterns.decorator;

public class SimplePasswordEncoder implements PasswordEncoder{
	@Override
    public String encoder(String motDePasse) {
        // Retourne le mot de passe tel quel (base sans decoration)
        return motDePasse;
    }
}
