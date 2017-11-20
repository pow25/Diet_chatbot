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

/**
 * Recommending menu for client
 * @author Group 14
 *
 */
@Slf4j
public class menu{
	/**
	 * Get recommended serving of client base on client profile
	 * @param gender
	 * @param ageRange
	 * @param pregnant
	 * @return Info of serving of food types of the client
	 * @throws Exception
	 */
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
	/**
	 * Get recommeding dish for client base on BMI
	 * @param userID
	 * @param bmi
	 * @return dish name with health state
	 */
	public String getRecommendDish(String userID,double bmi) {
		String result=null;
		String analy=analyzeBMI(bmi);
		if (analy==null) {
			return "maybe the weight and height you provide are not correct.";
		}
		try {
			Connection connection= getConnection();
			PreparedStatement stmt = connection.prepareStatement("SELECT * FROM history where userID=?;");
			stmt.setString(1, userID);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				try {
					Connection connection2=getConnection();
					PreparedStatement stmt2=connection.prepareStatement("with simitable(des,simi,ener) as " + 
							"(select description,similarity(description,?),energy/weight from nutrients) " + 
							"select * from simitable order by simi DESC limit 6;");
					stmt2.setString(1,rs.getString(4));
					ResultSet rs2=stmt2.executeQuery();
					while (rs2.next()) {
						if (analy.equals("underweight")&&rs2.getDouble(3)>2) {
							return "You are under weight.\nRecommended dish: "+rs2.getString(1);
						}
						if (analy.equals("overweight")&&rs2.getDouble(3)<1) {
							return "You are overweight.\nRecommended dish: "+rs2.getString(1);
						}
						if (analy.equals("healthy")&&rs2.getDouble(3)>=1&&rs2.getDouble(3)<=2) {
							return "You are healthy.\nRecommended dish: "+rs2.getString(1);
						}
					}
				}catch (Exception e) {
					return "sorry, maybe your history provided is not enough";
				}
			}
		}catch (Exception e) {
			return "sorry, maybe your history provided is not enough";
		}
		return "sorry, maybe your history proveded is not enough";
	}
	/**
	 * Analysis client BMI
	 * @param bmi
	 * @return health state
	 */
	private String analyzeBMI(double bmi) {
		if (bmi<=0) {
			return null;
		}
		else if(bmi>0 && bmi<18.5) {
			return "underweight";
		}
		else if(bmi>=18.5 && bmi<23) {
			return "healthy";
		}
		else {
			return "overweight";
		}
	}
	/**
	 * Get Menu for client
	 * @param dish
	 * @return dishes in menu format
	 */
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
	/**
	 * Insert dish and its info into menu database
	 * @param dish
	 * @param price
	 * @param ingredients
	 */
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
	/**
	 * Calculate nutrients of a dish base on the food weight
	 * @param name
	 * @param weight
	 * @return description of the food nutrients
	 */
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
	/**
	 * Connect with database
	 * @return
	 * @throws URISyntaxException
	 * @throws SQLException
	 */
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