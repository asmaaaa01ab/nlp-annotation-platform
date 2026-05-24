package com.nlpAnnotation.patterns.facade;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.nlpAnnotation.models.Dataset;
import com.nlpAnnotation.models.Utilisateur;
import com.nlpAnnotation.services.DatasetService;
import com.nlpAnnotation.services.EmailService;
import com.nlpAnnotation.services.TacheService;
import com.nlpAnnotation.services.UtilisateurService;

@Component
public class AdminFacade {
    private final DatasetService datasetService;
    private final UtilisateurService utilisateurService;
    private final TacheService tacheService;
    private final EmailService emailService;
    public AdminFacade(DatasetService datasetService, UtilisateurService utilisateurService,TacheService tacheService,EmailService emailService) {
    	this.datasetService = datasetService;
    	this.utilisateurService = utilisateurService;
    	this.tacheService = tacheService;
    	this.emailService = emailService;
    }
    // Une seule méthode qui orchestre tout
    public void creerDatasetEtAffecterAnnotateurs(
            MultipartFile fichier,
            String nom,
            String description,
            String classes,
            List<Integer> annotateurIds,
            LocalDate deadline) throws Exception {

        // Étape 1 créer le dataset + importer les couples
        Dataset dataset = datasetService.creerDataset(nom, description, classes, fichier);

        // Étape 2 affecter les annotateurs avec répartition
        tacheService.affecterAnnotateurs(dataset, annotateurIds, deadline);

        // Étape 3 notifier chaque annotateur par email
        for (int id : annotateurIds) {
            Utilisateur a = utilisateurService.findById(id);
            emailService.notifierNouvelleTache(
                a.getLogin(), dataset.getNomDataset());
        }
    }
}
