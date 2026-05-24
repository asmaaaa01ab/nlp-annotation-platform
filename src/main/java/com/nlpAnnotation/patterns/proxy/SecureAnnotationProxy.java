package com.nlpAnnotation.patterns.proxy;

import java.util.List;

import com.nlpAnnotation.models.Annotation;

public class SecureAnnotationProxy implements IAnnotationService{
	private IAnnotationService vraiService;
    private String roleUtilisateur;

    public SecureAnnotationProxy(IAnnotationService service,
                                  String role) {
        this.vraiService = service;
        this.roleUtilisateur = role;
    }

    @Override
    public void annoter(int coupleId, String classeChoisie,
                        int annotateurId) throws Exception {
        if (!roleUtilisateur.equals("ANNOTATEUR")) {
            throw new SecurityException("Accès refusé : seul un annotateur peut annoter !");
        }
        vraiService.annoter(coupleId, classeChoisie, annotateurId);
    }

    @Override
    public List<Annotation> getAnnotations(int datasetId) {
        if (!roleUtilisateur.equals("ADMIN")) {
            throw new SecurityException("Accès refusé : seul l'admin peut voir toutes les annotations !");
        }
        return vraiService.getAnnotations(datasetId);
    }
}
