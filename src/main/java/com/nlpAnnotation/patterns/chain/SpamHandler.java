package com.nlpAnnotation.patterns.chain;

public abstract class SpamHandler {
	protected SpamHandler suivant;

    public void setSuivant(SpamHandler suivant) {
        this.suivant = suivant;
    }

    public abstract void verifier(int annotateurId) throws Exception;
}
