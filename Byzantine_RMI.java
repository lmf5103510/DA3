import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Byzantine_RMI extends Remote{
	
	public void setProcessesNetwork() throws RemoteException;
	public void nBroadcast() throws RemoteException;
	public void nReceive(Messages msg) throws RemoteException;
	public void pReceive(Messages msg) throws RemoteException;
	public boolean isOver() throws RemoteException;
	
}
