package com.nlpAnnotation.services.template;

import java.util.List;
import com.nlpAnnotation.models.Annotation;
import com.nlpAnnotation.repository.AnnotationRepository;


public class ExportCSV extends ExportTemplate{
	
	public ExportCSV(AnnotationRepository annotationRepository) {
        super(annotationRepository);
    }

    @Override
    protected String formater(List<Annotation> annotations) {
        StringBuilder sb = new StringBuilder();
        // Entête CSV
        sb.append("id,texte1,texte2,classe,annotateur,date\n");

        for (Annotation a : annotations) {
            sb.append(a.getId()).append(",")
              .append(a.getCoupleTexte().getTexte1()).append(",")
              .append(a.getCoupleTexte().getTexte2()).append(",")
              .append(a.getClasse()).append(",")
              .append(a.getAnnotateur().getLogin()).append(",")
              .append(a.getDateAnnotation()).append("\n");
        }
        return sb.toString();
    }

    @Override
    protected String getExtension() {
        return "csv";
    }
}
