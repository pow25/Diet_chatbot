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
public class Client{
	private String userID;
	private String name;
	private int age;
	private String gender;
	private double height;
	private double weight;
	public Client(String userID,String name, int age, String gender, double height, double weight) {
		this.name=name;
		this.userID=userID;
		this.age=age;
		this.gender=gender;
		this.height=height;
		this.weight=weight;
		try {
			Connection connection=getConnection();
			PreparedStatement stmt=connection.prepareStatement("INSERT INTO client VALUES (?,?,?,?,?,?);");
			stmt.setString(2,name);
			stmt.setString(1, user_ID);
			stmt.setString(4,gender);
			stmt.setInt(3,age);
			stmt.setDouble(5,height);
			stmt.setDouble(6,weight);
			stmt.executeQuery();
			stmt.close();
			connection.close();
		}catch (Exception e) {
			System.out.println(e);
		}
	}
	public void updateHeight(double height) throws Exception{
		try {
			Connection connection=getConnection();
			PreparedStatement stmt=connection.prepareStatement("UPDATE client set height=? where userID=?;");
			stmt.setString(2,userID);
			stmt.setDouble(1,height);
			stmt.executeQuery();
			stmt.close();
			connection.close();
		}catch (Exception e) {
			System.out.println(e);
		}
	}
	public void updateWeight(double weight) throws Exception{
		try {
			Connection connection=getConnection();
			PreparedStatement stmt=connection.prepareStatement("UPDATE client set weight=? where userID=?;");
			stmt.setString(2,userID);
			stmt.setDouble(1,weight);
			stmt.executeQuery();
			stmt.close();
			connection.close();
		}catch (Exception e) {
			System.out.println(e);
		}
	}
	public int isInfoComplete() {
		
	}
	public double calculateBMI() {
		return (weight/height)/height;
	}
	public void addHistory(Date orderDate,String dish) throws Exception{
		try {
			Connection connection=getConnection();
			PreparedStatement stmt=connection.prepareStatement("INSERT INTO history VALUES (?,?,?,?,?);");
			stmt.setString(1,name);
			stmt.setDate(3,orderDate);
			stmt.setInt(2,age);
			stmt.setDouble(4,weight);
			stmt.setString(5,dish);
			stmt.executeQuery();
			stmt.close();
			connection.close();
		}catch (Exception e) {
			System.out.println(e);
		}
	}
	public String getHistory() throws Exception{
		String result = null;
		 try {
				Connection connection = getConnection();
				PreparedStatement stmt = connection.prepareStatement("SELECT * FROM history where name=? and age=?;");
				stmt.setString(1, name);
				stmt.setInt(2, age);
				ResultSet rs = stmt.executeQuery();
				
				while (rs.next()) {
					result=result+rs.getDate(3).toString()+"\t"+String.valueOf(rs.getDouble(4))+"\t"+rs.getString(5)+"\n";
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