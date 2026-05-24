package com.nlpAnnotation.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.nlpAnnotation.exceptions.TacheNotFoundException;
import com.nlpAnnotation.exceptions.UtilisateurNotFoundException;
import com.nlpAnnotation.models.CoupleTexte;
import com.nlpAnnotation.models.Dataset;
import com.nlpAnnotation.models.Tache;
import com.nlpAnnotation.models.Utilisateur;
import com.nlpAnnotation.repository.AnnotationRepository;
import com.nlpAnnotation.repository.CoupleTexteRepository;
import com.nlpAnnotation.repository.TacheRepository;
import com.nlpAnnotation.repository.UtilisateurRepository;
import com.nlpAnnotation.services.observer.AvancementObserver;
import com.nlpAnnotation.services.observer.StatistiquesObserver;

@Service
public class TacheService {
    private final TacheRepository tacheRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final CoupleTexteRepository coupleTexteRepository;
    private final AnnotationRepository annotationRepository;
    private List<AvancementObserver> observers = new ArrayList<>();
    public TacheService(TacheRepository tacheRepository,UtilisateurRepository utilisateurRepository,CoupleTexteRepository coupleTexteRepository,AnnotationRepository annotationRepository) {
    	this.tacheRepository = tacheRepository;
    	this.utilisateurRepository = utilisateurRepository;
    	this.coupleTexteRepository = coupleTexteRepository;
    	this.annotationRepository = annotationRepository;
    	// Enregistrement de l'observateur au démarrage
        observers.add(new StatistiquesObserver());
    }

    public void ajouterObserver(AvancementObserver o) {
        observers.add(o);
    }

    public void affecterAnnotateurs(Dataset dataset,List<Integer> annotateurIds,LocalDate deadline) {

        List<CoupleTexte> tousLesCouples =  coupleTexteRepository.findByDatasetId(dataset.getId());

        int nbAnnotateurs = annotateurIds.size();
        if (nbAnnotateurs == 0) return;

        // Répartition équitable des couples entre annotateurs
        int taillePart = tousLesCouples.size() / nbAnnotateurs;

        for (int i = 0; i < nbAnnotateurs; i++) {
        	final int annotateurId = annotateurIds.get(i);
            Utilisateur annotateur = utilisateurRepository.findById(annotateurId).orElseThrow(() -> new UtilisateurNotFoundException(annotateurId));

            Tache tache = new Tache(deadline, dataset, annotateur);
            tacheRepository.save(tache);
        }
    }

    public void mettreAJourAvancement(int tacheId) {
        Tache tache = tacheRepository.findById(tacheId).orElseThrow(() -> new TacheNotFoundException(tacheId));

        int totalCouples = coupleTexteRepository.countByDatasetId(tache.getDataset().getId());

        int nbAnnotes = annotationRepository
            .countByAnnotateurIdAndDatasetId(
                tache.getAnnotateur().getId(),
                tache.getDataset().getId());

        int pourcentage = totalCouples > 0
            ? (nbAnnotes * 100) / totalCouples : 0;

        tache.setAvancement(pourcentage);
        tacheRepository.save(tache);

        // Notifier tous les observateurs (Pattern Observer)
        for (AvancementObserver o : observers) {
            o.onAvancementChange(tacheId, pourcentage);
        }
    }

    public List<Tache> getTachesAnnotateur(int annotateurId) {
        return tacheRepository.findByAnnotateurId(annotateurId);
    }

    public List<Tache> getTachesDataset(int datasetId) {
        return tacheRepository.findByDatasetId(datasetId);
    }

    public Tache findById(int id) {
        return tacheRepository.findById(id).orElseThrow(() -> new TacheNotFoundException(id));
    }

    public int getAvancementMoyen(int datasetId) {
        Double avg = tacheRepository.getAvancementMoyenDataset(datasetId);
        return avg != null ? avg.intValue() : 0;
    }
}
