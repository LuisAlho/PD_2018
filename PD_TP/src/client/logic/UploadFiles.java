
package client.logic;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;



public class UploadFiles {
    
    
    public static final int MAX_SIZE = 4000;
    public static final int TIMEOUT = 5; //segundos
   
    protected ServerSocket serverSocket;
    
    public Socket socketToClient;

    public UploadFiles(ServerSocket socket) {
        serverSocket = socket;
        
    }
    
    public void processRequests(){   

        if(serverSocket == null){
            return;
        }
        System.out.println("Servidor de carregamento de ficheiros iniciado...");
  
        while(true){

            try{

                socketToClient = serverSocket.accept();
                
                new Thread(new UploadFileService(socketToClient)).start();

            }catch(IOException e){
                System.out.println("Ocorreu uma excepcao no socket enquanto aguardava por um pedido de ligacao: \n\t" + e);
                System.out.println("O servidor vai terminar...");
                return;
            }
        }
    } 
}
