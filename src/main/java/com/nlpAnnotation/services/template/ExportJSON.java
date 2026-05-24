package com.nlpAnnotation.services.template;

import java.util.List;

import com.nlpAnnotation.models.Annotation;
import com.nlpAnnotation.repository.AnnotationRepository;


public class ExportJSON extends ExportTemplate{
	
	public ExportJSON(AnnotationRepository annotationRepository) {
        super(annotationRepository);
    }

    @Override
    protected String formater(List<Annotation> annotations) {
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");

        for (int i = 0; i < annotations.size(); i++) {
            Annotation a = annotations.get(i);
            sb.append("  {\n")
              .append("    \"id\": ").append(a.getId()).append(",\n")
              .append("    \"texte1\": \"")
                  .append(a.getCoupleTexte().getTexte1()).append("\",\n")
              .append("    \"texte2\": \"")
                  .append(a.getCoupleTexte().getTexte2()).append("\",\n")
              .append("    \"classe\": \"")
                  .append(a.getClasse()).append("\",\n")
              .append("    \"annotateur\": \"")
                  .append(a.getAnnotateur().getLogin()).append("\",\n")
              .append("    \"date\": \"")
                  .append(a.getDateAnnotation()).append("\"\n")
              .append("  }");

            // Virgule sauf pour le dernier élément
            if (i < annotations.size() - 1) {
                sb.append(",");
            }
            sb.append("\n");
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    protected String getExtension() {
        return "json";
    }
}
