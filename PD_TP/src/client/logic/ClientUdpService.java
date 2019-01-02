
package client.logic;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.Message;


public class ClientUdpService implements Runnable {
    
    private final ObservableClient client;
    private final DatagramSocket dtSocket;
    private DatagramPacket dtPacket;
    private Message msg;

    public ClientUdpService(ObservableClient client, DatagramSocket dtSocket) {
        this.client = client;
        this.dtSocket = dtSocket;
    }

    @Override
    public void run() {
        
        System.out.println("Inicia UDP service" + dtSocket);
        
        while(true){
        
            try {
                
                dtSocket.receive(dtPacket);
                
                Object obj = dtPacket.getData();
                
                if(obj instanceof Message){
                    msg = (Message)obj;
                    System.out.println("UDP: " + msg.getType());
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
