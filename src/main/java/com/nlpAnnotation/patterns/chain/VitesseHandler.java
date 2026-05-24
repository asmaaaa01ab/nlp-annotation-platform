package com.nlpAnnotation.patterns.chain;

import java.time.LocalDateTime;

import com.nlpAnnotation.repository.AnnotationRepository;

public class VitesseHandler extends SpamHandler{
	private final AnnotationRepository annotationRepository;

    // Seuil : max 10 annotations par minute
    private static final int SEUIL_VITESSE = 10;

    public VitesseHandler(AnnotationRepository annotationRepository) {
        this.annotationRepository = annotationRepository;
    }

    @Override
    public void verifier(int annotateurId) throws Exception {
        LocalDateTime ilYAUneMinute = LocalDateTime.now().minusMinutes(1);

        int nbRecentes = annotationRepository
            .countAnnotationsDepuis(annotateurId, ilYAUneMinute);

        if (nbRecentes >= SEUIL_VITESSE) {
            throw new Exception(
                "Spam détecté : annotations trop rapides (" 
                + nbRecentes + " en 1 minute) !");
        }

        // OK — passe au maillon suivant
        if (suivant != null) {
            suivant.verifier(annotateurId);
        }
    }
}
