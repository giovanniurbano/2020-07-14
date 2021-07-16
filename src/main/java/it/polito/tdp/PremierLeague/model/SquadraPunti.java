package it.polito.tdp.PremierLeague.model;

public class SquadraPunti implements Comparable<SquadraPunti> {
	private Team t;
	private Integer punti;
	
	public SquadraPunti(Team t, Integer punti) {
		super();
		this.t = t;
		this.punti = punti;
	}
	public Team getT() {
		return t;
	}
	public void setT(Team t) {
		this.t = t;
	}
	public Integer getPunti() {
		return punti;
	}
	public void setPunti(Integer punti) {
		this.punti = punti;
	}
	@Override
	public int compareTo(SquadraPunti o) {
		return this.punti.compareTo(o.punti);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((t == null) ? 0 : t.hashCode());
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
		SquadraPunti other = (SquadraPunti) obj;
		if (t == null) {
			if (other.t != null)
				return false;
		} else if (!t.equals(other.t))
			return false;
		return true;
	}
	
	
}
