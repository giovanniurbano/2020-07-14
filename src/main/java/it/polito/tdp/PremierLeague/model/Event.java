package it.polito.tdp.PremierLeague.model;

public class Event implements Comparable<Event> {
	private int T;
	private Team team;
	
	@Override
	public String toString() {
		return "Event [T=" + T + ", team=" + team + "]";
	}

	public int getT() {
		return T;
	}

	public void setT(int t) {
		T = t;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public Event(int t, Team team) {
		super();
		T = t;
		this.team = team;
	}

	@Override
	public int compareTo(Event o) {
		return this.T-o.T;
	}
	
	
}