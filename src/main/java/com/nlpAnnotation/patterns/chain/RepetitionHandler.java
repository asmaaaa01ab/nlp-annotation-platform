package com.nlpAnnotation.patterns.chain;

import java.util.List;

import com.nlpAnnotation.repository.AnnotationRepository;

public class RepetitionHandler extends SpamHandler{
	private final AnnotationRepository annotationRepository;

    // Seuil : 90% des annotations avec la même classe = suspect
    private static final double SEUIL_REPETITION = 0.90;

    public RepetitionHandler(AnnotationRepository annotationRepository) {
        this.annotationRepository = annotationRepository;
    }

    @Override
    public void verifier(int annotateurId) throws Exception {
        List<Object[]> distribution = annotationRepository
            .getDistributionClasses(annotateurId);

        if (distribution == null || distribution.isEmpty()) {
            // Pas encore assez d'annotations, on laisse passer
            if (suivant != null) suivant.verifier(annotateurId);
            return;
        }

        // Compter le total
        long total = 0;
        for (Object[] row : distribution) {
            total += ((Number) row[1]).longValue();
        }

        // Vérifier si la classe la plus utilisée dépasse le seuil
        if (total >= 20) {
            long plusFrequente = ((Number) distribution.get(0)[1]).longValue();
            double taux = (double) plusFrequente / total;

            if (taux >= SEUIL_REPETITION) {
                throw new Exception(
                    "Spam détecté : " + (int)(taux * 100) 
                    + "% des annotations sont identiques !");
            }
        }

        // OK — passe au maillon suivant
        if (suivant != null) {
            suivant.verifier(annotateurId);
        }
    }
}
