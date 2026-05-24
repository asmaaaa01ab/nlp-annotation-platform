package com.nlpAnnotation.patterns.builder;

import java.util.ArrayList;
import java.util.List;

import com.nlpAnnotation.models.Dataset;

public class DatasetBuilder {
	private String nom;
    private String description = "";
    private List<String> classes = new ArrayList<>();

    public DatasetBuilder(String nom) {
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom du dataset est obligatoire !");
        }
        this.nom = nom;
    }

    public DatasetBuilder description(String description) {
        this.description = description;
        return this;
    }

    public DatasetBuilder ajouterClasse(String classe) {
        if (classe != null && !classe.trim().isEmpty()) {
            this.classes.add(classe.trim());
        }
        return this;
    }

    public List<String> getClasses() {
        return this.classes;
    }

    public Dataset build() {
        if (classes.isEmpty()) {
            throw new IllegalStateException("Un dataset doit avoir au moins une classe !");
        }
        Dataset d = new Dataset();
        d.setNomDataset(this.nom);
        d.setDescription(this.description);
        return d;
    }
}
