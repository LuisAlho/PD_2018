
package client.logic;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.Message;


public class ClientUdpService implements Runnable {
    
    private ObservableClient client;
    private DatagramSocket dtSocket;
    private DatagramPacket dtPacket;
    private Message msg;

    public ClientUdpService(ObservableClient client, DatagramSocket dtSocket) throws SocketException {
        this.client = client;
        dtSocket = new DatagramSocket();
       
    }

    @Override
    public void run() {
        
         
        while(true){
        
            try {
                dtSocket.receive(dtPacket);
                
                Object obj = dtPacket.getData();
                
                if(obj instanceof Message){
                    msg = (Message)obj;
                    
                    switch(msg.getType()){
                    
                    
                        case "BEAT":
                        
                            break;
                            
                        default:
                            
                            break;

                    }
                    
                }else{
                    System.out.println("Wrong Message received via UDP");
                }
                
                
                
                
            } catch (IOException ex) {
                Logger.getLogger(ClientUdpService.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Error receiving DatagramPacket: " + ex.getMessage());
            }
        }
    }
    
}
