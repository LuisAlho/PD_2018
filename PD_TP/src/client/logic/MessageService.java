/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.logic;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import utils.User;

/**
 *
 * @author Nasyx
 */
public class MessageService implements Runnable {
    
    User user;
    ObservableClient client;
    DatagramPacket dtPacket;
    DatagramSocket dtSocket;

    public MessageService(User user, ObservableClient client) {
        this.user = user;
        this.client = client;
    }
    
    @Override
    public void run() {
        
        System.out.println("Start message service");
        
        
        
        
        
    
    }
    
}
