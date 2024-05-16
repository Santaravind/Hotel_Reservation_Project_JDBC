import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
//import java.lang.Error;
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
 
    //First case 1:
    //Reservation of room 
    private static void  reserveRoom(Connection connection,Scanner sc){
        try {
            System.out.println("Enter guest name:");
            String guestName=sc.next();
            sc.nextLine();
            System.out.println("Enter room number");
            int roomNumber=sc.nextInt();
            System.out.println("Enter contact number:");
            String contactNumber=sc.next();

            String sql="insert into reservations (guest_name,room_number,contact_number)"+
            " values ('"+guestName+"',"+roomNumber+",'"+contactNumber+"');";
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

    //case 2: 

    // viwe the details  of User
    private static void ViewReservation(Connection connection )throws SQLException{
      String sql="Select reservation_id,guest_name,contact_number,room_number,reservation_data From reservations;";

      try(Statement stmt=connection.createStatement();
         ResultSet result=stmt.executeQuery(sql) ) {
        
            System.out.println("Current Resrvations: ");
            System.out.println("+----------------------------------------------------------------- ------------------------------------------------+");
            System.out.println("| Reservation Id | Guest               | Room Number      | Contact Number       | Reservation Date |");
            System.out.println("+-----------------------------------------------------------------------------------------------------------+");


            while(result.next()){

                int reservationid=result.getInt("reservation_id");
                String guestName = result.getString("guest_name");
                int room_number=result.getInt("room_number");
                String contactNumber= result.getString("contact_number");
                String reservationDate=result.getTimestamp("reservation_data").toString();

                System.out.printf("|%-14d | %-15s |%-13d |%-20s | %-19s  |\n",reservationid,guestName,room_number, contactNumber, reservationDate);
           }

            System.out.println(" +----------------------------+-------------------------- +--------------------------------+--------------------------------+");
      

      } 
         
    }

    //Case 3 
    //Get clinet details 
    private static void getRoomNumber(Connection connection ,Scanner sc){
        try{
              System.out.println("Enter reservation Id : ");
             int reservationId=sc.nextInt();
            System.out.println("Enter guest name : ");
            String guestName=sc.next();

            // String sql="Select room_number from reservations"+
            //           "Where reservation_id= "+reservationId +
            //           "And guest_name = '"+ guestName+ "'";
                        
             String sql="select room_number  from reservations"+
                        " Where guest_name = '" +guestName + "'";

                      try(Statement stmt=connection.createStatement();
                      ResultSet result=stmt.executeQuery(sql)) {

                        if(result.next())
                         {
                            int roomNumber=result.getInt("room_number");
                           // int reservaitonId =result.getInt("reservation_id");
                            System.out.println("Room number for Reservation Id = "+ reservationId  + " and  Guest name = " + guestName + " is : " + roomNumber);

                         }else{
                            System.out.println("Reservation not found for the given ind and Guest name ");
                         }
                      } catch (SQLException e) {
                        e.printStackTrace();
                      }
    }
    catch(Exception e){
        System.out.println(e.getMessage());
    }
    }

    // case 4:
    //Update the details 

    private static void UpdateReservation(Connection connection,Scanner sc){
        try{
            System.out.println("Enter reservation Id to update: ");
            int reservationId=sc.nextInt();
            sc.nextLine();

            if (!reservationExists(connection, reservationId)){

                System.out.println("Reservation not found for the given ID. ");
                return;

            }
            System.out.println("Enter new Guest name : ");
            String newGestName=sc.nextLine();
            System.out.println("Enter new room number : ");
            int newRommNumber =sc.nextInt();
            System.out.println("Enter new Contact number : ");
            String newContactNumber =sc.next();
            

            String sql="update reservations set guest_name ='" + newGestName + "' ," +
                  "room_number = " + newRommNumber + " ,"+
                   "contact_number = '" +  newContactNumber  + "' " +
                   "where reservation_id= " + reservationId ;

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
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    //case 5
    //Delete the reservations details of clint
    private static void deleteReservation(Connection connection ,Scanner sc){
        try{
            System.out.println("Enter  reservation ID to delete :");
            int reservationId=sc.nextInt();

            if(!reservationExists(connection, reservationId)){
                System.out.println("Reservation not found for the given ID .");
                return;
            }

            String sql="Delete from reservations where reservation_id=" + reservationId;

            try (Statement stmt=connection.createStatement()){
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
        }catch(Exception e){
            System.out.println(e.getMessage());
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
        System.out.print("Exitint system ");
        int i=5;
        while (i!=0) {
            System.out.print(".");
            Thread.sleep(450);
            i--;
        }
        System.out.println();
        System.out.println("ThankYou for using Hotel Reservation System !!1");
       }
}
