package ui.controller;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class LabelMap 
{
	
	private Map<String,String> basicAnalysisMap = new HashMap<>();
	private Map<String,String> additionalAnalysisMap = new HashMap<>();
	
	public LabelMap() {
        loadLabels("labels.properties");
    }

    private void loadLabels(String filename) {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(filename)) {
            Properties prop = new Properties();
            prop.load(input);

            prop.forEach((key, value) -> {
                String keyStr = (String) key;
                String valueStr = (String) value;

                if (isBasicAnalysisKey(keyStr)) {
                    basicAnalysisMap.put(keyStr, valueStr);
                } else {
                    additionalAnalysisMap.put(keyStr, valueStr);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private boolean isBasicAnalysisKey(String key) {
        return key.matches("\\d+") && Integer.parseInt(key) <= 7;
    }

	public Map<String, String> getBasicAnalysisMap() {
		return basicAnalysisMap;
	}
	
	public String getBasicAnalysisByID(String key) {
		return basicAnalysisMap.get(key);
	}


	public Map<String, String> getAdditionalAnalysisMap() {
		return additionalAnalysisMap;
	}
	
	
	public String getAdditionalAnalysisByID(String key) {
		return additionalAnalysisMap.get(key);
	}
	
	
	public void setAdditionalAnalysis11(int n) {
		additionalAnalysisMap.put("11", "Les classes qui possèdent plus de "+n+" méthodes");;
	}
	
	
}
