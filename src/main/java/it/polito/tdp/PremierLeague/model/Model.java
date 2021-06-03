package it.polito.tdp.PremierLeague.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	private PremierLeagueDAO dao;
	private Graph<Team, DefaultWeightedEdge> grafo;
	private Map<Integer, Team> idMap;
	
	public Model() {
		this.dao = new PremierLeagueDAO();
		this.idMap = new HashMap<Integer, Team>();
		this.dao.listAllTeams(idMap);
	}
	
	public String creaGrafo() {
		this.grafo = new SimpleDirectedWeightedGraph<Team, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		//vertici
		Graphs.addAllVertices(this.grafo, this.idMap.values());
		
		//archi
		Map<Integer, Integer> classifica = new HashMap<Integer, Integer>();
		this.setClassifica(classifica);
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
}
