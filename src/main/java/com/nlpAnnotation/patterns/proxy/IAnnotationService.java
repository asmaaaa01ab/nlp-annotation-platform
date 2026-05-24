package com.nlpAnnotation.patterns.proxy;

import java.util.List;

import com.nlpAnnotation.models.Annotation;

public interface IAnnotationService {
	void annoter(int coupleId, String classeChoisie, int annotateurId) throws Exception; 
	List<Annotation> getAnnotations(int datasetId);
}
