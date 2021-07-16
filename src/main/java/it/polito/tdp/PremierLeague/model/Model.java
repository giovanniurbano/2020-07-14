package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
	private List<SquadraPunti> classifica;
	
	private Simulator sim;
	
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
		classifica = this.dao.getSquadrePunti(idMap);
		for(SquadraPunti sp1 : classifica) {
			for(SquadraPunti sp2 : classifica) {
				if(!sp1.equals(sp2) && sp1.getPunti() > sp2.getPunti()) {
					Graphs.addEdge(this.grafo, sp1.getT(), sp2.getT(), (sp1.getPunti()-sp2.getPunti()));
				}
			}
		}
		
		return String.format("Grafo creato con %d vertici e %d archi\n", this.grafo.vertexSet().size(), this.grafo.edgeSet().size());
	}
	
	public Collection<Team> getVertici() {
		return this.idMap.values();
	}

	public List<SquadraPunti> miglioriDi(Team t) {
		List<SquadraPunti> m = new ArrayList<SquadraPunti>();
		for(DefaultWeightedEdge arco : this.grafo.incomingEdgesOf(t)) {
			SquadraPunti s = new SquadraPunti(this.grafo.getEdgeSource(arco), (int)this.grafo.getEdgeWeight(arco));
			m.add(s);
		}
		Collections.sort(m);
		return m;
	}

	public List<SquadraPunti> peggioriDi(Team t) {
		List<SquadraPunti> p = new ArrayList<SquadraPunti>();
		for(DefaultWeightedEdge arco : this.grafo.outgoingEdgesOf(t)) {
			SquadraPunti s = new SquadraPunti(this.grafo.getEdgeTarget(arco), (int)this.grafo.getEdgeWeight(arco));
			p.add(s);
		}
		Collections.sort(p);
		return p;
	}
	
	public List<Match> listAllMatches() {
		return this.dao.listAllMatches();
	}

	public Graph<Team, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}
	
	public Map<Integer, Team> getIdMap() {
		return idMap;
	}

	public void init(int n, int x) {
		this.sim = new Simulator(n, x, this);
	}
	
	public double getRepMedi() {
		return this.sim.getRepMedi();
	}
	
	public int getPartiteSottoX() {
		return this.sim.getPartiteSottoX();
	}
}
