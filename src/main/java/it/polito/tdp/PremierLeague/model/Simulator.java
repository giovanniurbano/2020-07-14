package it.polito.tdp.PremierLeague.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

public class Simulator {
	//coda degli eventi
	private PriorityQueue<Event> queue;
	
	//modello del mondo
	private Map<Team, Integer> numReporter; //data una squadra dimmi quanti reporter la seguono
	
	//parametri input
	private int n;
	private int x;
	private Graph<Team, DefaultWeightedEdge> grafo;
	
	private double probProm = 0.5;
	private double probBocc = 0.2;
	
	private List<Match> partite;
	private Map<Integer, Team> idMap;
	private Model model;
	
	//valori output
	private double repMedi;
	private int partiteSottoX;
	
	public Simulator(int n, int x, Model model) {
		this.n = n;
		this.x = x;
		this.model = model;
		this.grafo = model.getGrafo();
		
		this.repMedi = 0.0;
		this.partiteSottoX = 0;
		
		this.partite = model.listAllMatches();
		this.idMap = model.getIdMap();
		
		
		//inizializzo coda eventi
		this.queue = new PriorityQueue<Event>();
		
		//inizializzo il mondo
		this.numReporter = new HashMap<Team, Integer>();
		for(Team t : this.grafo.vertexSet()) {
			this.numReporter.put(t, this.n);
		}
	}
	
	public void run() {
		for(Match match : this.partite) {
			Team h = this.idMap.get(match.getTeamHomeID());
			Team a = this.idMap.get(match.getTeamAwayID());
			
			this.repMedi += this.numReporter.get(h) + this.numReporter.get(a);
			
			if((this.numReporter.get(h) + this.numReporter.get(a)) < this.x)
				this.partiteSottoX++;
			
			if(match.getResultOfTeamHome() == 1) {
				if(this.numReporter.get(h) > 0 && Math.random() < this.probProm) {
					//promozione casa
					this.numReporter.replace(h, this.numReporter.get(h)-1);
					List<SquadraPunti> migliori = this.model.miglioriDi(h);
					Team prossimo = migliori.get((int)Math.round(Math.random()*10)).getT();
					this.numReporter.replace(prossimo, this.numReporter.get(prossimo)+1);
				}
				
				if(this.numReporter.get(a) > 0 && Math.random() < this.probBocc) {
					//bocciatura trasferta
					int nRepBocciati = (int) Math.round(Math.random()*this.numReporter.get(a));
					this.numReporter.replace(a, this.numReporter.get(a)-nRepBocciati);
					List<SquadraPunti> peggiori = this.model.peggioriDi(a);
					Team prossimo = peggiori.get((int)Math.round(Math.random()*10)).getT();
					this.numReporter.replace(prossimo, this.numReporter.get(prossimo)+nRepBocciati);
				}
			}
			else if(match.getResultOfTeamHome() == -1) {
				if(this.numReporter.get(a) > 0 && Math.random() < this.probProm) {
					//promozione trasferta
					this.numReporter.replace(a, this.numReporter.get(a)-1);
					List<SquadraPunti> migliori = this.model.miglioriDi(a);
					Team prossimo = migliori.get((int)Math.round(Math.random()*10)).getT();
					this.numReporter.replace(prossimo, this.numReporter.get(prossimo)+1);
				}
				
				if(this.numReporter.get(h) > 0 && Math.random() < this.probBocc) {
					//bocciatura casa
					int nRepBocciati = (int) Math.round(Math.random()*this.numReporter.get(h));
					this.numReporter.replace(h, this.numReporter.get(h)-nRepBocciati);
					List<SquadraPunti> peggiori = this.model.peggioriDi(h);
					Team prossimo = peggiori.get((int)Math.round(Math.random()*10)).getT();
					this.numReporter.replace(prossimo, this.numReporter.get(prossimo)+nRepBocciati);
				}
			}
		}
	}

	public double getRepMedi() {
		return repMedi/this.partite.size();
	}

	public int getPartiteSottoX() {
		return partiteSottoX;
	}
	
}