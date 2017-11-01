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
	public String getRecommendServing(String gender,String ageRange,boolean pregnant,boolean lactating) throws Exception{
		String result = null;
		 try {
				Connection connection = getConnection();
				PreparedStatement stmt = connection.prepareStatement("SELECT * FROM recommendserving where gender=? and agerange=? and pregnant=? and lactating=?;");
				ResultSet rs = stmt.executeQuery();
				
				while (rs.next()) {
					result=result+String.valueOf(rs.getDouble(5))+"\t"+String.valueOf(rs.getDouble(6))+"\t"+String.valueOf(rs.getDouble(7))+"\t"+String.valueOf(rs.getDouble(8))+String.valueOf(rs.getDouble(9))+"\n";
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
	public String getMenu() throws Exception{
		String result = null;
		 try {
				Connection connection = getConnection();
				PreparedStatement stmt = connection.prepareStatement("SELECT * FROM menu;");
				ResultSet rs = stmt.executeQuery();
				
				while (rs.next()) {
					result=result+rs.getString(1)+"\t"+String.valueOf(rs.getInt(2))+"\t"+rs.getString(3)+String.valueOf(rs.getDouble(4))+"\t"+String.valueOf(rs.getDouble(5))+"\t"+String.valueOf(rs.getDouble(6))+"\t"+String.valueOf(rs.getDouble(7))+"\n";
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
	public void insertMenu(String dish,int price,String ingredients,double energy,double protein,double fat,double sodium) throws Exception{
		try {
			Connection connection=getConnection();
			PreparedStatement stmt=connection.prepareStatement("INSERT INTO history menu (?,?,?,?,?,?,?);");
			stmt.setString(1,dish);
			stmt.setString(3,ingredients);
			stmt.setInt(2,price);
			stmt.setDouble(4,energy);
			stmt.setDouble(5,protein);
			stmt.setDouble(6,fat);
			stmt.setDouble(7,sodium);
			stmt.executeQuery();
			stmt.close();
			connection.close();
		}catch (Exception e) {
			System.out.println(e);
		}
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