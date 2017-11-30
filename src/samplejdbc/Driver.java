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
	
	public static void ListHashes() throws SQLException, IOException {
		try{
			connect();
			String query = "select * from hashed_queries";
			statement = connection2.createStatement();
			ResultSet rs = statement.executeQuery(query);
			System.out.println("id\tSQL Query \t\t Hashed query");
			while(rs.next()){
				int id = rs.getInt("id");
				String queryname = rs.getString("sqlquery");
				String hashedquery = rs.getString("hashed_queries");
				
				System.out.print(id + "\t ");
				System.out.print(queryname + " \t");
				System.out.println(hashedquery);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally {
			disconnect();
		}
		
	}

	public  static void ValidateState(String sqlquery) throws SQLException{
		try {
			connect();
			System.out.println(sqlquery);

			statement = connection.createStatement();
			Statement statement1 = connection2.createStatement();
			ResultSet rs = statement.executeQuery(sqlquery);
			ResultSet rs1 = statement1.executeQuery("select * from hashed_queries");
			StringBuilder sb = new StringBuilder();
			int actor_id;
			while(rs.next()){
				 actor_id = rs.getInt("actor_id");
				String first_name = rs.getString("first_name");
				String last_name = rs.getString("last_name");
				sb.append(actor_id);
				sb.append(" ");
				sb.append(first_name);
				sb.append(" ");
				sb.append(last_name);
				sb.append(" ");
						
				
				}
			String actorquery = sb.toString(); 
			System.out.println(actorquery);
			String hashedquery = getHash(actorquery.getBytes(), "SHA-256");
			boolean flag = false; 
			while(rs1.next()){
				String queryname = rs1.getString("sqlquery");
				String hashed_q = rs1.getString("hashed_queries");
				
				if(sqlquery.equalsIgnoreCase(queryname) && hashedquery.equals(hashed_q)){
					System.out.println("Data has not changed and hashed query matched");
					flag = true;
					break; 
				}
				else
				{
					flag = false; 
				}
			}
			if(flag == false){
				System.out.println("Data has changed");
			}
			
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			disconnect();
		}
		
		
	}

	public  static void ProofOfState() throws SQLException, IOException {
		try {
		connect();
		String insert_query = "insert into hashed_queries (hashed_queries,sqlquery) values(?, ?)";
		statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
		preparedstatement = connection2.prepareStatement(insert_query);
		
		String actor_query = "SELECT * FROM actor";
		ResultSet rs = statement.executeQuery(actor_query);
		StringBuilder sb = new StringBuilder();
		int actor_id;
		while(rs.next()){
			 actor_id = rs.getInt("actor_id");
			String first_name = rs.getString("first_name");
			String last_name = rs.getString("last_name");
			sb.append(actor_id);
			sb.append(" ");
			sb.append(first_name);
			sb.append(" ");
			sb.append(last_name);
			sb.append(" ");
					
			
			}
		String actorquery = sb.toString(); 
		System.out.println(actorquery);
		
		//Generate Hash of the above data returned by the query 
		String hashed_actor_query = getHash(actorquery.getBytes(),"SHA-256");
		System.out.println(hashed_actor_query);
		
		
		preparedstatement.setString(1, hashed_actor_query);
		preparedstatement.setString(2, actor_query.toLowerCase());
		

		int i = preparedstatement.executeUpdate();
		
		if(i>0)
        {
              System.out.println("successful insert to table");
        }
              else
        {
             System.out.println("insert into the table failed");

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
			sc.nextLine();
			
			switch(choice){
			case 1:
				
				ProofOfState();
				break; 
				
			case 2: 
				System.out.println("Enter sql query: ");
				String sqlquery = sc.nextLine().toLowerCase();
				System.out.println(sqlquery);
				ValidateState(sqlquery);
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
