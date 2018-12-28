package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteServer;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.util.ArrayList;
import java.util.HashMap;
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
    private List<User> listUsers;
    private HashMap<Thread, User> userThreadList;
    
    
    
    private final DBConnection db;
    
    public Server(String dbUrl, int dbPort) {
        
        userThreadList =  new HashMap();
        
        listUsers = new ArrayList();
        //get reference to MySQLDataBase
        db = DBConnection.getInstance(dbUrl, dbPort);
        
    }

     public static void main(String [] args)  {
        
        String ip = "localhost";
        Registry r;
//        ServerSocket socket;
//        Socket clientSocket;
        
        if(args.length != 1){
            System.out.println("Invalid arguments! \nEx: java Server 'ip_bd'\n");
            System.out.println("Get default values to IP: localhost\n");
        }else{ 
            ip = args[0];
        }
        Server server =  null;
        server = new Server(ip, Constants.BD_PORT);
        try {
            ServerRemote remoteServer = new ServerRemote();
            
            try{
            //Init registry
            try{

                System.out.println("Tentativa de lancamento do registry no porto " + Registry.REGISTRY_PORT + "...");
                r = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
                System.out.println("Registry lancado!");

            }catch(RemoteException e){
                System.out.println("Registry provavelmente ja' em execucao!");
                r = LocateRegistry.getRegistry();

            }

            // Cria e lanca o servico,
            System.out.println("Servico vista criado e em execucao (" + remoteServer.getRef().remoteToString()+"...");

            // Regista o servico para que os clientes possam encontra'-lo, ou seja,
            // obter a sua referencia remota (endereco IP, porto de escuta, etc.).
            String serviceName = "ObservacaoSistema";
            r.bind(serviceName, remoteServer);
            System.out.println("Servico " + serviceName + "  registado no registry...");

        }catch(RemoteException e){
            System.out.println("Erro remoto - " + e);
            System.exit(1);
        }catch(Exception e){
            System.out.println("Erro - " + e);
            System.exit(1);
        }
            
            
        } catch (RemoteException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        server.startServer();
        
        
     }
     
     public void startServer(){
         
        System.out.println("Starting server....");
        
        
         
        try {
            
            System.out.println("Getting list of logged users");
            this.listUsers = db.listUsersLoggedIn(true);
 
            System.out.println("Waiting for client....");
            serverSocket = new ServerSocket(4555);
           
            while(true){
                               
                Socket socketToClient = serverSocket.accept();                
                 //Create thread for each user
                new ClientThread(socketToClient, this).start();
                //TODO add to hash map
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
     
     
     protected User loginClient(User user){
     
        User new_user = null;

        try {
            new_user = db.loginUser(user);
            if( user != null)
                return new_user;                
            return null;
  
        } catch (SQLException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }       
                
     }
     
     protected void userLogout(User user){
     
         if(db.userLogout(user.getUsername())){
             System.out.println("user logout com sucesso");
         }else{
             System.out.println("user logout falhou");
         }
     
     }
     
     
     protected void setLoggedIn(String username, boolean loggedIn){
     
         this.db.setUserLoggedIn(username, loggedIn);
     
     }
     
     
     protected void setListOfFiles(User user, List listOfFiles){
         
         System.out.println("Update list of files in DB");
     
         if((!listOfFiles.isEmpty()) && (listOfFiles != null))
            db.setUserFilesList(user, listOfFiles);
         
     }

    protected List<User> getLoggedUsers() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<User> getListUsers() {
        return listUsers;
    }

    public void setListUsers(List<User> listUsers) {
        this.listUsers = listUsers;
    }
    
    
    

}
