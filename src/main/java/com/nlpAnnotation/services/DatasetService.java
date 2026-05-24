package com.nlpAnnotation.services;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.nlpAnnotation.exceptions.DatasetNotFoundException;
import com.nlpAnnotation.models.ClassePossible;
import com.nlpAnnotation.models.CoupleTexte;
import com.nlpAnnotation.models.Dataset;
import com.nlpAnnotation.patterns.builder.DatasetBuilder;
import com.nlpAnnotation.repository.ClassePossibleRepository;
import com.nlpAnnotation.repository.CoupleTexteRepository;
import com.nlpAnnotation.repository.DatasetRepository;
import com.nlpAnnotation.services.strategy.CSVImportStrategy;
import com.nlpAnnotation.services.strategy.ImportStrategy;
import com.nlpAnnotation.services.strategy.JSONImportStrategy;

@Service
public class DatasetService {
    private final DatasetRepository datasetRepository;
    private final CoupleTexteRepository coupleTexteRepository;
    private final ClassePossibleRepository classePossibleRepository;
    public DatasetService(DatasetRepository datasetRepository,CoupleTexteRepository coupleTexteRepository,ClassePossibleRepository classePossibleRepository) {
    	this.datasetRepository = datasetRepository;
    	this.coupleTexteRepository = coupleTexteRepository;
    	this.classePossibleRepository = classePossibleRepository;
    }
    public Dataset creerDataset(String nom, String description,String classesStr, MultipartFile fichier) throws Exception {

        // Pattern Builder — construction propre de l'objet Dataset
        DatasetBuilder builder = new DatasetBuilder(nom)
            .description(description);

        // Parsing des classes séparées par ";"
        String[] classes = classesStr.split(";");
        for (String c : classes) {
            builder.ajouterClasse(c.trim());
        }

        Dataset dataset = builder.build();
        datasetRepository.save(dataset);

        // Sauvegarde des classes possibles
        for (String c : classes) {
            ClassePossible cp = new ClassePossible(c.trim(), dataset);
            classePossibleRepository.save(cp);
        }

        // Pattern Strategy — import CSV ou JSON selon le fichier
        importerFichier(fichier, dataset);

        return dataset;
    }

    public void importerFichier(MultipartFile fichier, Dataset dataset)
                                 throws Exception {
        String nomFichier = fichier.getOriginalFilename();
        ImportStrategy strategy;

        if (nomFichier != null && nomFichier.endsWith(".csv")) {
            strategy = new CSVImportStrategy();
        } else if (nomFichier != null && nomFichier.endsWith(".json")) {
            strategy = new JSONImportStrategy();
        } else {
            throw new IllegalArgumentException(
                "Format non supporté ! Utilisez CSV ou JSON.");
        }

        List<CoupleTexte> couples = strategy.importer(fichier.getInputStream());

        // Associer chaque couple au dataset
        for (CoupleTexte c : couples) {
            c.setDataset(dataset);
        }

        // Mélanger (shuffle) avant de sauvegarder
        Collections.shuffle(couples);
        coupleTexteRepository.saveAll(couples);
    }

    public List<Dataset> getTousLesDatasets() {
        return datasetRepository.findAll();
    }

    public Dataset findById(int id) {
        return datasetRepository.findById(id).orElseThrow(() -> new DatasetNotFoundException(id));
    }

    public int getAvancementDataset(int datasetId) {
    	Long count = datasetRepository.countCouplesByDatasetId(datasetId);
        return count != null ? count.intValue() : 0;
    }

    public int getTailleDataset(int datasetId) {
        return coupleTexteRepository.countByDatasetId(datasetId);
    }

    public List<ClassePossible> getClassesDataset(int datasetId) {
        return classePossibleRepository.findByDatasetId(datasetId);
    }

    public List<CoupleTexte> getCouplesDataset(int datasetId) {
        return coupleTexteRepository.findByDatasetId(datasetId);
    }
}
