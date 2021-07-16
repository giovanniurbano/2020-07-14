package it.polito.tdp.PremierLeague.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.PremierLeague.model.Action;
import it.polito.tdp.PremierLeague.model.Match;
import it.polito.tdp.PremierLeague.model.Player;
import it.polito.tdp.PremierLeague.model.SquadraPunti;
import it.polito.tdp.PremierLeague.model.Team;

public class PremierLeagueDAO {
	
	public List<Player> listAllPlayers(){
		String sql = "SELECT * FROM Players";
		List<Player> result = new ArrayList<Player>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Player player = new Player(res.getInt("PlayerID"), res.getString("Name"));
				
				result.add(player);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void listAllTeams(Map<Integer, Team> map){
		String sql = "SELECT * FROM Teams";
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				Team team = new Team(res.getInt("TeamID"), res.getString("Name"));
				map.put(team.getTeamID(), team);
			}
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<Action> listAllActions(){
		String sql = "SELECT * FROM Actions";
		List<Action> result = new ArrayList<Action>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Action action = new Action(res.getInt("PlayerID"),res.getInt("MatchID"),res.getInt("TeamID"),res.getInt("Starts"),res.getInt("Goals"),
						res.getInt("TimePlayed"),res.getInt("RedCards"),res.getInt("YellowCards"),res.getInt("TotalSuccessfulPassesAll"),res.getInt("totalUnsuccessfulPassesAll"),
						res.getInt("Assists"),res.getInt("TotalFoulsConceded"),res.getInt("Offsides"));
				
				result.add(action);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Match> listAllMatches(){
		String sql = "SELECT * "
				+ "FROM matches m "
				+ "ORDER BY Date ";
		List<Match> result = new ArrayList<Match>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				
				Match match = new Match(res.getInt("m.MatchID"), res.getInt("m.TeamHomeID"), res.getInt("m.TeamAwayID"), res.getInt("m.teamHomeFormation"), 
							res.getInt("m.teamAwayFormation"),res.getInt("m.resultOfTeamHome"), res.getTimestamp("m.date").toLocalDateTime(), null, null);
				result.add(match);

			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<SquadraPunti> getSquadrePunti(Map<Integer, Team> map) {
		String sql = "SELECT TeamHomeID AS h, TeamAwayID AS a, ResultOfTeamHome AS r "
				+ "FROM matches ";
		Map<Integer, Integer> sp = new HashMap<Integer, Integer>();
		Connection conn = DBConnect.getConnection();
		
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				int hId = res.getInt("h");
				int aId = res.getInt("a");
				int risultato = res.getInt("r");
				
				if(risultato == 1) {
					if(sp.containsKey(hId)) 
						sp.replace(hId, (sp.get(hId)+3));	
					else
						sp.put(hId, 3);
					
					if(!sp.containsKey(aId)) 
						sp.put(aId, 0);
				}
				else if(risultato == 0) {
					if(sp.containsKey(hId)) 
						sp.replace(hId, (sp.get(hId)+1));	
					else
						sp.put(hId, 1);
					
					if(sp.containsKey(aId)) 
						sp.replace(aId, (sp.get(aId)+1));	
					else
						sp.put(aId, 1);
				}
				else {
					if(sp.containsKey(aId)) 
						sp.replace(aId, (sp.get(aId)+3));	
					else
						sp.put(aId, 3);
					
					if(!sp.containsKey(hId)) 
						sp.put(hId, 0);
				}
			}
			conn.close();
			
			List<SquadraPunti> result = new ArrayList<>();
			for(Integer id : sp.keySet()) {
				result.add(new SquadraPunti(map.get(id), sp.get(id)));
			}
			
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
