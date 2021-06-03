package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.PremierLeague.model.Event.EventType;

public class Simulator {
	private Model model;
	public Simulator(Model m) {
		this.model = m;
	}
	
	//coda degli eventi
	private PriorityQueue<Event> queue;
	
	//parametri di input
	private int nReporter;
	private int soglia;
	
	//modello del mondo
	private Graph<Team, DefaultWeightedEdge> grafo;
	private Map<Team, List<Match>> teamMatches;
	private Map<Team, Integer> reporterPerTeam;
	private int nPartite;
	
	//misure di output
	private double reporterMediPerPartita;
	private int nPartiteSottoSoglia;
	
	public void init(int N, int X, Graph<Team, DefaultWeightedEdge> g) {
		this.grafo = g;
		this.nReporter = N;
		this.soglia = X;
		
		//stato iniziale
		this.teamMatches = new HashMap<Team, List<Match>>();
		this.model.setTeamMatches(this.teamMatches);
		this.reporterPerTeam = new HashMap<Team, Integer>();
		this.reporterMediPerPartita = 0;
		this.nPartiteSottoSoglia = 0;
		this.nPartite = 0;
		
		this.queue = new PriorityQueue<Event>();
		
		for(Team t : this.grafo.vertexSet()) {
			this.queue.add(new Event(this.teamMatches.get(t).get(0).getDate(), this.teamMatches.get(t).get(0), EventType.PRIMA_PARTITA, t, this.nReporter*2));
			this.reporterPerTeam.put(t, this.nReporter);
		}
	}
	
	public void run() {
		int i = 0;
		while(this.queue.size() > 0) {
			Event e = this.queue.poll();
			this.processEvent(e, i);
			i++;
			System.out.println(e);
		}
	}

