import java.io.IOException;
import javax.servlet.RequestDispatcher;  
import javax.servlet.ServletException;  
import javax.servlet.http.*;
import javax.servlet.http.HttpServlet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.nio.file.Files;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;  
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession.*; 

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

	
	public int insertItem(Map<String, LinkedHashMap<String, Integer>> shopList)
		{	System.out.println("Called insert item");
			int ordNo=0;
			boolean operation = false;
			Random rn = new Random();
			try
			{
				MySqlDataStoreUtilities ds = new MySqlDataStoreUtilities();
				String customer = (String) shopList.keySet().toArray()[0];
				//i=(i+1 < shopList.get(customer).keySet().toArray().length) ? (i+1) : i;
				System.out.println("Size of received hashmap: "+shopList.get(customer).keySet().toArray().length);
				int i = (shopList.get(customer).keySet().toArray().length) - 1;
				System.out.println("i= "+i);
				String itemName = (String) shopList.get(customer).keySet().toArray()[i];
				for (int x=0;x<=i ;x++ )
				{
					System.out.println("(String) shopList.get(customer).keySet().toArray()["+x+"]="+(String) shopList.get(customer).keySet().toArray()[x]);
				}
				//System.out.println("(String) shopList.get(customer).keySet().toArray()[i]="+(String) shopList.get(customer).keySet().toArray()[i]);
				int price = (Integer) shopList.get(customer).get(itemName);
				String chkOut = "select userName, OrderId, checkedOut from CustomerOrders where username=\""+customer+"\" AND checkedOut=false";
				PreparedStatement pst = conn.prepareStatement(chkOut);
				ResultSet rs = pst.executeQuery();
				ordNo = rn.nextInt(Integer.SIZE - 1) % 1000;
				if(rs.next())
				{
					if(!rs.getBoolean("checkedOut"))
					{
						//User NOT checkedout
						ordNo = rs.getInt("OrderId");
					}
				}
				String insertIntoCustomerOrdersQuery = "INSERT INTO CustomerOrders(OrderId, userName, orderName, orderPrice, checkedOut) VALUES (?,?,?,?,?);";
				System.out.println(ordNo+"-"+customer+"-"+itemName);
				pst = conn.prepareStatement(insertIntoCustomerOrdersQuery);
				pst.setInt(1,ordNo);
				pst.setString(2,customer);
				pst.setString(3,itemName);
				pst.setInt(4,price);
				pst.setBoolean(5,false);
				if(pst.executeUpdate() >= 0)
					System.out.println("Inserted order");
				else
					System.out.println("Order could not be inserted");
				
			}
			catch (SQLException ex) 
			{
				if (ex.getSQLState().startsWith("23"))
					ordNo=9999;
				ex.printStackTrace();
			}
		
		return ordNo;
		}


		public HashMap<String, Map<String, Integer>> getCart(String username)
		{
			HashMap<String, Map<String, Integer>> hm = new HashMap<String, Map<String, Integer>>();
			String ordNm="";
			int ordPr=0;
		try
			{
				Map<String, Integer> items = new HashMap<String, Integer>();
				String selectCartQuery = "SELECT orderName, orderPrice from CustomerOrders where username=\""+username+"\" and checkedOut=false";
				PreparedStatement pst = conn.prepareStatement(selectCartQuery);
				ResultSet rs = pst.executeQuery(selectCartQuery);
				while (rs.next())
				{
					ordNm = rs.getString("orderName");
					ordPr = rs.getInt("orderPrice");
					items.put(ordNm,ordPr);
				}
				
				hm.put(username, items);
				pst.close();
				//conn.close();
				
			}
			catch(SQLException se2)
					{se2.printStackTrace();}
			return hm;
		}

		public HashMap<String, Map<String, Integer>> getCart(String username, int ordNo)
		{
			HashMap<String, Map<String, Integer>> hm = new HashMap<String, Map<String, Integer>>();
			String ordNm="";
			int ordPr=0;
		try
			{
				Map<String, Integer> items = new HashMap<String, Integer>();
				String selectCartQuery = "SELECT orderName, orderPrice from CustomerOrders where username=\""+username+"\" and orderid="+ordNo+" and checkedOut=true";
				PreparedStatement pst = conn.prepareStatement(selectCartQuery);
				ResultSet rs = pst.executeQuery(selectCartQuery);
				System.out.println("QUERY="+selectCartQuery);
				while (rs.next())
				{
					ordNm = rs.getString("orderName");
					ordPr = rs.getInt("orderPrice");
					items.put(ordNm,ordPr);
					System.out.println("getCart rs_next loop");
				}
				
				hm.put(username, items);
				pst.close();
				conn.close();
				
			}
			catch(SQLException se2)
					{se2.printStackTrace();}

			return hm;
		}


		public int checkOut(String username, String cardNo, String address)
		{
			String ordNo="";
			
		try
			{
				String selectOrderId = "SELECT OrderId from CustomerOrders where username=\""+username+"\" and checkedOut=false";
				PreparedStatement pst = conn.prepareStatement(selectOrderId);
				ResultSet rs = pst.executeQuery(selectOrderId);
				while (rs.next())
					ordNo = rs.getString("OrderId");
				
				String insertCheckOut = "UPDATE CustomerOrders SET userAddress= \""+address+"\", creditCardNo=\""+cardNo+"\", checkedOut=true WHERE userName=\""+username+"\"";
				System.out.println(insertCheckOut);
				pst = conn.prepareStatement(insertCheckOut);
				if(pst.executeUpdate() >= 0)
					System.out.println("Payment details saved in DB");
				else
					System.out.println("Payment could not be saved iin DB!!!!");

				pst.close();
				conn.close();
				
			}
			catch(SQLException se2)
					{se2.printStackTrace();}
			return Integer.parseInt(ordNo);
		}

		public boolean deleteItemFromCart(String username, String item)
		{
			boolean deleted=false;
		try
			{	String deleteItem;
				updOrder uo = new updOrder();

				if((uo.getUsertype()).equals("seller")||(uo.getUsertype()).equals("manager"))
					deleteItem = "DELETE FROM CustomerOrders where userName=\""+username+"\" AND orderName=\""+item+"\" AND checkedOut=true";
				else
					deleteItem = "DELETE FROM CustomerOrders where userName=\""+username+"\" AND orderName=\""+item+"\" AND checkedOut=false";
				System.out.println(deleteItem);
				System.out.println("customerName="+username);
				System.out.println("customerType="+uo.getUsertype());
				PreparedStatement pst = conn.prepareStatement(deleteItem);
				//ResultSet rs = pst.executeUpdate(deleteItem);
				int rows_affected = pst.executeUpdate(deleteItem);
				if (rows_affected == 0)
					deleted = false;
				else
				{
					deleted = true;
					System.out.println(item+" deleted");
				}
			}
		catch(Exception e){}
		return deleted;
		}

		public boolean deleteOrder(String username, int ordNo)
		{
			boolean deleted=false;
			try
				{
					String delOrder = "DELETE FROM CustomerOrders where userName=\""+username+"\" AND OrderId="+ordNo+" AND checkedOut=true";
					System.out.println(delOrder);
					PreparedStatement pst = conn.prepareStatement(delOrder);
					//ResultSet rs = pst.executeUpdate(delOrder);
					int rows_affected = pst.executeUpdate(delOrder);
					if (rows_affected == 0)
						deleted = false;
					else
					{
						deleted = true;
						System.out.println(ordNo+" deleted");
					}
				}
			catch(Exception e){}
			return deleted;
		}

		public ArrayList<String> getOrderDetails(String username, int ordNo)
		{
			ArrayList<String> al = new ArrayList<String>();
			String ordNm,ordPr,uAddr;
			
		try
			{
				//Map<String, Integer> items = new HashMap<String, Integer>();
				String orderDetailsQuery = "SELECT orderName, orderPrice, userAddress from CustomerOrders where username=\""+username+"\" and OrderId="+ordNo+" and checkedOut=true";
				System.out.println(orderDetailsQuery);
				PreparedStatement pst = conn.prepareStatement(orderDetailsQuery);
				ResultSet rs = pst.executeQuery(orderDetailsQuery);
				while (rs.next())
				{
					ordNm = rs.getString("orderName");
					ordPr = Double.toString(rs.getDouble("orderPrice"));
					uAddr = rs.getString("userAddress");
					al.add(ordNm);
					al.add(ordPr);
					al.add(uAddr);
				}
				
				pst.close();
				conn.close();
				
			}
			catch(SQLException se2)
					{se2.printStackTrace();}
			return al;
		}

		public void loadProducts()
		{
			List<Product> prods = new ArrayList<Product>();
			HashMap<String, ArrayList<Product>> products = new HashMap<String, ArrayList<Product>>();
			LoadProducts lp = new LoadProducts();
			products=lp.getProductsFromXML();
			String prodID,prodName,prodType,prodManufacturer,prodCondition,retailer,retailer_zip,prodImgPath,accs1Name,accs1Retailer,accs2Name,accs2Retailer;
			float prodPrice;
			double accs2Price,accs1Price;
			int i=0;
			String loadXMLDataQuery=null,deleteExistingTable=null, createNewTable=null;
			PreparedStatement pst=null;
			try{
				if(getProductCount()>0)
				{
				deleteExistingTable = "drop table ProductDetails;";
				pst = conn.prepareStatement(deleteExistingTable);
				pst.execute();
				System.out.println("Deleted old table");
				}

				createNewTable = "create table ProductDetails(productId varchar(20),productName varchar(20),productType varchar(20),productPrice float,manufacturer varchar(20),productCondition varchar(20),retailerName varchar(20),retailerZIP int(10),prodImgPath varchar(30),accesory1Name varchar(20),accessory1Price double,accessory1Retailer varchar(20),accesory2Name varchar(20),accessory2Price double,accessory2Retailer varchar(20),primary key (productId));";
				pst = conn.prepareStatement(createNewTable);
				pst.execute();
				System.out.println("Created new table");
			}catch(Exception e){}

			for (String key : products.keySet())
			{
				for(Product prod: products.get(key)) 
				{
					prodID=prod.getId();
					prodName=prod.getName();
					prodType=prod.getType();
					prodPrice=prod.getPrice();
					prodManufacturer=prod.getManufacturer();
					prodCondition=prod.getCondition();
					retailer=prod.getRetailer();
					retailer_zip=prod.getRetailer_zip();
					prodImgPath=prod.getImage();
					accs1Name=(prod.getAccessories().get(prod.getAccessories().keySet().toArray()[0])).getName();
					accs2Name=(prod.getAccessories().get(prod.getAccessories().keySet().toArray()[1])).getName();
					accs1Price=(prod.getAccessories().get(prod.getAccessories().keySet().toArray()[0])).getPrice();
					accs2Price=(prod.getAccessories().get(prod.getAccessories().keySet().toArray()[1])).getPrice();
					accs1Retailer=(prod.getAccessories().get(prod.getAccessories().keySet().toArray()[0])).getRetailer();
					accs2Retailer=(prod.getAccessories().get(prod.getAccessories().keySet().toArray()[1])).getRetailer();
					try
					{
						loadXMLDataQuery = "INSERT INTO ProductDetails(productId,productName,productType,productPrice,manufacturer,productCondition,retailerName,retailerZIP,prodImgPath,accesory1Name,accessory1Price,accessory1Retailer,accesory2Name,accessory2Price,accessory2Retailer) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
						pst = conn.prepareStatement(loadXMLDataQuery);
						pst.setString(1,prodID);
						pst.setString(2,prodName);
						pst.setString(3,prodType);
						pst.setFloat(4,prodPrice);
						pst.setString(5,prodManufacturer);
						pst.setString(6,prodCondition);
						pst.setString(7,retailer);
						pst.setString(8,retailer_zip);
						pst.setString(9,prodImgPath);
						pst.setString(10,accs1Name);
						pst.setDouble(11,accs1Price);
						pst.setString(12,accs1Retailer);
						pst.setString(13,accs2Name);
						pst.setDouble(14,accs2Price);
						pst.setString(15,accs2Retailer);

						pst.execute();
					}
					catch(Exception e){}
					System.out.println("ProductID:"+prodID);
					System.out.println("prodName:"+prodName);
					System.out.println("prodType:"+prodType);
					System.out.println("prodPrice:"+prodPrice);
					System.out.println("prodManufacturer:"+prodManufacturer);
					System.out.println("prodCondition:"+prodCondition);
					System.out.println("prodImgPath:"+prodImgPath);
					System.out.println("prodRetailer:"+retailer);
					System.out.println("RetailerZIP:"+retailer_zip);
					System.out.println("Accessory1-Name:"+accs1Name);
					System.out.println("Accessory1-Price:"+accs1Price);
					System.out.println("Accessory1-Retailer:"+accs1Retailer);
					System.out.println("Accessory2-Name:"+accs2Name);
					System.out.println("Accessory2-Price:"+accs2Price);
					System.out.println("Accessory2-Retailer:"+accs2Retailer);
					

					i++;
					System.out.println("***********************LOOP# "+i+"**********************");
				}
				break;
			}
			System.out.println("Size of loaded products="+products.size());
		}

		public boolean deleteProduct(int orderNum)
		{
			boolean deleted=false;
			try
				{	String orderID= Integer.toString(orderNum);
					String deleteProd = "DELETE FROM productdetails where productId=\""+orderID+"\"";
					PreparedStatement pst = conn.prepareStatement(deleteProd);
					int rows_affected = pst.executeUpdate(deleteProd);
					if (rows_affected == 0)
						deleted = false;
					else
					{
						deleted = true;
						System.out.println(orderID+" deleted");
					}
				}
			catch(Exception e){}
			return deleted;
		}

		public int getProductCount()
		{
			int rowCount=0;
			try
				{	
					String countQuery = "select count(*) from productdetails";
					PreparedStatement pst = conn.prepareStatement(countQuery);
					ResultSet rs = pst.executeQuery(countQuery);
					while (rs.next())
						rowCount = rs.getInt("count(*)");
						
				
			}
			catch(SQLException se2)
					{se2.printStackTrace();}
			System.out.println("ROW COUNT="+rowCount);
			return rowCount;
		}

		public boolean addProduct(Product newprod)
		{
			boolean added=false;
			Accessory a = new Accessory();
			Accessory b = new Accessory();
			HashMap<String,Accessory> accessories=new HashMap<String,Accessory>();
			String prodID,prodName,prodType,prodManufacturer,prodCondition,retailer,retailer_zip,prodImgPath,accs1Name,accs1Retailer,accs2Name,accs2Retailer;
			int rows_affected=0;
			float prodPrice;
			double accs2Price,accs1Price;
			
			prodID = newprod.getId();
			prodName = newprod.getName();
			prodType = newprod.getType();
			prodPrice = newprod.getPrice();
			prodManufacturer = newprod.getManufacturer();
			prodCondition = newprod.getCondition();
			retailer = newprod.getRetailer();
			retailer_zip = newprod.getRetailer_zip();
			prodImgPath = newprod.getImage();
			accessories = newprod.getAccessories();
			a = accessories.get("acc1");
			b = accessories.get("acc2");
			accs1Name = a.getName();
			accs1Price = a.getPrice();
			accs1Retailer = a.getRetailer();
			accs2Name = b.getName();
			accs2Price = b.getPrice();
			accs2Retailer = b.getRetailer();
			try
					{
						updOrder uo = new updOrder();
						String productQuery = "INSERT INTO productdetails(productId,productName,productType,productPrice,manufacturer,productCondition,retailerName,retailerZIP,prodImgPath,accesory1Name,accessory1Price,accessory1Retailer,accesory2Name,accessory2Price,accessory2Retailer) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
						if(uo.getUpdate())
						{
							productQuery = "UPDATE ProductDetails SET productId=?,productName=?,productType=?,productPrice=?,manufacturer=?,productCondition=?,retailerName=?,retailerZIP=?,prodImgPath=?,accesory1Name=?,accessory1Price=?,accessory1Retailer=?,accesory2Name=?,accessory2Price=?,accessory2Retailer=? where productId='"+prodID+"'";
							uo.setUpdate(false);
						}
						PreparedStatement pst = conn.prepareStatement(productQuery);
						pst.setString(1,prodID);
						pst.setString(2,prodName);
						pst.setString(3,prodType);
						pst.setFloat(4,prodPrice);
						pst.setString(5,prodManufacturer);
						pst.setString(6,prodCondition);
						pst.setString(7,retailer);
						pst.setString(8,retailer_zip);
						pst.setString(9,prodImgPath);
						pst.setString(10,accs1Name);
						pst.setDouble(11,accs1Price);
						pst.setString(12,accs1Retailer);
						pst.setString(13,accs2Name);
						pst.setDouble(14,accs2Price);
						pst.setString(15,accs2Retailer);
						rows_affected = pst.executeUpdate();
						pst.close();
						conn.close();
						System.out.println("QUERY="+productQuery);
					}
					catch(Exception e){e.printStackTrace();}

					if (rows_affected == 0)
						added = false;
					else
						added = true;
			
			return added;
		}

		public Product getProduct(String prodId)
		{
			Product retrivedProd = new Product();
			HashMap<String,Accessory> ahm = new HashMap<String,Accessory>();
			Accessory a = new Accessory();
			Accessory b = new Accessory();
			try
			{
				String selectProduct = "SELECT * from ProductDetails where productId=\""+prodId+"\"";
				System.out.println("Query : "+selectProduct);
				PreparedStatement pst = conn.prepareStatement(selectProduct);
				ResultSet rs = pst.executeQuery(selectProduct);

				while (rs.next())
				{
					retrivedProd.setId(rs.getString("productId"));
					retrivedProd.setName(rs.getString("productName"));
					retrivedProd.setType(rs.getString("productType"));
					retrivedProd.setPrice(Float.parseFloat(rs.getString("productPrice")));
					retrivedProd.setManufacturer(rs.getString("manufacturer"));
					retrivedProd.setCondition(rs.getString("productCondition"));
					retrivedProd.setRetailer(rs.getString("retailerName"));
					retrivedProd.setRetailer_zip(rs.getString("retailerZIP"));
					retrivedProd.setImage(rs.getString("prodImgPath"));
					a.setName(rs.getString("accesory1Name"));
					a.setPrice(Double.parseDouble(rs.getString("accessory1Price")));
					a.setRetailer(rs.getString("accessory1Retailer"));
					b.setName(rs.getString("accesory2Name"));
					b.setPrice(Double.parseDouble(rs.getString("accessory2Price")));
					b.setRetailer(rs.getString("accessory2Retailer"));
					ahm.put("acc1",a);
					ahm.put("acc2",b);
					retrivedProd.setAccessories(ahm);
				}
				
				pst.close();
				conn.close();
				
			}
			catch(SQLException se2)
					{se2.printStackTrace();}

			return retrivedProd;
		}

		public ArrayList<Product> getProducts(String category, String brand, String prodNum)
		{
			ArrayList<Product> prods = new ArrayList<Product>();
			HashMap<String,Accessory> ahm = new HashMap<String,Accessory>();
			Accessory a = new Accessory();
			Accessory b = new Accessory();
			Product prod=null;
			String selectProduct=null;
			try{
				if(prodNum!=null)
					selectProduct = "SELECT * from ProductDetails where productId=\""+prodNum+"\"";
				else if(category.equals("all"))
					selectProduct = "SELECT * from ProductDetails";
				else
					selectProduct = "SELECT * from ProductDetails where productType=\""+category+"\" and manufacturer=\""+brand+"\"";
			System.out.println("Query : "+selectProduct);
			PreparedStatement pst = conn.prepareStatement(selectProduct);
			ResultSet rs = pst.executeQuery(selectProduct);

			while (rs.next())
			{
				prod=new Product();
				prod.setId(rs.getString("productId"));
				prod.setName(rs.getString("productName"));
				prod.setType(rs.getString("productType"));
				prod.setPrice(Float.parseFloat(rs.getString("productPrice")));
				prod.setManufacturer(rs.getString("manufacturer"));
				prod.setCondition(rs.getString("productCondition"));
				prod.setRetailer(rs.getString("retailerName"));
				prod.setRetailer_zip(rs.getString("retailerZIP"));
				prod.setImage(rs.getString("prodImgPath"));
				a.setName(rs.getString("accesory1Name"));
				a.setPrice(Double.parseDouble(rs.getString("accessory1Price")));
				a.setRetailer(rs.getString("accessory1Retailer"));
				b.setName(rs.getString("accesory2Name"));
				b.setPrice(Double.parseDouble(rs.getString("accessory2Price")));
				b.setRetailer(rs.getString("accessory2Retailer"));
				ahm.put("acc1",a);
				ahm.put("acc2",b);
				prod.setAccessories(ahm);
				prods.add(prod);
			}
				pst.close();
				conn.close();
				
			}
			catch(SQLException se2)
					{se2.printStackTrace();}

			return prods;
		}

		public ArrayList<String> getItemDetails(String itemName,String username)
		{
			ArrayList<String> itemDetails =null;
			try{
			String selectProduct = "SELECT * from CustomerOrders where orderName=\""+itemName+"\" and userName=\""+username+"\" and checkedOut=true";
			System.out.println("Query : "+selectProduct);
			PreparedStatement pst = conn.prepareStatement(selectProduct);
			ResultSet rs = pst.executeQuery(selectProduct);
			itemDetails = new ArrayList<String>();

			while (rs.next())
			{
				itemDetails.add(Integer.toString(rs.getInt("OrderId")));
				itemDetails.add(rs.getString("userName"));
				itemDetails.add(rs.getString("orderName"));
				itemDetails.add(Double.toString(rs.getDouble("orderPrice")));
				itemDetails.add(rs.getString("userAddress"));
				itemDetails.add(rs.getString("creditCardNo"));
			}
			pst.close();
			conn.close();
			}
			catch(SQLException se2)
					{se2.printStackTrace();}
			System.out.println("itemDetails size(mysql)="+itemDetails.size());
			return itemDetails;
		}

		public boolean updateOrderItem(String orderId,String userName,String prodName,String prodPrice,String delivAddr,String creditCard, String oldOrdName)
		{
			int rows_affected=0;
			boolean updated=false;
			try
					{
						System.out.println("orderId="+orderId);
						int ord = Integer.parseInt(orderId);
						double price = Double.parseDouble(prodPrice);
						String productQuery = "UPDATE CustomerOrders SET userName='"+userName+"',orderName='"+prodName+"',orderPrice="+price+",userAddress='"+delivAddr+"',creditCardNo='"+creditCard+"' where OrderId="+ord+" and userName='"+userName+"' and orderName='"+oldOrdName+"' and checkedOut=true";
						PreparedStatement pst = conn.prepareStatement(productQuery);
						
						/*pst.setString(1,userName);
						pst.setString(2,prodName);
						pst.setDouble(3,Double.parseDouble(prodPrice));
						pst.setString(4,delivAddr);
						pst.setString(5,creditCard);*/
												
						rows_affected = pst.executeUpdate();
						pst.close();
						conn.close();
						System.out.println("QUERY="+productQuery);
					}
					catch(Exception e){e.printStackTrace();}

					if (rows_affected == 0)
						updated = false;
					else
						updated = true;

					return updated;
		}
	}