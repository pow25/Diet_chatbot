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
	public Client() {
		userID=null;
		name=null;
		age=0;
		gender=null;
		height=0;
		weight=0;
	}
	public void loadClient(String userID) {
		try {
			Connection connection=getConnection();
			PreparedStatement stmt=connection.prepareStatement("SELECT * FROM client WHERE userID=?;");
			stmt.setString(1, userID);
			ResultSet rs=stmt.executeQuery();
			rs.next();
			this.name=rs.getString(2);
			this.age=rs.getInt(3);
			this.gender=rs.getString(4);
			this.height=rs.getDouble(5);
			this.weight=rs.getDouble(6);
			stmt.close();
			rs.close();
			connection.close();
		}catch (Exception e) {
			System.out.println(e);
		}
	}
	public void addClient(String userID) {
		try {
			this.userID=userID;
			Connection connection=getConnection();
			PreparedStatement stmt=connection.prepareStatement("INSERT INTO client VALUES (?,?,?,?,?,?);");
			stmt.setString(2,name);
			stmt.setString(1, userID);
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
	public String getProfile() {
		String result=null;
		result=result+"UserID: "+userID+"\nName: "+name+"\nGender: "+gender+"\nHeight(m): "+String.valueOf(height)+"\nWeight(kg): "+String.valueOf(weight);
		return result;
	}
	public void updateName(String name) throws Exception{
		try {
			this.name=name;
			Connection connection=getConnection();
			PreparedStatement stmt=connection.prepareStatement("UPDATE client set name=? where userID=?;");
			stmt.setString(2,userID);
			stmt.setString(1,name);
			stmt.executeQuery();
			stmt.close();
			connection.close();
		}catch (Exception e) {
			System.out.println(e);
		}
	}
	public void updateAge(int age) throws Exception{
		try {
			this.age=age;
			Connection connection=getConnection();
			PreparedStatement stmt=connection.prepareStatement("UPDATE client set age=? where userID=?;");
			stmt.setString(2,userID);
			stmt.setInt(1,age);
			stmt.executeQuery();
			stmt.close();
			connection.close();
		}catch (Exception e) {
			System.out.println(e);
		}
	}
	public void updateGender(String gender) throws Exception{
		try {
			this.gender=gender;
			Connection connection=getConnection();
			PreparedStatement stmt=connection.prepareStatement("UPDATE client set gender=? where userID=?;");
			stmt.setString(2,userID);
			stmt.setString(1,gender);
			stmt.executeQuery();
			stmt.close();
			connection.close();
		}catch (Exception e) {
			System.out.println(e);
		}
	}
	public void updateHeight(double height) throws Exception{
		try {
			this.height=height;
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
			this.weight=weight;
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
	public int isInfoComplete(String userID) throws Exception{
		int result = 1;
		try {
				Connection connection = getConnection();
				PreparedStatement stmt = connection.prepareStatement("SELECT * FROM client where userID=?;");
				stmt.setString(1, userID);
				ResultSet rs = stmt.executeQuery();
				
				while (rs.next()) {
					if (rs.getString(2)==null) {
						result=2;
						break;
					}
					else if (rs.getInt(3)==0) {
						result=3;
					}
					else if (rs.getString(4)==null) {
						result=4;
					}
					else if (rs.getDouble(5)==0) {
						result=5;
					}
					else if (rs.getDouble(6)==0) {
						result=6;
					}
					else
						result=0;
				}
				rs.close();
				stmt.close();
				connection.close();
		 	} catch (Exception e) {
			    System.out.println(e);
		 	 	}
			if (result != 1)
				return result;
			throw new Exception("NOT FOUND");
	}
	public double calculateBMI() {
		if (weight!=0 && height!=0)
			return (weight/height)/height;
		else return 0;
	}
	public void addHistory(Date orderDate,String dish) throws Exception{
		try {
			Connection connection=getConnection();
			PreparedStatement stmt=connection.prepareStatement("INSERT INTO history VALUES (?,?,?,?);");
			stmt.setString(1,userID);
			stmt.setDate(2,orderDate);
			stmt.setDouble(3,weight);
			stmt.setString(4,dish);
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
				PreparedStatement stmt = connection.prepareStatement("SELECT * FROM history where userID=?;");
				stmt.setString(1, userID);
				ResultSet rs = stmt.executeQuery();
				
				while (rs.next()) {
					result=result+rs.getDate(2).toString()+"\t"+String.valueOf(rs.getDouble(3))+"\t"+rs.getString(4)+"\n";
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