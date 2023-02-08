package org.lesson.db.nations;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		String url = "jdbc:mysql://localhost:3306/nation";
		String user ="root";
		String psw = "root";
		queryNations(url, user, psw);
		Scanner s = new Scanner(System.in);
		System.out.println("Inserisci il nome o parte di esso per ricercarla");
		String inputCountry = s.nextLine();
		sqlQueryName(url, user, psw, inputCountry);
		System.out.println("------------------------- ");
		System.out.println("Inserisci ID della nazione per visualizzare lingue e ultima statistica sulla popolazione");
		String inputId = s.nextLine();
		sqlQueryID(url, user, psw, inputId);
		s.close();
	}
	
	public static void queryNations(String url, String user, String psw) {
		try(Connection con = DriverManager.getConnection(url,user,psw)) {
			String sql="select c.name as name , c.country_id as id , r.name as regione, c2.name as continente"
					+ "					from countries c "
					+ "					join regions r on r.region_id = c.region_id "
					+ "					join continents c2 on r.continent_id =c2.continent_id "
					+ "					order by name ";
			try(PreparedStatement ps = con.prepareStatement(sql)){
				try(ResultSet rs = ps.executeQuery()){
					while(rs.next()) {
						int idCountry = rs.getInt("id");
						String nameCountry = rs.getString("name");
						String regione = rs.getString("regione");
						String continente = rs.getString("continente");
						System.out.println("ID COUNTRY: " + idCountry + " NAME: " + nameCountry + " REGION: " + regione + " CONTINENT:  " + continente );
					}
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void sqlQueryName(String url , String user, String psw, String input) {
		try(Connection con = DriverManager.getConnection(url,user,psw)) {
			String sql="select c.name as name , c.country_id as id , r.name as regione, c2.name as continente"
					+ "					from countries c "
					+ "					join regions r on r.region_id = c.region_id "
					+ "					join continents c2 on r.continent_id =c2.continent_id "
					+ "					where c.name like ? "
					+ "					order by name ";
			try (PreparedStatement ps=con.prepareStatement(sql)){
				ps.setString(1, "%"+input+"%");
			try(ResultSet rs = ps.executeQuery()){
				while(rs.next()) {
					int idCountry = rs.getInt("id");
					String nameCountry = rs.getString("name");
					String regione = rs.getString("regione");
					String continente = rs.getString("continente");
					System.out.println("ID COUNTRY: " + idCountry + " NAME: " + nameCountry + " REGION: " + regione + " CONTINENT:  " + continente );
				}
			}}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
		
	public static void sqlQueryID(String url, String user, String psw, String inputId ) {
		try(Connection con = DriverManager.getConnection(url,user,psw)) {
			String sql="select c.name as name"
					+ "	from countries c "
					+ "WHERE c.country_id = ? ";	
			try(PreparedStatement ps = con.prepareStatement(sql)){
				ps.setString(1, inputId );
				try(ResultSet rs = ps.executeQuery()){
					while(rs.next()) {
						String nameCountry = rs.getString("name");
						System.out.println("COUNTRY: " + nameCountry );
					}
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		try (Connection connection = DriverManager.getConnection(url, user, psw)) {
			 String langQuery = "SELECT lng.language "
			 		+ "FROM countries c "
			 		+ "JOIN country_languages cl "
			 		+ "ON c.country_id = cl.country_id "
			 		+ "JOIN languages lng "
			 		+ "ON cl.language_id = lng.language_id "
			 		+ "WHERE c.country_id = ? ";
								
			try (PreparedStatement ps = connection.prepareStatement (langQuery)) {
				ps.setString(1, inputId );
				
				try (ResultSet rs = ps.executeQuery()) {
					System.out.print("Lingue parlate: ");
					while(rs.next()) {
						
						 String lang = rs.getString(1);
						
						System.out.print(lang + (rs.isLast() ? "" : "-"));
					}
				}
			}
			
			System.out.println("");
			
			 String statisticsQuery = "SELECT country_stats.year as anno,country_stats.population as popolazione,country_stats.gdp as gdp "
								 + "FROM countries "
								 + "JOIN country_stats "
								 + "ON countries.country_id = country_stats.country_id "
								 + "WHERE countries.country_id = ? "
								 + "ORDER BY year DESC "
								 + "LIMIT 1 ";
				try (PreparedStatement ps = connection.prepareStatement(statisticsQuery)) {
				
				ps.setString(1, inputId);
				
				try (ResultSet rs = ps.executeQuery()) {
				
					System.out.println("Ricerca piu recente: ");
					if(rs.next()) {
						
						 String year = rs.getString("anno");
						 String pop = rs.getString("popolazione");
						 String gdp = rs.getString("gdp");
						System.out.println("Anno: " + year);
						System.out.println("Popolazione: " + pop);
						System.out.println("GDP: " + gdp);
					}
				}	
			}
		} catch (Exception e) {
			System.err.println(e.getMessage()) ;
		}
	
	

}}
