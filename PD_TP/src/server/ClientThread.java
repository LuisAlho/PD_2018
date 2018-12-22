/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

/**
 *
 * @author Nasyx
 */
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
                        System.out.println("Ip: " + socketToClient.getInetAddress());
                        System.out.println("Port: " + socketToClient.getPort());
                        System.out.println("User: " + msg.getUser().toString());
                        
                        user = server.loginClient(msg.getUser().getUsername(), msg.getUser().getPassword());
                        
                        if(user != null){
                            msg.setType(Constants.LOGIN_SUCCESSFULL);
                        }
                        else{
                            msg.setType(Constants.LOGIN_FAIL);
                        }
                        sendMessage(msg);
                        
                        break;      
                    case "REGISTER":
                        System.out.println("Ip: " + socketToClient.getInetAddress());
                        System.out.println("Port: " + socketToClient.getPort());
                        System.out.println("User: " + msg.getUser().toString());
                        if(server.registerClient(msg.getUser())){
                            msg.setType(Constants.REGISTER_SUCCESSFULL);
                            
                        }else{
                            msg.setType(Constants.REGISTER_FAIL);
                        }
                        sendMessage(msg);
                        
                        
                        
                        break;

                    default: break;
                }
            }
            
        } catch (IOException ex) {
            System.out.println("Error IO: " + ex);
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }finally {

            this.server.deleteObserver(this);
            if(this.user != null)
                this.server.setLoggedIn(this.user.getUsername(), false);
                     
        }
    }

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
