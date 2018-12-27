/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.logic;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nasyx
 */
public class FolderWatch implements Runnable {
    
    
    private final Observable client;
    
    
    public FolderWatch(Observable client){
        this.client = client;
    }
    
    @Override
    public void run() {
        
        System.out.println("Folder Watch service running");
       
        try {
            WatchService watchService = FileSystems.getDefault().newWatchService();
            
            Path path = Paths.get("./downloads");
            
            path.register(watchService,
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_DELETE,
                    StandardWatchEventKinds.ENTRY_MODIFY);
            
            WatchKey key;
            while ((key = watchService.take()) != null) {
                for (WatchEvent<?> event : key.pollEvents()) {
                    System.out.println("Event kind:" + event.kind() + ". File affected: " + event.context() + ".");
                }
                key.reset();
            }
        } catch (IOException ex) {
            Logger.getLogger(FolderWatch.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(FolderWatch.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
