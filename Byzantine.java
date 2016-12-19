import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.CopyOnWriteArrayList;

public class Byzantine extends UnicastRemoteObject implements Byzantine_RMI  {

	private static final long serialVersionUID = -8000564594456251818L;
	int pro_id;
	int n;
	boolean correct;
	boolean decided;
	boolean over;
	int round;
	int v;
	int f;
	Byzantine_RMI proc[];
	CopyOnWriteArrayList<Messages> notification = new CopyOnWriteArrayList<Messages>();
	CopyOnWriteArrayList<Messages> proposal = new CopyOnWriteArrayList<Messages>();
	
	protected Byzantine(int i, int j, int k, boolean c) throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
		pro_id = i;
		n = j;
		f = k;
		correct = c;
		round = 1;
		decided = false;
		over = false;
		proc = new Byzantine_RMI[j];
		v = (int)(Math.random()*2);
		if(correct){
			System.out.println("I am reliable general!");	
		}
		else{
			System.out.println("I will destroy you guys! hahahaaa!");
		}
		
	}

	public void setProcessesNetwork() throws RemoteException{
		// TODO Auto-generated method stub
		for(int i=0; i<n; i++){
			try{
				int port = i+1099;
				proc[i] = (Byzantine_RMI) Naming.lookup("rmi://localhost:"+port+"/BZT"+i);
			} catch(NotBoundException e){
				
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
			}
		}
	}

	public boolean isOver() throws RemoteException {
		return over;
	}
	@Override
	public void nBroadcast() throws RemoteException {
		// TODO Auto-generated method stub
		if(correct){
			System.out.println("I want to do "+v+" this round "+round+" .");
			Messages s = new Messages();
			s.r = round;
			s.v = v;
			s.t = "N";
			for(Byzantine_RMI p : proc){
				p.nReceive(s);
			}
		}
		else{
			int method = (int)Math.random()*2;
			if(method == 0){
				
			}
			else{
				Messages s = new Messages();
				s.r = round;
				s.t = "N";
				for(Byzantine_RMI p : proc){
					s.v = (int)Math.random()*2;
					p.nReceive(s);
				}
			}
		}
	}

	@Override
	public void nReceive(Messages msg) throws RemoteException {
		// TODO Auto-generated method stub
		if(notification.size() != n-f){
			notification.add(msg); 
			if(notification.size() == n-f){
				goProposal();
			}
		}
	}
	
	
	public void goProposal() throws RemoteException{
		
		if(correct){
			int N_0 = 0, N_1 = 0;
			float C_1 = ((float)(n+f))/2;
			for(Messages s : notification){
				if(s.v == 0){
					N_0++;
				}
				if(s.v == 1){
					N_1++;
				}
			}
			notification.clear();
			
			if((float)N_0 >C_1){
				Messages s = new Messages();
				s.r = round;
				s.v = 0;
				s.t = "P";
				System.out.println("I propose to do "+s.v+" .");
				for(Byzantine_RMI p : proc){
					p.pReceive(s);
				}
			}
			else if((float)N_1 >C_1){
				Messages s = new Messages();
				s.r = round;
				s.v = 1;
				s.t = "P";
				System.out.println("I propose to do "+s.v+" .");
				for(Byzantine_RMI p : proc){
					p.pReceive(s);
				}
			}
			else{
				Messages s = new Messages();
				s.r = round;
				s.v = 2;
				s.t = "P";
				System.out.println("I cannot make a proposal.");
				for(Byzantine_RMI p : proc){
					p.pReceive(s);
				}
			}
		}
		// If the general is traitor, never send any message or send fake message
		else{
			notification.clear();
			int method = (int)Math.random()*2;
			if(method == 0){
				
			}
			else{
				Messages s = new Messages();
				s.r = round;
				s.t = "P";
				for(Byzantine_RMI p : proc){
					s.v = (int)Math.random()*2;
					p.pReceive(s);
				}
			}
		}
	}

	@Override
	public void pReceive(Messages msg) throws RemoteException {
		// TODO Auto-generated method stub
		if(proposal.size() != n-f){
			proposal.add(msg); 
			if(proposal.size() == n-f){
				goDecision();
			}
		}
	}
	
	public void goDecision(){
		if(decided){
			System.out.println("It's over!");
			over = true;
		}
		else{
			int P_0 = 0, P_1 = 0;
			for(Messages s : proposal){
				if(s.v == 0){
					P_0++;
				}
				if(s.v == 1){
					P_1++;
				}
			}
			proposal.clear();
			
			if(P_0 > f || P_1 > f){
				if(P_0 > f){
					v = 0;
					if(P_0 > 3*f){
						System.out.println("I decide to do "+v+" !");
						decided = true;
					}
				}
				else if(P_1 > f){
					v = 1;
					if(P_1 > 3*f){
						System.out.println("I decide to do "+v+" !");
						decided = true;
					}
				}
			}
			else{
				v = (int)(Math.random()*2); 
			}
			
			round++;
		}
	}
	
	
}
