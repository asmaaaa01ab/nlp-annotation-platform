package com.nlpAnnotation.services.template;

import java.util.List;

import com.nlpAnnotation.models.Annotation;
import com.nlpAnnotation.repository.AnnotationRepository;

import jakarta.servlet.http.HttpServletResponse;

public abstract class ExportTemplate {
	protected AnnotationRepository annotationRepository;

    public ExportTemplate(AnnotationRepository annotationRepository) {
        this.annotationRepository = annotationRepository;
    }

    // Méthode template — ordre fixe, ne change jamais
    public final void exporter(int datasetId, HttpServletResponse response) throws Exception {
        List<Annotation> annotations = chargerDonnees(datasetId);
        configurerReponse(response);
        String contenu = formater(annotations);
        ecrireContenu(contenu, response);
    }

    // Étape fixe 1 — chargement des données
    private List<Annotation> chargerDonnees(int datasetId) {
        return annotationRepository.findByDatasetId(datasetId);
    }

    // Étape fixe 2 — configuration headers HTTP
    private void configurerReponse(HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", 
            "attachment; filename=\"export." + getExtension() + "\"");
    }

    // Étape fixe 3 — écriture du contenu
    private void ecrireContenu(String contenu, HttpServletResponse response) throws Exception {
        response.getWriter().write(contenu);
        response.getWriter().flush();
    }

    // Étape VARIABLE — chaque sous-classe l'implémente
    protected abstract String formater(List<Annotation> annotations);

    // Étape VARIABLE — extension du fichier (csv ou json)
    protected abstract String getExtension();
}
