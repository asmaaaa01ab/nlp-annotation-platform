package com.nlpAnnotation.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.nlpAnnotation.exceptions.TacheNotFoundException;
import com.nlpAnnotation.models.Annotation;
import com.nlpAnnotation.models.CoupleTexte;
import com.nlpAnnotation.models.Tache;
import com.nlpAnnotation.models.Utilisateur;
import com.nlpAnnotation.patterns.chain.RepetitionHandler;
import com.nlpAnnotation.patterns.chain.SpamHandler;
import com.nlpAnnotation.patterns.chain.VitesseHandler;
import com.nlpAnnotation.repository.AnnotationRepository;
import com.nlpAnnotation.repository.CoupleTexteRepository;
import com.nlpAnnotation.repository.UtilisateurRepository;

@Service
public class AnnotationService {
    private final AnnotationRepository annotationRepository;
    private final CoupleTexteRepository coupleTexteRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final TacheService tacheService;
    public AnnotationService(AnnotationRepository annotationRepository,CoupleTexteRepository coupleTexteRepository,UtilisateurRepository utilisateurRepository,TacheService tacheService) {
    	this.annotationRepository = annotationRepository;
    	this.coupleTexteRepository = coupleTexteRepository;
    	this.utilisateurRepository = utilisateurRepository;
    	this.tacheService = tacheService;
    }
    public AnnotationRepository getAnnotationRepository() {
        return this.annotationRepository;
    }
    public void annoter(int coupleId, String classeChoisie,int annotateurId) throws Exception {
        // Pattern Chain of Responsibility — vérification anti-spam
        SpamHandler vitesse = new VitesseHandler(annotationRepository);
        SpamHandler repetition = new RepetitionHandler(annotationRepository);
        vitesse.setSuivant(repetition);

        // Déclenche la chaîne — lève une exception si spam détecté
        vitesse.verifier(annotateurId);

        // Récupération des objets
        CoupleTexte couple = coupleTexteRepository.findById(coupleId)
            .orElseThrow(() -> new RuntimeException("Couple introuvable"));

        Utilisateur annotateur = utilisateurRepository.findById(annotateurId)
            .orElseThrow(() -> new RuntimeException("Annotateur introuvable"));

        // Vérifier si déjà annoté par cet annotateur
        List<Annotation> existantes =
            annotationRepository.findByCoupleTexteId(coupleId);
        boolean dejaAnnote = existantes.stream()
            .anyMatch(a -> a.getAnnotateur().getId() == annotateurId);

        if (dejaAnnote) {
            throw new RuntimeException("Ce couple est déjà annoté !");
        }

        // Créer et sauvegarder l'annotation
        Annotation annotation = new Annotation(classeChoisie,
                                               annotateur, couple);
        annotation.setDateAnnotation(LocalDateTime.now());
        annotationRepository.save(annotation);

        // Mettre à jour l'avancement de la tâche (Pattern Observer)
        List<com.nlpAnnotation.models.Tache> taches =
            tacheService.getTachesAnnotateur(annotateurId);
        taches.stream()
            .filter(t -> t.getDataset().getId()
                          == couple.getDataset().getId())
            .findFirst()
            .ifPresent(t -> tacheService.mettreAJourAvancement(t.getId()));
    }

    public List<Annotation> getAnnotations(int datasetId) {
        return annotationRepository.findByDatasetId(datasetId);
    }

    public List<Annotation> getAnnotationsAnnotateur(int annotateurId) {
        return annotationRepository.findByAnnotateurId(annotateurId);
    }

    public CoupleTexte getCoupleActuel(int tacheId, int annotateurId) {
        Tache tache = tacheService.findById(tacheId);
        List<CoupleTexte> couples = coupleTexteRepository.findByDatasetId(tache.getDataset().getId());

        // Retourne le premier couple non encore annoté par cet annotateur
        return couples.stream()
            .filter(c -> annotationRepository
                .findByCoupleTexteId(c.getId())
                .stream()
                .noneMatch(a -> a.getAnnotateur().getId() == annotateurId))
            .findFirst()
            .orElseThrow(() -> new TacheNotFoundException(tacheId));
    }

    public List<Object[]> getDistributionClasses(int annotateurId) {
        return annotationRepository.getDistributionClasses(annotateurId);
    }

    public int compterAnnotations(int annotateurId, int datasetId) {
        return annotationRepository
            .countByAnnotateurIdAndDatasetId(annotateurId, datasetId);
    }
}
