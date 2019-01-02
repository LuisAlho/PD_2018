
package client.logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;


public class UploadFileService implements Runnable {
    
    public Socket socketToClient;
    public String filename;
    public static final int MAX_SIZE = 4000;
    
    public UploadFileService(Socket socketToClient) {
        this.socketToClient = socketToClient;
    }
    

    @Override
    public void run() {
        
        BufferedReader in;
        OutputStream out;
        Socket socketToClient = null;        
        byte []fileChunck = new byte[MAX_SIZE];
        int nbytes;
        String requestedFileName, requestedCanonicalFilePath = null;
        FileInputStream requestedFileInputStream = null;
        
        System.out.println("Download file: " + filename);
        
        File localDirectory = new File("../downloads");


        try{

            socketToClient.setSoTimeout(10000);

            in = new BufferedReader(new InputStreamReader(socketToClient.getInputStream()));
            out = socketToClient.getOutputStream();

            requestedFileName = in.readLine();

            System.out.println("Recebido pedido para: " + requestedFileName);

            requestedCanonicalFilePath = new File(localDirectory+File.separator+requestedFileName).getCanonicalPath();

            if(!requestedCanonicalFilePath.startsWith(localDirectory.getCanonicalPath()+File.separator)){
                System.out.println("Nao e' permitido aceder ao ficheiro " + requestedCanonicalFilePath + "!");
                System.out.println("A directoria de base nao corresponde a " + localDirectory.getCanonicalPath()+"!");
            }

            requestedFileInputStream = new FileInputStream(requestedCanonicalFilePath);
            System.out.println("Ficheiro " + requestedCanonicalFilePath + " aberto para leitura.");

            while((nbytes = requestedFileInputStream.read(fileChunck))>0){                        

                out.write(fileChunck, 0, nbytes);
                out.flush();

            }     

            System.out.println("Transferencia concluida");

        }catch(FileNotFoundException e){   //Subclasse de IOException                 
            System.out.println("Ocorreu a excepcao {" + e + "} ao tentar abrir o ficheiro " + requestedCanonicalFilePath + "!");                   
        }catch(IOException e){
            System.out.println("Ocorreu a excepcao de E/S: \n\t" + e);                       
        }

        if(requestedFileInputStream != null){
            try {
                requestedFileInputStream.close();
            } catch (IOException ex) {}
        }

        try{
             socketToClient.close();
         } catch (IOException e) {}
                
            
    }
    
    
    
}
