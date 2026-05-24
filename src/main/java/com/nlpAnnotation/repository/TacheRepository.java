package com.nlpAnnotation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nlpAnnotation.models.Tache;

@Repository
public interface TacheRepository extends JpaRepository<Tache,Integer>{
	// Toutes les tâches d'un annotateur (pour son tableau de bord)
    List<Tache> findByAnnotateurId(int annotateurId);

    // Toutes les tâches d'un dataset (pour l'admin)
    List<Tache> findByDatasetId(int datasetId);

    // Tâches non terminées d'un annotateur
    List<Tache> findByAnnotateurIdAndAvancementLessThan(int annotateurId, int seuil);

    // Avancement moyen global d'un dataset
    @Query("SELECT AVG(t.avancement) FROM Tache t WHERE t.dataset.id = :datasetId")
    Double getAvancementMoyenDataset(@Param("datasetId") int datasetId);
}
