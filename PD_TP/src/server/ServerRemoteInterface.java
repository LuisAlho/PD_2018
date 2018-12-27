
package server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import utils.User;

public interface ServerRemoteInterface extends Remote {
    
    
    public List<User> getLoggedUsers() throws RemoteException;
    
    public void registerListeners() throws RemoteException;
    
    public void deleteListeners() throws RemoteException;
    
    
    
    
}
