package com.nlpAnnotation.services.strategy;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nlpAnnotation.models.CoupleTexte;

public class JSONImportStrategy implements ImportStrategy{
	@Override
    public List<CoupleTexte> importer(InputStream flux) throws Exception {
        List<CoupleTexte> couples = new ArrayList<>();

        // Lire le contenu du flux
        String contenu = new String(flux.readAllBytes(), "UTF-8");
        JSONArray tableau = new JSONArray(contenu);

        for (int i = 0; i < tableau.length(); i++) {
            JSONObject obj = tableau.getJSONObject(i);
            CoupleTexte c = new CoupleTexte();
            c.setTexte1(obj.getString("texte1"));
            c.setTexte2(obj.getString("texte2"));
            couples.add(c);
        }
        return couples;
    }
}