	private void processEvent(Event e, int T) {
		Match match = e.getMatch();
		int risultato = match.getResultOfTeamHome();
	
		if(e.getN() < this.soglia)
			this.nPartiteSottoSoglia++;
		
		if(e.getN() == 0)
			return;
		
		this.reporterMediPerPartita += e.getN();
		this.nPartite++;
		
		Team home = this.model.idMap.get(match.getTeamHomeID());
		Team away = this.model.idMap.get(match.getTeamAwayID());
		
		if(risultato == 1) {	//vittoria casa
			if(match.getTeamHomeID() == e.getTeam().getTeamID()) {	//seguo casa
				int p = (int)(Math.random()*100);
				if(p < 50) {
					//promozione
					Set<Team> migliori = this.model.getMigliori(e.getTeam()).keySet();
					List<Team> best = new ArrayList<Team>(migliori);
					int nuovaSquadra = (int)(Math.random()*best.size());
					Team newTeam = best.get(nuovaSquadra);
					
					int nuovoN = (this.reporterPerTeam.get(newTeam)+1) + ;
					
					Event nuovoEvento = new Event(this.teamMatches.get(newTeam).get(T+1).getDate(), this.teamMatches.get(newTeam).get(T+1), EventType.PROMOZIONE, newTeam, nuovoN);
					this.reporterPerTeam.replace(e.getTeam(), (this.reporterPerTeam.get(e.getTeam())-1));
					this.queue.add(nuovoEvento);
				}
				else {
					//invariato
					Event nuovoEvento = new Event(this.teamMatches.get(e.getTeam()).get(T+1).getDate(), this.teamMatches.get(e.getTeam()).get(T+1), EventType.INVARIATO, e.getTeam(), this.reporterPerTeam.get(e.getTeam()));
					this.queue.add(nuovoEvento);
				}

				//bocciatura per l'altra squadra
				int pp = (int)(Math.random()*100);
				if(pp < 20) {
					int nReporterBocciati = (int)(Math.random()*this.reporterPerTeam.get(away));
					Set<Team> peggiori = this.model.getMigliori(away).keySet();
					List<Team> worst = new ArrayList<Team>(peggiori);
					int nuovaSquadra = (int)(Math.random()*worst.size());
					Team newTeam = worst.get(nuovaSquadra);
					
					Event nuovoEvento = new Event(this.teamMatches.get(newTeam).get(T+1).getDate(), this.teamMatches.get(newTeam).get(T+1), EventType.BOCCIATURA, newTeam, (this.reporterPerTeam.get(newTeam)+nReporterBocciati));
					this.reporterPerTeam.replace(away, (this.reporterPerTeam.get(away)-nReporterBocciati));
					this.queue.add(nuovoEvento);
				}
				else {
					//invariato
					Event nuovoEvento = new Event(this.teamMatches.get(away).get(T+1).getDate(), this.teamMatches.get(away).get(T+1), EventType.INVARIATO, away, this.reporterPerTeam.get(away));
					this.queue.add(nuovoEvento);
				}
			}
			else if(match.getTeamAwayID() == e.getTeam().getTeamID()) {	//seguo trasferta
				//bocciatura
				int pp = (int)(Math.random()*100);
				if(pp < 20) {
					int nReporterBocciati = (int)(Math.random()*this.reporterPerTeam.get(e.getTeam()));
					Set<Team> peggiori = this.model.getMigliori(e.getTeam()).keySet();
					List<Team> worst = new ArrayList<Team>(peggiori);
					int nuovaSquadra = (int)(Math.random()*worst.size());
					Team newTeam = worst.get(nuovaSquadra);
					
					Event nuovoEvento = new Event(this.teamMatches.get(newTeam).get(T+1).getDate(), this.teamMatches.get(newTeam).get(T+1), EventType.BOCCIATURA, newTeam, (this.reporterPerTeam.get(newTeam)+nReporterBocciati));
					this.reporterPerTeam.replace(e.getTeam(), (this.reporterPerTeam.get(e.getTeam())-nReporterBocciati));
					this.queue.add(nuovoEvento);
				}
				else {
					//invariato
					Event nuovoEvento = new Event(this.teamMatches.get(e.getTeam()).get(T+1).getDate(), this.teamMatches.get(e.getTeam()).get(T+1), EventType.INVARIATO, e.getTeam(), this.reporterPerTeam.get(e.getTeam()));
					this.queue.add(nuovoEvento);
				}
				
				//promozione per l'altra squadra
				Team home = this.model.idMap.get(match.getTeamHomeID());
				int p = (int)(Math.random()*100);
				if(p < 50) {
					Set<Team> migliori = this.model.getMigliori(home).keySet();
					List<Team> best = new ArrayList<Team>(migliori);
					int nuovaSquadra = (int)(Math.random()*best.size());
					Team newTeam = best.get(nuovaSquadra);
					
					Event nuovoEvento = new Event(this.teamMatches.get(newTeam).get(T+1).getDate(), this.teamMatches.get(newTeam).get(T+1), EventType.PROMOZIONE, newTeam, (this.reporterPerTeam.get(newTeam)+1));
					this.reporterPerTeam.replace(home, (this.reporterPerTeam.get(home)-1));
					this.queue.add(nuovoEvento);
				}
				else {
					//invariato
					Event nuovoEvento = new Event(this.teamMatches.get(home).get(T+1).getDate(), this.teamMatches.get(home).get(T+1), EventType.INVARIATO, home, this.reporterPerTeam.get(home));
					this.queue.add(nuovoEvento);
				}
			}
		}
		
		if(risultato == 0) {	//pareggio
			//tutto invariato
			Event nuovoEvento = new Event(this.teamMatches.get(home).get(T+1).getDate(), this.teamMatches.get(home).get(T+1), EventType.INVARIATO, home, this.reporterPerTeam.get(home));
			this.queue.add(nuovoEvento);
			
			Event nuovoEventoo = new Event(this.teamMatches.get(away).get(T+1).getDate(), this.teamMatches.get(away).get(T+1), EventType.INVARIATO, away, this.reporterPerTeam.get(away));
			this.queue.add(nuovoEventoo);
		}
	}

	public double getReporterMediPerPartita() {
		return reporterMediPerPartita/this.nPartite;
	}

	public int getnPartiteSottoSoglia() {
		return nPartiteSottoSoglia;
	}
}
