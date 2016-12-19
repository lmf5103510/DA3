import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class Process {
	public static void main(String[] args) throws Exception {
		int i = Integer.parseInt(args[0]);
		int total_pro = Integer.parseInt(args[1]);
		int f = Integer.parseInt(args[2]);
		boolean correct = Boolean.parseBoolean(args[3]);
		int port = i+1099;
		
		System.setSecurityManager(new RMISecurityManager());		
		try{
			LocateRegistry.createRegistry(port);
		} catch (RemoteException e){
			e.printStackTrace();
		}
		
		Byzantine b = new Byzantine(i,total_pro,f,correct);
		Naming.rebind("rmi://localhost:"+port+"/BZT"+i, b);		
		Byzantine_RMI proc = (Byzantine_RMI) Naming.lookup("rmi://localhost:"+port+"/BZT"+i);	
		
		try{
			Thread.sleep(2500);
			proc.setProcessesNetwork();
			while(!proc.isOver()){
				proc.nBroadcast();
				Thread.sleep(2500);
			}
		}catch (Exception e){
			e.printStackTrace();
			System.out.println("No!");
		}		
	}	
}
