
package client.logic;


import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.Observable;

import java.util.logging.Level;
import java.util.logging.Logger;
import utils.Constants;
import utils.Message;
import utils.User;

/**
 *
 * @author 
 */
public class ObservableClient extends Observable implements Runnable { //Class que vai notificar os observers(vista) das modificações

    private InetAddress server;
    private int serverPort;
    private Socket socket;
    private boolean isConnected;
   
    
    public ObservableClient(InetAddress server, int port) {
        
        this.serverPort = port;
        this.server = server;
    }
    
    
    public boolean startConnectionToServer(){
    
        try {
            socket = new Socket(server, serverPort);
            //socket.setSoTimeout(Constants.TIMEOUT);
            
            if(socket.isConnected())
                new Thread(this).start();
            
            return socket.isConnected();
            
        } catch (SocketException ex) {
            Logger.getLogger(ObservableClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ObservableClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
    
    public void registerUser(User user){
        
        //Create Message
        
        Message msg = new Message();
        
        msg.setType(Constants.REGISTER);
        msg.setUser(user);
        
        
        // Send Message
        ObjectOutputStream out;
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(msg);
            out.flush();
            
            
        } catch (IOException ex) {
            Logger.getLogger(ObservableClient.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public void loginUser(User user){
        
        //Create Message
        
        Message msg = new Message();
        
        msg.setType(Constants.LOGIN);
        msg.setUser(user);
        
        
        // Send Message
        ObjectOutputStream out;
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(msg);
            out.flush();
            
            
        } catch (IOException ex) {
            
            System.out.println("Login client error");
            
            Logger.getLogger(ObservableClient.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public void listFolder(String path){
        
        System.out.println("Get list of files...");
    
        File f = new File(path);
        ArrayList<File> files = new ArrayList<>(Arrays.asList(f.listFiles()));
        
        System.out.println("List files: " + files.toString() );
    
    
    }
    
    

    @Override
    public void run() {
        
        System.out.println("Waiting messages");
        
        ObjectInputStream ois = null;
        try {
            
            
            while(true){
            
                ois = new ObjectInputStream(socket.getInputStream());
                Message msg = (Message)ois.readObject();
                System.out.println("Message: " + msg.getType());
                //Manage messages(commands) from user
                
                System.out.println("Observers: " + this.countObservers());
                
                switch( msg.getType()){
                    
                    case Constants.LOGIN_SUCCESSFULL:
                        System.out.println("Message: " + msg.getType());
                        setChanged();
                        notifyObservers(msg);
                        
                        break;
                        
                    case Constants.REGISTER_SUCCESSFULL:
                        System.out.println("Message: " + msg.getType());
                        
                        setChanged();
                        notifyObservers(msg);
                        
                        break;
                        
                    case Constants.LOGIN_FAIL:
                        System.out.println("Message: " + msg.getType());
                        setChanged();
                        notifyObservers(msg);
                        
                        break;
                        
                    case Constants.REGISTER_FAIL:
                        System.out.println("Message: " + msg.getType());
                        
                        setChanged();
                        notifyObservers(msg);
                        
                        break;
                        
                    default: break;
                    
                }
                
                //is.close();
            }  
        } catch (IOException ex) {
            Logger.getLogger(ObservableClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ObservableClient.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
//            try {
//                if(is != null)
//                    is.close();
//            } catch (IOException ex) {
//                Logger.getLogger(ObservableClient.class.getName()).log(Level.SEVERE, null, ex);
//            }
        }
    }
       
}
