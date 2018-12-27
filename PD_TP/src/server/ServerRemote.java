
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
    
    public void startRemoteServer(){
    
        try{
            //Init registry
            try{

                System.out.println("Tentativa de lancamento do registry no porto " + Registry.REGISTRY_PORT + "...");
                r = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
                System.out.println("Registry lancado!");

            }catch(RemoteException e){
                System.out.println("Registry provavelmente ja' em execucao!");
                r = LocateRegistry.getRegistry();

            }

            // Cria e lanca o servico,
            System.out.println("Servico vista criado e em execucao ("+this.getRef().remoteToString()+"...");

            // Regista o servico para que os clientes possam encontra'-lo, ou seja,
            // obter a sua referencia remota (endereco IP, porto de escuta, etc.).
            String serviceName = "ObservacaoSistema";
            r.bind(serviceName, this);
            System.out.println("Servico " + serviceName + "  registado no registry...");

        }catch(RemoteException e){
            System.out.println("Erro remoto - " + e);
            System.exit(1);
        }catch(Exception e){
            System.out.println("Erro - " + e);
            System.exit(1);
        }
    }

    @Override
    public List<User> getLoggedUsers() throws RemoteException {
        
        return server.getLoggedUsers();
        
    }

    @Override
    public void registerListeners() throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteListeners() throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
