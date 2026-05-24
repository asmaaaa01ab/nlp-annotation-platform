package com.nlpAnnotation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nlpAnnotation.models.ClassePossible;
import com.nlpAnnotation.models.Dataset;

@Repository
public interface ClassePossibleRepository extends JpaRepository<ClassePossible,Integer>{
	List<ClassePossible> findByDatasetId(int datasetId);
}
