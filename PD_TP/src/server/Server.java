package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.util.List;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.Constants;
import utils.DBConnection;
import utils.Message;
import utils.User;

public class Server extends Observable {
    
    private ServerSocket serverSocket;
    
    
    private final DBConnection db;
    
    public Server(String dbUrl, int dbPort) {
        
        
        //get reference to MySQLDataBase
        db = DBConnection.getInstance(dbUrl, dbPort);
        
    }

     public static void main(String [] args)  {
        
        String ip = "localhost";
//        ServerSocket socket;
//        Socket clientSocket;
        
        if(args.length != 1){
            System.out.println("Invalid arguments! \nEx: java Server 'ip_bd'\n");
            System.out.println("Get default values to IP: localhost\n");
        }else{ 
            ip = args[0];
        }
        Server server =  null;
//        try{
            
            server = new Server(ip, Constants.BD_PORT);
        
            RemoteServer remoteServer;
            //remoteServer = new RemoteServer();
        
//        }catch(RemoteException ex){
//            System.out.println("Error: " + ex.getMessage());
//            System.exit(1);
//        }
        
        server.startServer();
        
        
     }
     
     public void startServer(){
         
        System.out.println("Starting server....");
         
        try {
 
            System.out.println("Waiting for client....");
            serverSocket = new ServerSocket(4555);
           
            while(true){
                               
                Socket socketToClient = serverSocket.accept();                
                new ClientThread(socketToClient, this).start(); //Create thread for each user
                
            }
        }catch (IOException ex) {
           Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
         
        }     
     }
     
     protected boolean registerClient(User user){
     
        try {
            return db.registerUser(user);

        } catch (SQLException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
     
     }
     
     
     protected User loginClient(String username, String password){
     
        User user = null;

        try {
            user = db.loginUser(username, password);
            if( user != null)
                return user;                
            return null;
  
        } catch (SQLException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }       
                
     }
     
     protected void setLoggedIn(String username, boolean loggedIn){
     
         this.db.setUserLoggedIn(username, false);
     
     }
     
     
     protected void setListOfFiles(User user, List listOfFiles){
         
         System.out.println("Update list of files in DB");
     
         if((!listOfFiles.isEmpty()) && (listOfFiles != null))
            db.setUserFilesList(user, listOfFiles);
         
     }

    protected List<User> getLoggedUsers() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
