package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	private PremierLeagueDAO dao;
	private Graph<Team, DefaultWeightedEdge> grafo;
	Map<Integer, Team> idMap;
	private Map<Integer, Integer> classifica;
	
	public Model() {
		this.dao = new PremierLeagueDAO();
		this.idMap = new HashMap<Integer, Team>();
		this.dao.listAllTeams(idMap);
		this.classifica = new HashMap<Integer, Integer>();
		this.setClassifica(classifica);
	}
	
	public String creaGrafo() {
		this.grafo = new SimpleDirectedWeightedGraph<Team, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		//vertici
		Graphs.addAllVertices(this.grafo, this.idMap.values());
		
		//archi
		for(Integer team1 : classifica.keySet())
			for(Integer team2 : classifica.keySet())
				if(!this.grafo.containsEdge(this.idMap.get(team1), this.idMap.get(team2)) && !this.grafo.containsEdge(this.idMap.get(team2), this.idMap.get(team1))) {
					double peso = classifica.get(team1) - classifica.get(team2);
					if(peso > 0) 
						Graphs.addEdgeWithVertices(this.grafo, this.idMap.get(team1), this.idMap.get(team2), peso);
					else if(peso < 0)
						Graphs.addEdgeWithVertices(this.grafo, this.idMap.get(team2), this.idMap.get(team1), (peso*-1));
				}
		return String.format("Grafo creato con %d vertici e %d archi\n", this.grafo.vertexSet().size(), this.grafo.edgeSet().size());
	}
	
	public void setClassifica(Map<Integer, Integer> classifica) {
		List<Match> matches = this.dao.listAllMatches();
		for(Match m : matches) {
			if(!classifica.containsKey(m.teamHomeID))
				classifica.put(m.teamHomeID, 0);
			if(!classifica.containsKey(m.teamAwayID))
				classifica.put(m.teamAwayID, 0);
			
			if(m.resultOfTeamHome == 1)
				classifica.replace(m.teamHomeID, classifica.get(m.teamHomeID)+3);
			else if(m.resultOfTeamHome == -1)
				classifica.replace(m.teamAwayID, classifica.get(m.teamAwayID)+3);
			else {
				classifica.replace(m.teamHomeID, classifica.get(m.teamHomeID)+1);
				classifica.replace(m.teamAwayID, classifica.get(m.teamAwayID)+1);
			}
		}
	}
	
	public Set<Team> getVertici(){
		return this.grafo.vertexSet();
	}
	
	public LinkedHashMap<Team, Integer> getMigliori(Team s) {
		Map<Team, Integer> migliori = new HashMap<Team, Integer>();
		Integer puntiS = this.classifica.get(s.teamID);
		
		for(Integer teamID : this.classifica.keySet()) {
			if(this.classifica.get(teamID) > puntiS)
				migliori.put(this.idMap.get(teamID), (this.classifica.get(teamID)-puntiS));
		}
		
		return this.sortHashMapByValues(migliori);
	}
	
	public LinkedHashMap<Team, Integer> getPeggiori(Team s) {
		Map<Team, Integer> peggiori = new HashMap<Team, Integer>();
		Integer puntiS = this.classifica.get(s.teamID);
		
		for(Integer teamID : this.classifica.keySet()) {
			if(this.classifica.get(teamID) < puntiS)
				peggiori.put(this.idMap.get(teamID), (puntiS-this.classifica.get(teamID)));
		}
		
		return this.sortHashMapByValues(peggiori);
	}
	
	public Graph<Team, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}

	public LinkedHashMap<Team, Integer> sortHashMapByValues(Map<Team, Integer> migliori) {
	    List<Team> mapKeys = new ArrayList<>(migliori.keySet());
	    List<Integer> mapValues = new ArrayList<>(migliori.values());
	    Collections.sort(mapValues);
	    Collections.sort(mapKeys);

	    LinkedHashMap<Team, Integer> sortedMap = new LinkedHashMap<>();

	    Iterator<Integer> valueIt = mapValues.iterator();
	    while (valueIt.hasNext()) {
	        Integer val = valueIt.next();
	        Iterator<Team> keyIt = mapKeys.iterator();

	        while (keyIt.hasNext()) {
	            Team key = keyIt.next();
	            Integer comp1 = migliori.get(key);
	            Integer comp2 = val;

	            if (comp1.equals(comp2)) {
	                keyIt.remove();
	                sortedMap.put(key, val);
	                break;
	            }
	        }
	    }
	    return sortedMap;
	}
	
	public void setTeamMatches(Map<Team, List<Match>> map) {
		for(Integer teamID : this.idMap.keySet()) {
			map.put(this.idMap.get(teamID), this.dao.getMatchesByTeam(teamID));
		}
	}
}
