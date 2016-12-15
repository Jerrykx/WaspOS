package processesManagement;

import java.util.HashMap;

import syncMethod.Lock;

public class PCB {
	
	protected int ProcessID;
	protected String ProcessName;
	protected int ProcessState;
	protected int BaseProcessPriority;
	protected int CurrentProcessPriority;
	protected boolean blocked;
	protected int whenCameToList;
	protected int howLongWaiting;
	
	
	public String receivedMsg;
	public int commandCounter;
	public HashMap<String, Integer> labels = new HashMap<String, Integer>();
	public int A;
	public int B;
	public int C;
	public int D;

}