package com.nlpAnnotation.controllers;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nlpAnnotation.models.CoupleTexte;
import com.nlpAnnotation.models.Tache;
import com.nlpAnnotation.models.Utilisateur;
import com.nlpAnnotation.services.AnnotationService;
import com.nlpAnnotation.services.DatasetService;
import com.nlpAnnotation.services.TacheService;
import com.nlpAnnotation.services.UtilisateurService;

@Controller
@RequestMapping("/annotateur")
public class AnnotateurController {
    private final AnnotationService annotationService;
    private final TacheService tacheService;
    private final UtilisateurService utilisateurService;
    private final DatasetService datasetService;
    public AnnotateurController(AnnotationService annotationService,TacheService tacheService,UtilisateurService utilisateurService,DatasetService datasetService) {
    	this.annotationService = annotationService;
    	this.tacheService = tacheService;
    	this.utilisateurService = utilisateurService;
    	this.datasetService = datasetService;
    }
    @GetMapping("/taches")
    public String listeTaches(Principal principal, Model model) {
        // Principal = l'utilisateur connecté via Spring Security
        Utilisateur utilisateur = utilisateurService.findByLogin(principal.getName());
        List<Tache> taches = tacheService.getTachesAnnotateur(utilisateur.getId());
        Map<Integer, Integer> tailleMap = new HashMap<>();
        for (Tache t : taches) {
            int taille = datasetService.getTailleDataset(t.getDataset().getId());
            tailleMap.put(t.getId(), taille);
        }
        model.addAttribute("tailleMap", tailleMap);
        model.addAttribute("taches", taches);
        return "annotateur/tasks";
    }

    @GetMapping("/tache/{tacheId}/travailler")
    public String travaillerTache(@PathVariable("tacheId") int tacheId, Principal principal, Model model) {
        Utilisateur utilisateur = utilisateurService.findByLogin(principal.getName());
        Tache tache = tacheService.findById(tacheId);
        // Vérifier que la tâche appartient bien à cet annotateur
        if (tache.getAnnotateur().getId() != utilisateur.getId()) {
            return "redirect:/annotateur/taches";
        }
        // Récupérer le couple actuel à annoter
        CoupleTexte couple = annotationService.getCoupleActuel(tacheId, utilisateur.getId());
        // Classes possibles pour ce dataset
        model.addAttribute("tache", tache);
        model.addAttribute("couple", couple);
        model.addAttribute("classes",datasetService.getClassesDataset(tache.getDataset().getId()));
        model.addAttribute("annotateurId", utilisateur.getId());
        return "annotateur/annotate";
    }

    @PostMapping("/tache/{tacheId}/annoter")
    public String annoter(@PathVariable("tacheId") int tacheId,@RequestParam("coupleId") int coupleId,@RequestParam("classe") String classe,Principal principal, Model model) {
        Utilisateur utilisateur = utilisateurService.findByLogin(principal.getName());
        try {
            annotationService.annoter(coupleId, classe,utilisateur.getId());
            // Rediriger vers le couple suivant
            return "redirect:/annotateur/tache/" + tacheId + "/travailler";
        } catch (Exception e) {
        	Tache tache = tacheService.findById(tacheId);
            CoupleTexte couple = annotationService.getCoupleActuel(tacheId, utilisateur.getId()); // ← AJOUTER
            model.addAttribute("erreur", e.getMessage());
            model.addAttribute("tache", tache);
            model.addAttribute("couple", couple); 
            model.addAttribute("classes", datasetService.getClassesDataset(tache.getDataset().getId()));
            return "annotateur/annotate";
        }
    }
}
