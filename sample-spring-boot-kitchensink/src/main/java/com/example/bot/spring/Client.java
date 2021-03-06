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
import java.time.LocalDate;
import java.util.*;
/**
 * Client using the chatbot service, it contains client profile
 * @author Project Group 14
 *
 */
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
	
	/**
	 * Get gender
	 * @return gender
	 */
	public String getGender() {
		return gender;
	}
	/**
	 * Get age
	 * @return age
	 */
	public int getAge() {
		return age;
	}
	/**
	 * Get name
	 * @return name
	 */
	public String getName() {
		return name;
	}
	/**
	 * Get height
	 * @return height
	 */
	public double getHeight() {
		return height;
	}
	/**
	 * Get weight
	 * @return weight
	 */
	public double getWeight() {
		return weight;
	}
	/**
	 * Load the data client's profile into the class from database
	 * @param userID user's id
	 */
	public void loadClient(String userID){
		try {
			Connection connection=getConnection();
			PreparedStatement stmt=connection.prepareStatement("SELECT * FROM client WHERE userID=?;");
			stmt.setString(1, userID);
			ResultSet rs=stmt.executeQuery();
			rs.next();
			this.userID=rs.getString(1);
			this.name=rs.getString(2);
			this.age=rs.getInt(3);
			this.gender=rs.getString(4);
			this.height=rs.getDouble(5);
			this.weight=rs.getDouble(6);
			stmt.close();
			rs.close();
			connection.close();
		}catch (Exception e) {
		}
	}
	/**
	 * Create proflie nd save into database for first time user
	 * @param userID user's id
	 */
	public void addClient(String userID) {
		try {
			this.userID=userID;
			Connection connection=getConnection();
			PreparedStatement stmt=connection.prepareStatement("INSERT INTO client VALUES (?,?,?,?,?,?);");
			stmt.setString(2,null);
			stmt.setString(1, userID);
			stmt.setString(4,null);
			stmt.setInt(3,0);
			stmt.setDouble(5,0);
			stmt.setDouble(6,0);
			stmt.executeUpdate();
			stmt.close();
			connection.close();
		}catch (Exception e) {
			
		}
		try {
			long initi=-1;
			Connection connection2=getConnection();
			PreparedStatement stmt2=connection2.prepareStatement("insert into clientcoupon values(?,?,?);");
			stmt2.setString(1,userID);
			stmt2.setLong(2, initi);
			stmt2.setBoolean(3,false);
			stmt2.executeUpdate();
			stmt2.close();
			connection2.close();
		}catch (Exception e) {}
	}
	/**
	 * Get client proflie
	 * @return Client Profile
	 */
	public String getProfile() {
		String result=null;
		result="Name: "+name+"\nGender: "+gender+"\nHeight(m): "+String.valueOf(height)+"\nWeight(kg): "+String.valueOf(weight)+"\nBMI(kg/m^2):"+this.calculateBMI();
		return result;
	}
	/**
	 * Update client name
	 * @param name client name
	 */
	public void updateName(String name){
		try {
			this.name=name;
			Connection connection=getConnection();
			PreparedStatement stmt=connection.prepareStatement("UPDATE client set name=? where userID=?;");
			stmt.setString(2,userID);
			stmt.setString(1,name);
			stmt.executeUpdate();
			stmt.close();
			connection.close();
		}catch (Exception e) {
		}
	}
	/**
	 * Update client name
	 * @param age client name
	 */
	public void updateAge(int age){
		try {
			this.age=age;
			Connection connection=getConnection();
			PreparedStatement stmt=connection.prepareStatement("UPDATE client set age=? where userID=?;");
			stmt.setString(2,userID);
			stmt.setInt(1,age);
			stmt.executeUpdate();
			stmt.close();
			connection.close();
		}catch (Exception e) {
		}
	}
	/**
	 * Update client gender
	 * @param gender client gender
	 */
	public void updateGender(String gender){
		try {
			this.gender=gender;
			Connection connection=getConnection();
			PreparedStatement stmt=connection.prepareStatement("UPDATE client set gender=? where userID=?;");
			stmt.setString(2,userID);
			stmt.setString(1,gender);
			stmt.executeUpdate();
			stmt.close();
			connection.close();
		}catch (Exception e) {
		}
	}
	/**
	 * Update client height
	 * @param height client height
	 */
	public void updateHeight(double height){
		try {
			this.height=height;
			Connection connection=getConnection();
			PreparedStatement stmt=connection.prepareStatement("UPDATE client set height=? where userID=?;");
			stmt.setString(2,userID);
			stmt.setDouble(1,height);
			stmt.executeUpdate();
			stmt.close();
			connection.close();
		}catch (Exception e) {
		}
	}
	/**
	 * Update client weight
	 * @param weight client wwight
	 */
	public void updateWeight(double weight){
		try {
			this.weight=weight;
			Connection connection=getConnection();
			PreparedStatement stmt=connection.prepareStatement("UPDATE client set weight=? where userID=?;");
			stmt.setString(2,userID);
			stmt.setDouble(1,weight);
			stmt.executeUpdate();
			stmt.close();
			connection.close();
		}catch (Exception e) {
		}
	}
	/**
	 * Check if pclient profile is complete
	 * @param userID client user id
	 * @return the stages of the completiveness of client's profile 
	 */
	public int isInfoComplete(String userID){
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
		 	 	}
			return result;
	}
	/**
	 * Calculate client's BMI
	 * @return BMI
	 */
	public double calculateBMI() {
		if (height!=0 && weight!=0)
			return (weight/height)/height;
		else return 0;
	}
	/**
	 * Add ate dish of client into database
	 * @param dish client dish
	 * @throws Exception if exception inside
	 */
	public void addHistory(String dish) throws Exception{
		try {
			Connection connection=getConnection();
			java.sql.Date sqlDate=new java.sql.Date(Calendar.getInstance().getTime().getTime());
			PreparedStatement stmt=connection.prepareStatement("INSERT INTO history VALUES (?,?,?,?);");
			stmt.setString(1,userID);
			stmt.setDate(2,sqlDate);
			stmt.setDouble(3,weight);
			stmt.setString(4,dish);
			stmt.executeUpdate();
			stmt.close();
			connection.close();
		}catch (Exception e) {
			
		}
	}
	/**
	 * Get clien's eating history form database
	 * @return dish history
	 */
	public String getHistory(){
		String result = null;
		 try {
				Connection connection = getConnection();
				PreparedStatement stmt = connection.prepareStatement("SELECT * FROM history where userID=?;");
				stmt.setString(1, userID);
				ResultSet rs = stmt.executeQuery();
				
				while (rs.next()) {
					if (result==null) {
						result="Date & Dish History:\n"+rs.getDate(2).toString()+"\t"+rs.getString(4)+"\n";
					}
					else
						result=result+rs.getDate(2).toString()+"\t"+rs.getString(4)+"\n";
				}
				rs.close();
				stmt.close();
				connection.close();
		 	 } catch (Exception e) {
		 	 }
			return result;
	}
	/**
	 * Client get coupon for friends
	 * @return coupon digits
	 * 
	 */
	public long getCoupon() {
		long coupon=-1;
		try {
			Connection connection=getConnection();
			PreparedStatement stmt=connection.prepareStatement("select coupon from clientcoupon where userid=?;");
			stmt.setString(1,userID);
			ResultSet rs=stmt.executeQuery();
			while (rs.next()) {
				coupon=rs.getLong(1);
			}
			connection.close();
			stmt.close();
			rs.close();
		}catch (Exception e) {
		}
		return coupon;
	}
	/**
	 * Update coupon digits to the client's profile
	 * @param coupon coupon for client
	 */
	public void updateCoupon(long coupon) {
		try {
			Connection connection=getConnection();
			PreparedStatement stmt=connection.prepareStatement("update clientcoupon set coupon=? where userid=?;");
			stmt.setString(2, userID);
			stmt.setLong(1, coupon);
			stmt.executeUpdate();
			connection.close();
			stmt.close();
		}catch(Exception e) {
		}
	}
	/**
	 * Check clietn claimed any coupon before
	 * @return false if claim, true otherwise
	 */
	public boolean ifclaim() {
		boolean claim=false;
		try {
			Connection connection=getConnection();
			PreparedStatement stmt=connection.prepareStatement("select claim from clientcoupon where userid=?;");
			stmt.setString(1,userID);
			ResultSet rs=stmt.executeQuery();
			while (rs.next()) {
				claim=rs.getBoolean(1);
			}
			connection.close();
			stmt.close();
			rs.close();
		}catch (Exception e) {
		}
		return claim;
	}
	/**
	 * Claim coupon for both client and friend
	 * @param coupon coupon for client
	 * @return the list of client and friend
	 */
	public List<String> claim(long coupon) {
		List<String> updateClients=new ArrayList<String>();
		try {
			Connection connection=getConnection();
			PreparedStatement stmt=connection.prepareStatement("select * from clientcoupon where coupon=?;");
			stmt.setLong(1,coupon);
			ResultSet rs=stmt.executeQuery();
			while (rs.next()) {
				updateClients.add(rs.getString(1));
			}
			connection.close();
			stmt.close();
			rs.close();
		}catch (Exception e) {
		}
		try {
			Connection connection2=getConnection();
			PreparedStatement stmt2=connection2.prepareStatement("update clientcoupon set claim=true where userid=?;");
			stmt2.setString(1,userID);
			stmt2.executeUpdate();
			connection2.close();
			stmt2.close();
		}catch (Exception e) {}
		return updateClients;
	}
	public void deleteRecord(String userID) {
		try {
			Connection connection1=getConnection();
			PreparedStatement stmt1=connection1.prepareStatement("delete from client where userid=?;");
			stmt1.setString(1, userID);
			stmt1.executeUpdate();
			stmt1.close();
			connection1.close();
		}
		catch (Exception e) {}
		try {
			Connection connection2=getConnection();
			PreparedStatement stmt2=connection2.prepareStatement("delete from history where userid=?;");
			stmt2.setString(1, userID);
			stmt2.executeUpdate();
			stmt2.close();
			connection2.close();
		}
		catch (Exception e) {}
		try {
			Connection connection3=getConnection();
			PreparedStatement stmt3=connection3.prepareStatement("delete from clientcoupon where userid=?;");
			stmt3.setString(1, userID);
			stmt3.executeUpdate();
			stmt3.close();
			connection3.close();
		}
		catch (Exception e) {}
	}
	/**
	 * Connect with database
	 * @return
	 * @throws URISyntaxException if there is uri syntax exception
	 * @throws SQLException if there is connection problem to database
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