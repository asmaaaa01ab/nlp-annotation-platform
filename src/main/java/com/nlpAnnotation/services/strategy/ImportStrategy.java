package com.nlpAnnotation.services.strategy;

import java.io.InputStream;
import java.util.List;

import com.nlpAnnotation.models.CoupleTexte;

public interface ImportStrategy {
	List<CoupleTexte> importer(InputStream flux) throws Exception;
}
