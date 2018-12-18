
package client.logic;


import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

import java.util.Observable;

import java.util.logging.Level;
import java.util.logging.Logger;
import utils.Constants;

/**
 *
 * @author 
 */
public class ObservableClient extends Observable { //Class que vai notificar os observers(vista) das modificações

    private InetAddress server;
    private int serverPort;
    private Socket socket;
   
    
    public ObservableClient(InetAddress server, int port) {
        
        this.serverPort = port;
        this.server = server;
        
        
        
    }
    
    
    public boolean startConnectionToServer(){
    
        try {
            socket = new Socket(server, serverPort);
            socket.setSoTimeout(Constants.TIMEOUT);
            
            return socket.isConnected();
            
        } catch (SocketException ex) {
            Logger.getLogger(ObservableClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ObservableClient.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            return false;
        }
            
        
    
    }

    
    
    
}
