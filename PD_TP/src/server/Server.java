package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.Constants;
import utils.DBConnection;
import utils.Message;

public class Server extends Thread {
    
    private Socket clientSocket;
    
    private DBConnection db;
    
    public Server(Socket socket, String dbUrl, int dbPort) {
        
        this.clientSocket = socket;
        
        //get reference to MySQLDataBase
        db = DBConnection.getInstance(dbUrl, dbPort);
        
    }
    
    
    @Override
    public void run() {
        
        try {
            System.out.println("Processing new Client...");
            
            while(true){
            
                ObjectInputStream is = new ObjectInputStream(clientSocket.getInputStream());

                Message msg = (Message)is.readObject();
                System.out.println("Message: " + msg.getType());
                
                //Manage messages(commands) from user

                switch( msg.getType()){

                    case "LOGIN":
                        System.out.println("Ip: " + clientSocket.getInetAddress());
                        System.out.println("Port: " + clientSocket.getPort());
                        System.out.println("User: " + msg.getUsername());
                        
                        break;      
                    case "REGISTER":
                        System.out.println("Ip: " + clientSocket.getInetAddress());
                        System.out.println("Port: " + clientSocket.getPort());
                        System.out.println("User: " + msg.getUsername());
                        
                        break;  

                    case "START": 
                        break;

                    default: break;

                }
            }
            
        } catch (IOException ex) {
            System.out.println("Error IO: " + ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }



    
     public static void main(String [] args)  {
        
        String ip = "localhost";
        ServerSocket socket;
        Socket clientSocket;
        
        
        if(args.length != 1){
            System.out.println("Invalid arguments! \nEx: java Server 'ip_bd'\n");
            System.out.println("Get default values to IP: localhost\n");
        }else{ 
            ip = args[0];
        }
        
        System.out.println("Starting server....");
        
        try {
 
            System.out.println("Waiting for client....");
            socket = new ServerSocket(4555);
           
            while(true){
                
                clientSocket = socket.accept();                
                new Server(clientSocket, ip, Constants.BD_PORT).start(); //Create thread for each user
                
            }
        }catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }       
     }
}
