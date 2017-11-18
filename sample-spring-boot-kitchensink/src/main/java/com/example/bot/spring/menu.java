package com.example.bot.spring;

import lombok.extern.slf4j.Slf4j;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.*;
import java.net.URISyntaxException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

@Slf4j
public class menu{
	public String getRecommendServing(String gender,String ageRange,boolean pregnant) throws Exception{
		String result = null;
		 try {
				Connection connection = getConnection();
				PreparedStatement stmt = connection.prepareStatement("SELECT * FROM recommendserving where gender=? and agerange like ? and pregnant=? and lactating=false;");
				stmt.setString(1,gender);
				ageRange="%"+ageRange+"%";
				stmt.setString(2, ageRange);
				stmt.setBoolean(3, pregnant);
				ResultSet rs = stmt.executeQuery();
				
				while (rs.next()) {
					result="Vegetables & legumes/beans: "+String.valueOf(rs.getDouble(5))+"\nFruit: "+String.valueOf(rs.getDouble(6))+"\nGrain (cereal) foods, mostly wholegrain: "+String.valueOf(rs.getDouble(7))+"\nLean meat and poultry, fish, eggs, nuts and seeds, and legumes/beans: "+String.valueOf(rs.getDouble(8))+"\nMilk, yoghurt, cheese and/or alternatives (mostly reduced fat): "+String.valueOf(rs.getDouble(9));
				}
				rs.close();
				stmt.close();
				connection.close();
		 	 } catch (Exception e) {
			    System.out.println(e);
		 	 	}
			if (result != null)
				return result;
			throw new Exception("NOT FOUND");
	}
	public String getMenu(String dish){
		String result = null;
		 try {
				Connection connection = getConnection();
				PreparedStatement stmt = connection.prepareStatement("SELECT * FROM menu WHERE dish=?;");
				stmt.setString(1, dish);
				ResultSet rs = stmt.executeQuery();
				
				while (rs.next()) {
					result=result+rs.getString(1)+"\t"+String.valueOf(rs.getInt(2))+"\t"+rs.getString(3)+"\n";
				}
				rs.close();
				stmt.close();
				connection.close();
		 	 } catch (Exception e) {
			    System.out.println(e);
		 	 }
			return result;
			
	}
	public void insertMenu(String dish,int price,String ingredients){
		try {
			Connection connection=getConnection();
			PreparedStatement stmt=connection.prepareStatement("INSERT INTO menu values (?,?,?);");
			stmt.setString(1,dish);
			stmt.setString(3,ingredients);
			stmt.setInt(2,price);
			stmt.executeQuery();
			stmt.close();
			connection.close();
		}catch (Exception e) {
			System.out.println(e);
		}
	}
	public String calculateNutrients(String name,double weight) {
		String result=null;
		try {
			Connection connection=getConnection();
			PreparedStatement stmt=connection.prepareStatement("with simitable(dbno,simi) as " + 
					"(select ndbno,similarity(description,?) from nutrients) " + 
					"select * from nutrients where ndbno=" + 
					"(select dbno from simitable order by simi DESC limit 1);");
			stmt.setString(1,name);
			ResultSet rs=stmt.executeQuery();
			while (rs.next()) {
				if (weight==0) {
					result="Description: "+rs.getString(2)+"\nWeight(g): "+String.valueOf(rs.getDouble(3))+"\nEnergy(kcal): "+String.valueOf(rs.getDouble(5))+"\nSodium, Na(mg): "
						+String.valueOf(rs.getDouble(6))+"\nSaturated Fat(g): "+String.valueOf(rs.getDouble(7));
				}
				else{
					result="Description: "+rs.getString(2)+"\nWeight(g): "+String.valueOf(weight)+"\nEnergy(kcal): "+String.valueOf(rs.getDouble(5)*weight/rs.getDouble(3))+"\nSodium, Na(mg): "
						+String.valueOf(rs.getDouble(6)*weight/rs.getDouble(3))+"\nSaturated Fat(g): "+String.valueOf(rs.getDouble(7)*weight/rs.getDouble(3));
				}
			}
			stmt.close();
			rs.close();
			connection.close();
		}catch (Exception e) {
			System.out.println(e);
		}
		return result;
	}
	private Connection getConnection() throws URISyntaxException, SQLException {
		Connection connection;
		URI dbUri = new URI(System.getenv("DATABASE_URL"));

		String username = dbUri.getUserInfo().split(":")[0];
		String password = dbUri.getUserInfo().split(":")[1];
		String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() +  "?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";

		log.info("Username: {} Password: {}", username, password);
		log.info ("dbUrl: {}", dbUrl);
		
		connection = DriverManager.getConnection(dbUrl, username, password);

		return connection;
	}
}