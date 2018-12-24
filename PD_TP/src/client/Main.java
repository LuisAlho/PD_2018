

package client;


import client.logic.ObservableClient;
import client.ui.LoginForm;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    
    
    private int serverPort;
    private String serverIp;
    private Socket socket;
 
    
    
    public static void main(String [] args) {
        
        
        int port = 4555;
        InetAddress server = null;
        
        try{
            
            if(args.length != 2){
                System.out.println("Invalid arguments! \nEx: java Server 'ip_servidor porto_servidor'\n");
                System.out.println("Get default values to IP: localhost , PORTO: 4555\n");
                server = InetAddress.getByName("localhost");
            }else{
                    
                    server = InetAddress.getByName(args[0]);
                    port = Integer.parseInt(args[1]);

            }
        }catch(NumberFormatException | UnknownHostException ex){
                System.out.println("Error: " + ex.getMessage());
                
                System.exit(0);
        }
        
        
        ObservableClient obsClient = new ObservableClient(server, port);
        if(!obsClient.startConnectionToServer()){
            
            System.out.println("Exit");
            System.exit(0);
        
        }
            
        
        
        System.out.println("Start UI");
        //Start client UI
        LoginForm LoginForm = new LoginForm(obsClient);
    }
    
    
}
