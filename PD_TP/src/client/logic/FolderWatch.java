/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.logic;

import java.util.Observable;

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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
