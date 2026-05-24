package com.nlpAnnotation.services.strategy;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.nlpAnnotation.models.CoupleTexte;

public class CSVImportStrategy implements ImportStrategy{
	@Override
    public List<CoupleTexte> importer(InputStream flux) throws Exception {
        List<CoupleTexte> couples = new ArrayList<>();
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(flux, "UTF-8"));

        String ligne;
        boolean premiereLigne = true;

        while ((ligne = reader.readLine()) != null) {
            // Ignorer la ligne d'entête
            if (premiereLigne) {
                premiereLigne = false;
                continue;
            }

            String[] cols = ligne.split(",");
            if (cols.length >= 2) {
                CoupleTexte c = new CoupleTexte();
                c.setTexte1(cols[0].trim());
                c.setTexte2(cols[1].trim());
                couples.add(c);
            }
        }
        reader.close();
        return couples;
    }
}
