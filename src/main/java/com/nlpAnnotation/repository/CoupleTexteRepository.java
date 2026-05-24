package com.nlpAnnotation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nlpAnnotation.models.CoupleTexte;
import com.nlpAnnotation.models.Dataset;

@Repository
public interface CoupleTexteRepository extends JpaRepository<CoupleTexte,Integer>{
	List<CoupleTexte> findByDatasetId(int datasetId);
	int countByDatasetId(int datasetId);
}
