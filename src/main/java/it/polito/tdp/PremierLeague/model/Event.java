package it.polito.tdp.PremierLeague.model;

import java.time.LocalDateTime;

public class Event implements Comparable<Event>{
	public enum EventType {
		PRIMA_PARTITA,
		PROMOZIONE,
		BOCCIATURA,
		INVARIATO
	}
	
	private LocalDateTime t;
	private EventType type;
	private Team team;
	private int n;
	private Match match;
	
	public Event(LocalDateTime t, Match match, EventType type, Team team, int n) {
		this.t = t;
		this.match = match;
		this.type = type;
		this.team = team;
		this.n = n;
	}

	public Match getMatch() {
		return match;
	}

	public void setMatch(Match match) {
		this.match = match;
	}

	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
	}

	public LocalDateTime getT() {
		return t;
	}

	public void setT(LocalDateTime t) {
		this.t = t;
	}

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	@Override
	public int compareTo(Event o) {
		return this.t.compareTo(o.t);
	}

	@Override
	public String toString() {
		return "Event [t=" + t + ", type=" + type + ", team=" + team + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((match == null) ? 0 : match.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Event other = (Event) obj;
		if (match == null) {
			if (other.match != null)
				return false;
		} else if (!match.equals(other.match))
			return false;
		return true;
	}
	
}
