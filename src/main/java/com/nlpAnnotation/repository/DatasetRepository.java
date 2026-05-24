package com.nlpAnnotation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nlpAnnotation.models.Dataset;

@Repository
public interface DatasetRepository extends JpaRepository<Dataset,Integer>{
	 // Chercher un dataset par son nom
    Dataset findByNomDataset(String nomDataset);

    // Vérifier si un dataset existe déjà avec ce nom
    boolean existsByNomDataset(String nomDataset);

    // Compter le nombre total de couples d'un dataset (taille)
    @Query("SELECT COUNT(c) FROM CoupleTexte c WHERE c.dataset.id = :datasetId")
    Long countCouplesByDatasetId(@Param("datasetId") int datasetId);
}
