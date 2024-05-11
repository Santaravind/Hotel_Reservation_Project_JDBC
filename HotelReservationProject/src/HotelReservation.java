import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.lang.Error;
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
            Connection connection=DriverManager.getConnection(url,username,password);
            System.out.println("connection estavlished Successfully !!");

            while (true) {
                System.out.println(" ");
                System.out.println("HOTEL MANAGEMENT SYSTEM ");
                Scanner sc =new Scanner(System.in);

                System.out.println(" 1. Reserve a room");
                System.out.println("2. View Reservations");
                System.out.println("3. Get Room Number");
                System.out.println("4. Update Reservations");
                System.out.println("5. Delete Reservations");
                System.out.println("0 .Exit ");
                System.out.println("Choose an option :");
                int choice=sc.nextInt();
                switch (choice) {
                    case 1:
                        reserveRoom(connection,sc);
                        break;
                        case 2:
                        ViewReservation(connection);
                        break;
                        case 3:
                          getRoomNumber( connection , sc);
                        break;
                        case 4:
                        UpdateReservation( connection, sc);
                        break;
                        case 5:
                        deleteReservation(connection,sc);
                        break;
                    case 0:
                     exit();
                     sc.close();
                     return;

                    default:
                    System.out.println("Invalid choice. Try again. ");
                        
                }
            }




            
       }catch(SQLException e){
               System.out.println(e.getMessage());
       } catch(InterruptedException e){
        throw new RuntimeException(e);
       // System.out.println(e.getMessage());
       }
      
       
    }

    private static void  reserveRoom(Connection connection,Scanner sc){
        try {
            System.out.println("Enter gest name:");
            String guestName=sc.next();
            sc.nextLine();
            System.out.println("Enter room number");
            int roomNumber=sc.nextInt();
            System.out.println("Enter contact number:");
            String contactNumber=sc.next();

            String sql="insert into reservations (gust_name,room_number,contact_number)"+
            " values ('"+guestName+"',"+roomNumber+",'"+contactNumber+"')";
            try(Statement stat=connection.createStatement()){
            int affectedrows=stat.executeUpdate(sql);

            if(affectedrows>0){
                System.out.println("Reservation successful!");
            }
            else{
                System.out.println("Reservation failed!");
            }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void ViewReservation(Connection connection )throws SQLException{
      String sql="Select reservation_id,guest_name,room_number,reservation_data From reservations;";

      try(Statement stmt=connection.createStatement();
         ResultSet result=stmt.executeQuery(sql) ) {
        
            System.out.println("Current Resrvations: ");
            System.out.println("+----------------------------------------------------------------- ------------------------------------------------+");
            System.out.println("| Reservation Id | Guest               | Room Number      | Contact Number       | Reservation Date |");
            System.out.println("+------------------------------------------------------------------------------------------------------------------+");


            while(result.next()){

                int reservationid=result.getInt("reservation_id");
                String guestName = result.getString("guest_name");
                int room_number=result.getInt("room_number");
                String contactNumber= result.getString("contact_number");
                String reservationDate=result.getTimestamp("reservation_data").toSting();
           }

            System.out.println(" +----------------------------+-------------------------- +--------------------------------+--------------------------------+");
      

      } 
         
    }

    private static void getRoomNumber(Connection connection ,Scanner sc){
        try{
            System.out.println("Enter reservation 10 : ");
            int reservationId=sc.nextInt();
            System.out.println("Enter guest name : ");
            String guestName=sc.next();

            String sql="Select room_number from reservations"+
                      "Where reservation_id= "+reservationId +
                      "And guest_name = '"+ guestName+ "'";
                        
        }
                      try(Statement stmt=connection.createStatement();
                      ResultSet result=stmt.executeQuery(sql)) {

                        if(result.next())
                         {
                            int roomNumber=result.getInt("room_number");
                            System.out.println("Room number for Reservation Id "+ reservationId+"and Guest "+guestName+"is :"+ roomNumber);

                         }else{
                            System.out.println("Reservation not found for the given ind and Guest name ");
                         }
                      } catch (SQLException e) {
                        e.printStackTrace();
                      }
        
    }

    
    private static UpdateReservation(Connection connection,Scanner sc){
        try{
            System.out.println("Enter reservation Id to update: ");
            int reservationId=sc.nextInt();
            sc.nextLine();

            if (!reservationExits(connection, reservationId)){

                System.out.println("Reservation not found for the given ID. ");
                return;

            }
            System.out.println("Enter new fuest name : ");
            String newGestName=sc.nextLine();
            System.out.println("Enter new room number : ");
            int newRommNumber =sc.nextInt();
            System.out.println("Enter new Contact number : ");
            String newContactNumber =sc.next();
            

            String sql="update reservations set guest_name ='"+newGestName+ " '"+
                  "room_number ='" +newRommNumber + " '"+
                   "contact_number = '"+ newContactNumber +"' "+
                   "where reservation_id= "+reservationId;

                   try(Statement stmt=connection.createStatement()){
                    int affectedrows= stmt.executeUpdate(sql);
                    if(affectedrows>0){
                        System.out.println("Reservation update successfully !");
                    }else{
                        System.out.println(" Reservation update failed .");
                    }
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
    }

    private static void deleteReservation(Connection connection ,Scanner sc){
        try{
            System.out.println("Enter  reservation ID to delete :");
            int reservationId=sc.nextInt();

            if(!reservationExists(connection, reservationId)){
                System.out.println("Reservation not found for the given ID .");
                return;
            }

            String sql="Delete from reservations where reservation_id"+reservationId;

            try (Statment stmt=connection.createStatement()){
                int affectedrows=stmt.executeUpdate(sql);

                if(affectedrows >0){
                    System.out.println(" Reservation Deleted successfully !");
                }
                else{
                    System.out.println(" Reservation deletion failed .");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    private static boolean reservationExists(Connection connection , int reservaitonId){
        try {
            String sql= "Select reservation_id from reservations where reservation_id= "+ reservaitonId;
             try(Statement stmt=connection.createStatement();
             ResultSet result= stmt.executeQuery(sql)){
                 return result .next();
             }
        } catch (SQLException e) {
             e.printStackTrace();
             return false;
        }
    }

       private static void exit() throws InterruptedException{
        System.out.println("Exitint system ");
        int i=5;
        while (i!=0) {
            System.out.println(".");
            Thread.sleep(450);
            i--;
        }
        System.out.println();
        System.out.println("ThankYou for using Hotel Reservation System !!1");
       }
}
