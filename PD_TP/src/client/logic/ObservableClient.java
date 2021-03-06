
package client.logic;


import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.util.Observable;

import java.util.logging.Level;
import java.util.logging.Logger;
import utils.Constants;
import utils.Files;
import utils.Message;
import utils.User;

/**
 *
 * @author 
 */
public class ObservableClient extends Observable implements Runnable { //Class que vai notificar os observers(vista) das modificações

    private final InetAddress server;
    private final int serverPort;
    private Socket socket;
    private boolean isConnected;
    private final FolderWatch folderWatch;
    private DownloadFiles dFiles;
    private DatagramSocket dtSocket;
    public User user;
    private List<Files> listOfFiles;
    private List<User> listOfLoggedUsers;
    private UploadFiles uploadFiles;
    private ServerSocket serverSocket;
    private ClientUdpService udpService;
   
    
    public ObservableClient(InetAddress server, int port) {
        
        this.serverPort = port;
        this.server = server;
        
        folderWatch = new FolderWatch(this);
    }
    
    
    public boolean startConnectionToServer(){
        
        try {
            dtSocket = new DatagramSocket();
            serverSocket = new ServerSocket(0);
            
            
        } catch (IOException ex) {
            Logger.getLogger(ObservableClient.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        System.out.println("ServerSocket: " + serverSocket);
        uploadFiles = new UploadFiles(serverSocket);
        //udpService = new ClientUdpService(this, dtSocket);
        
        
        new Thread(udpService).start();  
        new Thread(folderWatch).start();
    
        try {
            socket = new Socket(server, serverPort);
            //socket.setSoTimeout(Constants.TIMEOUT);
            
            if(socket.isConnected())
                new Thread(this).start();
            
            return socket.isConnected();
            
        } catch (SocketException ex) {
            Logger.getLogger(ObservableClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ObservableClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
    
    public void registerUser(User user){
        
        //Create Message
        Message msg = new Message();
        
        msg.setType(Constants.REGISTER);
        msg.setUser(user);
        
        
        // Send Message
        ObjectOutputStream out;
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(msg);
            out.flush();
            
            
        } catch (IOException ex) {
            Logger.getLogger(ObservableClient.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public void loginUser(User user){
        
        //Create Message
        
        Message msg = new Message();
        user.setIp(this.serverSocket.getInetAddress().toString());
        user.setPorto_tcp(this.serverSocket.getLocalPort());
        //TODO change this
        user.setPorto_udp(dtSocket.getLocalPort());
        
        msg.setType(Constants.LOGIN);
        msg.setUser(user);
        
        
        // Send Message
        ObjectOutputStream out;
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(msg);
            out.flush();
            
            
        } catch (IOException ex) {
            
            System.out.println("Login client error");
            
            Logger.getLogger(ObservableClient.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    
    public void logoutUser(User user){
        
        //Create Message
        
        Message msg = new Message();
       
        
        msg.setType(Constants.LOGOUT);
        msg.setUser(user);
        
        
        // Send Message
        ObjectOutputStream out;
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(msg);
            out.flush();
            
            
        } catch (IOException ex) {
            System.out.println("Logout client error");
            Logger.getLogger(ObservableClient.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public void listFolder(String path){
        
        System.out.println("Get list of files...");
            
        
        try{
        
            File f = new File(path);
            ArrayList<File> files = new ArrayList<>(Arrays.asList(f.listFiles()));

            System.out.println("List files: " + files.toString() );

            listOfFiles = new ArrayList();
            files.forEach((item) -> {
                listOfFiles.add(new Files(item.getName(),item.length()));
            });
            
            sendFilesList();
        
        }catch(Exception ex){
            System.out.println("Erro read file: " + path);
            System.out.println("Error... " + ex.getMessage());
        }
 
    }
    
    public void sendFilesList(){
    
        System.out.println("Send list of files...");
            
        
        Message msg = new Message();
        
        msg.setType(Constants.SET_LIST_OF_FILES);
        msg.setUser(user);
        
        //TODO verifu if is not null
        msg.setListOfFiles(listOfFiles);
        
        //notifica oberserver
        System.out.println("Notifiers: " + this.countObservers());
        this.setChanged();
        this.notifyObservers(msg);
        
        
        // Send Message
        ObjectOutputStream out;
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(msg);
            out.flush();
            
            
        } catch (IOException ex) {
            
            System.out.println("Login client error");
            
            Logger.getLogger(ObservableClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
    
    
    //RECEIVE MESSAGES FROM SERVER
    @Override
    public void run() {
        
        System.out.println("Waiting messages");
        
        ObjectInputStream ois = null;
        try {
            
            
            while(true){
            
                ois = new ObjectInputStream(socket.getInputStream());
                Message msg = (Message)ois.readObject();
                System.out.println("Message: " + msg.getType());
                //Manage messages(commands) from user
                
                System.out.println("Observers: " + this.countObservers());
                
                switch( msg.getType()){
                    
                    case Constants.LOGIN_SUCCESSFULL:
                        System.out.println("Message: " + msg.getType());
                        
                        user = msg.getUser();

                        setChanged();
                        notifyObservers(msg);
                        
                        break;
                        
                    case Constants.REGISTER_SUCCESSFULL:
                        System.out.println("Message: " + msg.getType());
                        
                        setChanged();
                        notifyObservers(msg);
                        
                        break;
                        
                    case Constants.LOGIN_FAIL:
                        System.out.println("Message: " + msg.getType());
                        setChanged();
                        notifyObservers(msg);
                        
                        break;
                        
                    case Constants.REGISTER_FAIL:
                        System.out.println("Message: " + msg.getType());
                        
                        setChanged();
                        notifyObservers(msg);
                        
                        break;
                        
                    case Constants.LOGOUT:
                        
                        System.out.println("Message: " + msg.getType());

                        setChanged();
                        notifyObservers(msg);
                        
                        break;
                        
                        
                    case Constants.GET_FILES_DOWNLOAD:
                        
                        System.out.println("Received List of Download Files");
                        
                        setChanged();
                        notifyObservers(msg);
                        
                        break;
                        
                    case Constants.GET_LOGGED_USERS:
                        
                        System.out.println("Received list of users logged");
                        
                        this.listOfLoggedUsers = msg.getListOfUsers();
                        
                        break;
                        
                        
                    case "UPDATE_LOGIN":
                        System.out.println("Received list of users logged");
                        
                        this.listOfLoggedUsers = msg.getListOfUsers();
                        
                        break;
                    
                        
                    default: break;
                    
                }
                
                //is.close();
            }  
        } catch (IOException ex) {
            Logger.getLogger(ObservableClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ObservableClient.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
//            try {
//                if(is != null)
//                    is.close();
//            } catch (IOException ex) {
//                Logger.getLogger(ObservableClient.class.getName()).log(Level.SEVERE, null, ex);
//            }
        }
    }

    public void getListForDownload() {
        
        
        //Create Message
        
        Message msg = new Message();
       
        
        msg.setType(Constants.GET_FILES_DOWNLOAD);
        msg.setUser(user);
        
        
        // Send Message
        ObjectOutputStream out;
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(msg);
            out.flush();
            
            
        } catch (IOException ex) {
            System.out.println("Logout client error");
            Logger.getLogger(ObservableClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
    }

    public void getMyHistoryFiles() {
        
        //Create Message
        
        Message msg = new Message();
       
        
        msg.setType(Constants.GET_HISTORY_FILES);
        msg.setUser(user);
        
        
        // Send Message
        ObjectOutputStream out;
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(msg);
            out.flush();
            
            
        } catch (IOException ex) {
            System.out.println("Logout client error");
            Logger.getLogger(ObservableClient.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }
    
    
    public void downloadFile(String fileName, String username, ObservableClient client){
        
        String ip;
        int port;
        
        for (User u: this.listOfLoggedUsers){
            
            if(u.getUsername().equals(username) && (u.getUsername() == null ? user.getUsername() != null : !u.getUsername().equals(user.getUsername()))){
                ip = u.getIp();
                port = u.getPorto_tcp();
                new Thread(new DownloadFiles(client, fileName, ip, port)).start();
                break;
            }
            
        }
        
    }
    
    public void getLoggedUsers(){
    
         //Create Message
        
        Message msg = new Message();
       
        
        msg.setType(Constants.GET_LOGGED_USERS);
        msg.setUser(user);
        
        
        // Send Message
        ObjectOutputStream out;
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(msg);
            out.flush();
            
            
        } catch (IOException ex) {
            System.out.println("GET LIST LOGGED USERS");
            Logger.getLogger(ObservableClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
       
}
