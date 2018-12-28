
package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.Constants;
import utils.Message;
import utils.User;

//MODULO PARA FAZER TRATAMENTO DA COMUNICAÇÂO COM O CLIENT

public class ClientThread extends Thread implements Observer{
    
    User user;
    Socket socketToClient;
    Server server;
    ObjectOutputStream oos = null;
    
    public ClientThread(Socket socket, Server server){
        this.socketToClient = socket;
        this.server = server;
        this.server.addObserver(this);
    }

    
    
    
    //RECEIVE MESSAGE FROM CLIENT
    @Override
    public void run() {
        
        ObjectInputStream is = null;
        
        try {
            System.out.println("Processing new Client...");
            
            
            while(true){
            
                is = new ObjectInputStream(socketToClient.getInputStream());
                Message msg = (Message)is.readObject();
                System.out.println("Message: " + msg.getType());
                
                //Manage messages(commands) from user

                switch( msg.getType()){

                    case "LOGIN":
          
                        user = server.loginClient(msg.getUser());
 
                        if(user != null){
                            System.out.println("User login: " + user.toString());
                            server.getListUsers().add(user);
                            msg.setType(Constants.LOGIN_SUCCESSFULL);
                            msg.setUser(user);
                        }
                        else{
                            msg.setType(Constants.LOGIN_FAIL);
                        }
                        sendMessage(msg);
                        
                        break;
                        
                    case "REGISTER":
                        
                        if(server.registerClient(msg.getUser())){
                            System.out.println("Registado com sucesso");
                            msg.setType(Constants.REGISTER_SUCCESSFULL);
                            
                        }else{
                            msg.setType(Constants.REGISTER_FAIL);
                        }
                        sendMessage(msg);

                        break;
                        
                    case Constants.SET_LIST_OF_FILES:
                        
                        System.out.println("List of files received: " + msg.getType());
                        server.setListOfFiles(msg.getUser(), msg.getListOfFiles());
                            
                        
                        break;

                    default: break;
                }
            }
            
        } catch (IOException ex) {
            System.out.println("Error IO: " + ex);
            System.out.println("Clean user: " + user);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }catch(Exception ex){
            System.out.println("Error: " + ex.getMessage());
        
        }finally {
            System.out.println("Finally");
            this.server.deleteObserver(this);
            if(this.user != null){
                System.out.println("Clean user: " + this.user.getUsername());
                //this.server.setLoggedIn(this.user.getUsername(), false);
                this.server.userLogout(user);
                
            }
//                this.server.getListUsers().forEach( (User item) -> {
//                    if(item.getUsername().equals(user.getUsername())){
//                        this.server.getListUsers().remove(item);
//                    }
//                });
                     
        }
    }

    
    //RECEIVE NOTIFICATIONS FROM SERVER
    @Override
    public void update(Observable o, Object arg) {
        
        System.out.println("UPDATE COMUNICATION");

        if(arg != null){
        
            if(arg instanceof Message ){

                Message msg = (Message)arg;
                System.out.println("MESSAGE TYPE: " + msg.getType());
                
//                try {
//                    
//                    oos = new ObjectOutputStream(socketToClient.getOutputStream());
//                    
//                    oos.writeObject(msg);
//                    oos.flush();
//                    
//                } catch (IOException ex) {
//                    Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
//                    System.out.println("Error send message to client");
//                }

            }else{

                System.out.println("WRONG MESSAGE TYPE RECEIVED");
            }
        }else{
        
            System.out.println("WRONG MESSAGE TYPE RECEIVED");
            
        }
       
    }
    
    private void sendMessage(Message msg) throws IOException{
        
        oos = new ObjectOutputStream(socketToClient.getOutputStream());            
        oos.writeObject(msg);
        oos.flush();
    
    
    }
    
    
    
    
    
    
    
}
