/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.logic;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nasyx
 */
public class DownloadFiles implements Runnable{
    
    ObservableClient client;
    String fileName;
    String ip;
    int port;

    public DownloadFiles(ObservableClient client, String fileName, String ip, int port) {
        this.client = client;
        this.fileName = fileName;
        this.ip = ip;
        this.port = port;
    }
    

    
    
        

    @Override
    public void run() {
        
        byte [] fileChunck = new byte[4096];
        FileOutputStream localFileOutputStream = null;
        
        System.out.println("Start downloading File: " + fileName);
        File localDirectory = new File("../downloads");
        String localFilePath = null;
        try {
            localFilePath = localDirectory.getCanonicalPath()+File.separator+fileName;
            localFileOutputStream = new FileOutputStream(localFilePath);
            
            
        } catch (IOException ex) {
            Logger.getLogger(DownloadFiles.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("Ficheiro " + localFilePath + " criado.");
        
        try{
        
            InetAddress serverAddr = InetAddress.getByName(ip);
               
            Socket socket = new Socket(serverAddr,port);

            //socket.setSoTimeout(10000);

            InputStream in = socket.getInputStream();
//            PrintWriter pout = new PrintWriter( socket.getOutputStream(), true );
//
//            pout.println(fileName);
//            pout.flush();
            int nbytes;

            while ((nbytes = in.read(fileChunck)) > 0) {
                localFileOutputStream.write(fileChunck, 0, nbytes);
            }

            System.out.println("Transferencia concluida.");
        
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }finally{
            //Inform client if download was finished ok

            //client.finishDownload(false);
        }
        
        
    }
    
}
