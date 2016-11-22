import java.io.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class MySqlDataStoreUtilities
{
	Connection conn = null;
	public MySqlDataStoreUtilities()
	{
		try
		{

			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/carrental","root","root");
		}
		catch(Exception e)
		{}
	}

	public boolean insertUser(String username,String password,String usertype)
	{
		int rows_affected = 0;
		boolean inserted = false;
		try
		{
			
			String insertIntoCustomerRegisterQuery = "INSERT INTO Registration(username,password,usertype) VALUES (?,?,?);";
			PreparedStatement pst = conn.prepareStatement(insertIntoCustomerRegisterQuery);
			pst.setString(1,username);
			pst.setString(2,password);
			pst.setString(3,usertype);
			rows_affected = pst.executeUpdate();
		}
		catch(Exception e){}

		if(rows_affected != 0)
			inserted = true;
		
		return inserted;
	}


	public HashMap<String, User> selectUser(String username)
	{
		HashMap<String, User> hm = new HashMap<String, User>();
		try
		{
			String pswd="", utype="";
			
			String selectUserQuery = "SELECT password, usertype from Registration where username=\""+username+"\"";
			System.out.println("Query : "+selectUserQuery);
			PreparedStatement pst = conn.prepareStatement(selectUserQuery);
			ResultSet rs = pst.executeQuery(selectUserQuery);
			while (rs.next())
			{
				pswd = rs.getString("password");
				utype = rs.getString("usertype");
			}
			
			User usr = new User(username, pswd,utype);
			hm.put(username, usr);
			pst.close();
			conn.close();
		}
		catch(SQLException se2)
				{se2.printStackTrace();}
		return hm;
	}

	public boolean findUser(String username)
		{
		boolean entry = false;
		try
			{
				String selectUserQuery = "SELECT password, usertype from Registration where username=\""+username+"\"";
				PreparedStatement pst = conn.prepareStatement(selectUserQuery);
				ResultSet rs = pst.executeQuery(selectUserQuery);
				if (!rs.next())
					entry = false;
				else
					entry = true;
			}
		catch(Exception e){}
		return entry;
		}

	
	public int bookCar(Reservation r)
	{
		int rows_affected = 0;
		boolean inserted = false;
		String booking_id=r.getBookingid();
		int customer_id=r.getCustomerId();
		String status=r.getStatus();
		float bookingAmount=r.getBookingAmount();
		String bookingDate=r.getBookingDate();
		float discount=r.getDiscount();
		String vehicalId=r.getVehicalId();
		String pickup=r.getPickupLoc();
		String drop=r.getDropLoc();
		String pickupDate=r.getPickupDate();
		String dropDate=r.getDropDate();

		try
		{
			
			String insertIntoCustomerRegisterQuery = "INSERT INTO booking(booking_id,customer_id,booking_status,booking_amount,booking_date,discount,vehical_id,pick_loc,drop_loc,pick_date,drop_date) VALUES (?,?,?,?,?,?,?,?,?,?,?);";
			PreparedStatement pst = conn.prepareStatement(insertIntoCustomerRegisterQuery);
			pst.setString(1,booking_id);
			pst.setInt(2,customer_id);
			pst.setString(3,status);
			pst.setFloat(4,bookingAmount);
			pst.setString(5,bookingDate);
			pst.setFloat(6,discount);
			pst.setString(7,vehicalId);
			pst.setString(8,pickup);
			pst.setString(9,drop);
			pst.setString(10,pickupDate);
			pst.setString(11,dropDate);
			rows_affected = pst.executeUpdate();
		}
		catch(Exception e){}

		if(rows_affected != 0)
			inserted = true;
		
		return inserted;
	}
}