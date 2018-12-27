/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.logic;

/**
 *
 * @author Nasyx
 */
public class DownloadFiles implements Runnable{
    
    ObservableClient client;
    String name;
    

    public DownloadFiles(ObservableClient client, String name) {
        this.client = client;
        this.name = name;
        
    }
    
        

    @Override
    public void run() {
        
        
        System.out.println("Start downloading File");
        
        try{
        
            //TODO download file
        
        
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }finally{
            //Inform client if download was finished ok

            //client.finishDownload(false);
        }
        
        
    }
    
}
