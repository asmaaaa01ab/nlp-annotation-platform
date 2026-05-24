package com.nlpAnnotation.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nlpAnnotation.models.Annotation;

@Repository
public interface AnnotationRepository extends JpaRepository<Annotation,Integer>{
	// Toutes les annotations d'un annotateur
    List<Annotation> findByAnnotateurId(int annotateurId);

    // Toutes les annotations d'un couple de textes
    List<Annotation> findByCoupleTexteId(int coupleId);

    // Nombre d'annotations faites par un annotateur sur un dataset
    @Query("SELECT COUNT(a) FROM Annotation a " +
           "WHERE a.annotateur.id = :annotateurId " +
           "AND a.coupleTexte.dataset.id = :datasetId")
    int countByAnnotateurIdAndDatasetId(@Param("annotateurId") int annotateurId,
                                        @Param("datasetId") int datasetId);

    // Pour le pattern Chain — détection spam vitesse
    @Query("SELECT COUNT(a) FROM Annotation a " +
           "WHERE a.annotateur.id = :annotateurId " +
           "AND a.dateAnnotation >= :depuis")
    int countAnnotationsDepuis(@Param("annotateurId") int annotateurId,
                               @Param("depuis") LocalDateTime depuis);

    // Pour le pattern Chain — détection spam répétition
    @Query("SELECT a.classe, COUNT(a) FROM Annotation a " +
           "WHERE a.annotateur.id = :annotateurId " +
           "GROUP BY a.classe ORDER BY COUNT(a) DESC")
    List<Object[]> getDistributionClasses(@Param("annotateurId") int annotateurId);

    // Export — toutes les annotations d'un dataset
    @Query("SELECT a FROM Annotation a " +
           "WHERE a.coupleTexte.dataset.id = :datasetId")
    List<Annotation> findByDatasetId(@Param("datasetId") int datasetId);
}
