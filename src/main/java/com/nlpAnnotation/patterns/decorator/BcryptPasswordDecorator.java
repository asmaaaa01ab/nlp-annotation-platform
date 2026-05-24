package com.nlpAnnotation.patterns.decorator;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BcryptPasswordDecorator implements PasswordEncoder{
	private PasswordEncoder encoderBase;
    private BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();

    public BcryptPasswordDecorator(PasswordEncoder encoderBase) {
        this.encoderBase = encoderBase;
    }

    @Override
    public String encoder(String motDePasse) {
        // Appelle d'abord l'encodeur de base
        String motDePasseBase = encoderBase.encoder(motDePasse);
        // Puis applique BCrypt par dessus
        return bcrypt.encode(motDePasseBase);
    }
}
