import java.sql.*;
public class HotelReservation{
    public static void main(String[] args) throws Exception {
        //String url="jdbc:mysql://localhost:3306/mysql";
        String url="jdbc:mysql://Localhost:3306/hotel_db";
        String username="root";
        String password="root";

        
        try{
        Class.forName("com.mysql.jdbc.Diver");
        System.out.println("Driver loaded successfully");
        }
        catch(ClassNotFoundException e){

            System.out.println(e.getMessage());

        } 

        try{
            //@SuppressWarnings("unused")
            Connection con=DriverManager.getConnection(url,username,password);
            System.out.println("connection estavlished Successfully !!");




            
       }catch(SQLException e){
               System.out.println(e.getMessage());
       } 
      
    }
}
