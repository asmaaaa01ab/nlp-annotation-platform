package com.nlpAnnotation.controllers;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.nlpAnnotation.models.Dataset;
import com.nlpAnnotation.models.Tache;
import com.nlpAnnotation.models.Utilisateur;
import com.nlpAnnotation.patterns.facade.AdminFacade;
import com.nlpAnnotation.services.AnnotationService;
import com.nlpAnnotation.services.DatasetService;
import com.nlpAnnotation.services.TacheService;
import com.nlpAnnotation.services.UtilisateurService;
import com.nlpAnnotation.services.template.ExportCSV;
import com.nlpAnnotation.services.template.ExportJSON;

import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UtilisateurService utilisateurService;
    private final DatasetService datasetService;
    private final TacheService tacheService;
    private final AnnotationService annotationService;
    private final AdminFacade adminFacade;
    public AdminController(UtilisateurService utilisateurService,DatasetService datasetService,TacheService tacheService,AnnotationService annotationService,AdminFacade adminFacade) {
    	this.utilisateurService = utilisateurService;
    	this.datasetService = datasetService;
    	this.tacheService = tacheService;
    	this.annotationService = annotationService;
    	this.adminFacade = adminFacade;
    }
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        List<Dataset> datasets = datasetService.getTousLesDatasets();
        List<Utilisateur> annotateurs = utilisateurService.getAnnotateursActifs();

        Map<Integer, Integer> avancementMap = new HashMap<>();
        for (Dataset d : datasets) {
            avancementMap.put(d.getId(), tacheService.getAvancementMoyen(d.getId()));
        }

        model.addAttribute("datasets", datasets);
        model.addAttribute("annotateurs", annotateurs);
        model.addAttribute("nbDatasets", datasets.size());
        model.addAttribute("nbAnnotateurs", annotateurs.size());
        model.addAttribute("avancementMap", avancementMap); 
        return "admin/dashboard";
    }

    @GetMapping("/datasets")
    public String listeDatasets(Model model) {
        List<Dataset> datasets = datasetService.getTousLesDatasets();
        // Calcul avancement pour chaque dataset
        for (Dataset d : datasets) {
            int avancement = tacheService.getAvancementMoyen(d.getId());
            model.addAttribute("avancement_" + d.getId(), avancement);
        }
        model.addAttribute("datasets", datasets);
        return "admin/dashboard";
    }

    @GetMapping("/dataset/creer")
    public String afficherFormulaireCreerDataset() {
        return "admin/create-dataset";
    }

    @PostMapping("/dataset/creer")
    public String creerDataset(@RequestParam("nom") String nom, @RequestParam("description") String description, @RequestParam("classes") String classes, @RequestParam("fichier") MultipartFile fichier, Model model) {
        try {
            datasetService.creerDataset(nom, description, classes, fichier);
            return "redirect:/admin/dashboard";
        } catch (Exception e) {
            model.addAttribute("erreur", e.getMessage());
            return "admin/create-dataset";
        }
    }

    @GetMapping("/dataset/{id}/details")
    public String detailsDataset(@PathVariable("id") int id, Model model) {
        Dataset dataset = datasetService.findById(id);
        model.addAttribute("dataset", dataset);
        model.addAttribute("taille", datasetService.getTailleDataset(id));
        model.addAttribute("classes", datasetService.getClassesDataset(id));
        model.addAttribute("couples", datasetService.getCouplesDataset(id));
        model.addAttribute("avancement", tacheService.getAvancementMoyen(id));
        model.addAttribute("annotateurs", tacheService.getTachesDataset(id));
        return "admin/dataset-details";
    }

    @GetMapping("/dataset/{id}/annotateurs")
    public String afficherFormulaireAnnotateurs(@PathVariable("id") int id, Model model) {
        Dataset dataset = datasetService.findById(id);
        List<Utilisateur> annotateurs = utilisateurService.getAnnotateursActifs();
        List<Tache> tachesExistantes = tacheService.getTachesDataset(id);
        model.addAttribute("dataset", dataset);
        model.addAttribute("annotateurs", annotateurs);
        model.addAttribute("tachesExistantes", tachesExistantes);
        return "admin/assign-annotators";
    }

    @PostMapping("/dataset/{id}/annotateurs")
    public String affecterAnnotateurs(@PathVariable("id") int id,@RequestParam("annotateurIds") List<Integer> annotateurIds,@RequestParam("deadline") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate deadline, Model model) {
        try {
            Dataset dataset = datasetService.findById(id);
            tacheService.affecterAnnotateurs(dataset, annotateurIds, deadline);
            return "redirect:/admin/dataset/" + id + "/details";
        } catch (Exception e) {
        		model.addAttribute("erreur", e.getMessage());
            model.addAttribute("dataset", datasetService.findById(id));
            model.addAttribute("annotateurs", utilisateurService.getAnnotateursActifs());
            model.addAttribute("tachesExistantes", tacheService.getTachesDataset(id));
            return "admin/assign-annotators";
        }
    }

    @GetMapping("/dataset/{id}/annotateurs/liste")
    public String listeAnnotateursDataset(@PathVariable("id") int id, Model model) {
        model.addAttribute("dataset", datasetService.findById(id));
        model.addAttribute("taches", tacheService.getTachesDataset(id));
        return "admin/dataset-annotators";
    }

    @GetMapping("/annotateurs")
    public String listeAnnotateurs(Model model) {
        model.addAttribute("annotateurs",utilisateurService.getAnnotateursActifs());
        return "admin/manage-users";
    }

    @GetMapping("/annotateur/creer")
    public String afficherFormulaireCreerAnnotateur() {
        return "admin/create-user";
    }

    @PostMapping("/annotateur/creer")
    public String creerAnnotateur(@RequestParam("nom") String nom,@RequestParam("prenom") String prenom,@RequestParam("login") String login,@RequestParam("email") String email,Model model) {
        try {
            // Vérifier si login existe déjà
            if (utilisateurService.loginExiste(login)) {
                model.addAttribute("erreur", "Ce login existe déjà !");
                return "admin/create-user";
            }
            utilisateurService.creerAnnotateur(nom, prenom, login, email);
            return "redirect:/admin/annotateurs";
        } catch (Exception e) {
            model.addAttribute("erreur", e.getMessage());
            return "admin/create-user";
        }
    }

    @GetMapping("/annotateur/{id}/modifier")
    public String afficherFormulaireModifier(@PathVariable("id") int id, Model model) {
    	model.addAttribute("utilisateur",utilisateurService.findById(id));
        return "admin/edit-user";
    }

    @PostMapping("/annotateur/{id}/modifier")
    public String modifierAnnotateur(@PathVariable("id") int id, @RequestParam("nom") String nom, @RequestParam("prenom") String prenom,@RequestParam("login") String login, Model model) {
        try {
            utilisateurService.modifierUtilisateur(id, nom, prenom, login);
            return "redirect:/admin/annotateurs";
        } catch (Exception e) {
            model.addAttribute("erreur", e.getMessage());
            return "admin/edit-user";
        }
    }

    @PostMapping("/annotateur/{id}/supprimer")
    public String supprimerAnnotateur(@PathVariable("id") int id) {
        utilisateurService.desactiverUtilisateur(id);
        return "redirect:/admin/annotateurs";
    }

    @GetMapping("/dataset/{id}/export/csv")
    public void exporterCsv(@PathVariable("id") int id, HttpServletResponse response)throws Exception {
        response.setContentType("text/csv");
        new ExportCSV(annotationService.getAnnotationRepository()).exporter(id, response);
    }

    @GetMapping("/dataset/{id}/export/json")
    public void exporterJson(@PathVariable("id") int id,jakarta.servlet.http.HttpServletResponse response)throws Exception {
        response.setContentType("application/json");
        new ExportJSON(annotationService.getAnnotationRepository()).exporter(id, response);
    }

}
