
package server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import utils.User;


public class ServerRemote extends UnicastRemoteObject implements ServerRemoteInterface {
    
    Registry r;
    private Server server;
    
    
    public ServerRemote(Server server) throws RemoteException{
        super();
        this.server = server;
        
    }

    @Override
    public List<User> getLoggedUsers() throws RemoteException {
        
        return null;
        
    }

    @Override
    public void registerListeners() throws RemoteException {
        System.out.println("ola");
    }

    @Override
    public void deleteListeners() throws RemoteException {
        System.out.println("ola");
    }
    
}
