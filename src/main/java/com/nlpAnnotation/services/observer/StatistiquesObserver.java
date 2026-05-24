package com.nlpAnnotation.services.observer;

public class StatistiquesObserver implements AvancementObserver{
	@Override
    public void onAvancementChange(int tacheId, int pourcentage) {
        System.out.println("[Observer] Tâche " + tacheId 
            + " — avancement mis à jour : " + pourcentage + "%");

        if (pourcentage == 100) {
            System.out.println("[Observer] Tâche " + tacheId 
                + " est terminée !");
        }
    }
}
