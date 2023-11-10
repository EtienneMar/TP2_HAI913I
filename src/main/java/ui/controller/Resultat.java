package ui.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Resultat {
	private Map<String, Integer> resultats = new HashMap<>();
	
	public Resultat() {
		super();
	}

	public Map<String, Integer> getResultats() {
		return resultats;
	}

	public void addResultat(String key, int val) {

		 this.resultats.put(key, val);
	}
	
	public void addAllResultat(Map<String, Integer> res) {
		this.resultats.putAll(res);;
	}


	
	public int getResultatByKey(String key) {
		return resultats.get(key);
	}

	
	
	public Set<String> getKeys() {
		return resultats.keySet();
	}
	

}
