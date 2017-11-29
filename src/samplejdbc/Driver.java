package samplejdbc;

import java.sql.*;
import java.util.Scanner;
import java.io.IOException;
import java.security.MessageDigest;
import javax.xml.bind.DatatypeConverter;

public class Driver {
	private static Connection connection;
	private static Connection connection2;
	private  static Statement statement;
	private static PreparedStatement preparedstatement; 
	
	public  static void connect() throws SQLException
	{
		String url = "jdbc:mysql://sbstest.chd1y6fipbcf.us-east-1.rds.amazonaws.com/sakila";
		String url1 = "jdbc:mysql://sbstest.chd1y6fipbcf.us-east-1.rds.amazonaws.com/SBSTest";
		String user = "CodingTest";
		String password = "CodingTest";
		connection = DriverManager.getConnection(url, user, password);
		connection2 = DriverManager.getConnection(url1, user, password);
	
	}
	
	public static void disconnect() throws SQLException
	{
		if (statement != null) statement.close();
		if (connection != null) connection.close();
	}
	
	//This function converts the given byte array into the hashing algorithm provided. Here, we will use sha-256 while calling the function on main
	public static String getHash(byte[] inputBytes, String algorithm) {
		String hashValue = "";
		try {
			
			MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
			messageDigest.update(inputBytes);
			byte[] digestedBytes = messageDigest.digest();
			hashValue = DatatypeConverter.printHexBinary(digestedBytes).toLowerCase();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return hashValue; 
	}
	
	public static void ListHashes() throws SQLException {
		
	}

	public  static void ValidateState() throws SQLException{
		
	}

	public  static void ProofOfState() throws SQLException, IOException {
		try {
		connect();
		
		String insert_query = "insert into hashed_actor values(?, ?, ?)";
		statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
		preparedstatement = connection2.prepareStatement(insert_query);
		
		String actor_query = "SELECT * FROM actor";
		ResultSet rs = statement.executeQuery(actor_query);
		
		while(rs.next()){
			int actor_id = rs.getInt("actor_id");
			String first_name = rs.getString("first_name");
			String last_name = rs.getString("last_name");
			
			preparedstatement.setInt(1, actor_id);
			preparedstatement.setString(2, getHash(first_name.getBytes(),"SHA-256"));
			preparedstatement.setString(3, getHash(last_name.getBytes(),"SHA-256"));

			int i = preparedstatement.executeUpdate();
			if(i>0)
	        {
	              System.out.println("success");
	        }
	              else
	        {
	             System.out.println("insert into the table failed");

	        }
			
		}
		}
		catch(SQLException e){
			
			e.printStackTrace();
		}finally {
			disconnect();
		}

	}

	public static void main(String[] args) throws Exception{
	Scanner sc = new Scanner(System.in);
		try {
			//Menu choices
			
			System.out.println("Menu");
			System.out.println("1. Proof of State");
			System.out.println();
			System.out.println("2. Validate State");
			System.out.println();
			System.out.println("3. List hashes");
			
			int choice; 
			System.out.println("Enter the choice: ");
			choice = sc.nextInt();
			
			switch(choice){
			case 1:
				ProofOfState();
				break; 
				
			case 2: 
				ValidateState();
				break;
				
			case 3:
				ListHashes();
				break;
				
				default: 
					System.out.println("Invalid choice. ");
					break; 
				
			}
			
		}
		catch (Exception exc) {
			exc.printStackTrace();
		}finally{
			sc.close();
		}

	}

	

}
