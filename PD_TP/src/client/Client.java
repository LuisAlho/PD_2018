

package client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {
    
    /*
    
    Deve ser indicado aos utilizadores o endereço IP e o porto TCP de escuta de um
    determinado servidor
    
    */
    
    private int serverPort;
    private String serverIp;
    private Socket socket;
    
    
    public Client(String ip, int port){
    
        this.serverIp = ip;
        this.serverPort = port;
        
        this.start();
        
    }
    
    private void start(){
        
        
        
        try {
            socket = new Socket(InetAddress.getByName(this.serverIp), this.serverPort);
            
            socket.setSoTimeout(100000);
            
            
            //Send message to server
            
            
            
            
            
            
            //***********************
            
        } catch (UnknownHostException ex) {
            //Erro inetaddress
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            
            //Erro socket
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
    
    public static void main(String [] args) {
        
        
        String ip = "localhost";
        int port = 6400;
        
        if(args.length != 2){
            System.out.println("Invalid arguments! \nEx: java Server 'ip_servidor porto_servidor'\n");
            System.out.println("Get default values to IP: localhost , PORTO: 6400\n");
        }else{ 
            //Need to verify
            ip = args[0];
            port = Integer.parseInt(args[1]);
        }
        
        new Client(ip, port);

        
    }
    
    
}
