import java.sql.*;  
public class JdbcConnection
{  
public static void main(String args[]){  
  try{   
  Class.forName("com.mysql.cj.jdbc.Driver");  
  Connection con=DriverManager.getConnection("jdbc:mysql://localhost/bank_management_system","root","");  
  System.out.println("Connection is successful done "+con);
      System.out.println("Databse is created");  
  }catch(Exception e){ System.out.println(e);}  
  }  
 
 }

 