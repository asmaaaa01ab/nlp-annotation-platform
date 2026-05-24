package com.nlpAnnotation.patterns.singleton;

public class ApplicationConfig {
	private static ApplicationConfig instance;
    private String appName = "NLPAnnotation";
    private int maxAnnotateursParDataset = 10;
    private ApplicationConfig() {}
    public static ApplicationConfig getInstance() {
        if (instance == null) {
            instance = new ApplicationConfig();
        }
        return instance;
    }
    public String getAppName() { return appName; }
    public int getMaxAnnotateurs() { return maxAnnotateursParDataset; }
}
