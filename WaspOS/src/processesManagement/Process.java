package processesManagement;

public class Process {

	//===ZMIENNE=========================================================================================
	
		//private String ProgramPath;
		
		public PCB pcb = new PCB();
		
		
	//---zmienne pomocnicze------------------------------------------------------------------------------
		
		private PriorityOverseer priorityOverseer = new PriorityOverseer();
		
		private ProcessStateOverseer stateOverseer = new ProcessStateOverseer();
		
		private int basePriority;
	
	//===METODY==========================================================================================
	
	//---Stworz nowy proces------------------------------------------------------------------------------
		
	protected void CreateProcess(int ID,String name, int number){
		
		pcb.ProcessState = stateOverseer.newbie;
		
		pcb.ProcessID = ID;
		
		pcb.ProcessName = name;
		
		basePriority = priorityOverseer.RollPriority();
		
		pcb.BaseProcessPriority = basePriority;
		
		pcb.CurrentProcessPriority = basePriority;
		
		pcb.blocked = false;
		
		pcb.A = 0;
		
		pcb.B = 0;
		
		pcb.C = 0;
		
		pcb.D = 0;
			
		pcb.commandCounter = 0;

		pcb.whenCameToList = number;
		
		pcb.ProcessState = stateOverseer.ready;
		
		pcb.howLongWaiting = 0;
	}
	
	protected void CreateProcess(int ID,String ProgramPath_Original, String Name, int number) {
		pcb.ProcessState = stateOverseer.newbie;
		pcb.ProcessID = ID;
		
		//ProgramPath = ProgramPath_Original;
		
		pcb.ProcessName = Name;
		
		basePriority = priorityOverseer.RollPriority();
		
		pcb.BaseProcessPriority = basePriority;
		
		pcb.CurrentProcessPriority = basePriority;
		
		pcb.blocked = false;
		
		pcb.A = 0;
		
		pcb.B = 0;
		
		pcb.C = 0;
		
		pcb.D = 0;
		
		pcb.commandCounter = 0;
		
		pcb.receivedMsg = "";
		
		pcb.whenCameToList = number;
		
		pcb.ProcessState = stateOverseer.ready;
		
		pcb.howLongWaiting = 0;
	}
	
	//---odczytaj dane procesu----------------------------------------------------------------------------
	
	public void printInformations() {
		System.out.println("------------------------------");
		System.out.println("ID - " + pcb.ProcessID);
		System.out.println("name - " + pcb.ProcessName);
		System.out.println("order number - " + pcb.whenCameToList);
		System.out.println("state - " + pcb.ProcessState);
		System.out.println("base priority - " + pcb.BaseProcessPriority);
		System.out.println("current priority - " + pcb.CurrentProcessPriority);
		System.out.println("waiting time - " + pcb.howLongWaiting);
		System.out.println("lock state - " + pcb.blocked);
		System.out.println("received msg - " + pcb.receivedMsg);
		//procesor
		System.out.println("Register A - " + pcb.A);
		System.out.println("Register B - " + pcb.B);
		System.out.println("Register C - " + pcb.C);
		System.out.println("Register D - " + pcb.D);
		System.out.println("done command counter - " + pcb.commandCounter);
	}
	
	//-- Getery i Setery----------------------------------------------------------------------------------
	
	//---
	
	public int GetID() {
		
		return pcb.ProcessID;
	}
	
	//---
	
	public String GetName() {
		
		return pcb.ProcessName;
	}
	
	//---
	
	public int GetWhenCameToList() {
			
		return pcb.whenCameToList;
			
	}
		
	public void SetWhenCameToList(int when) {
			
		pcb.whenCameToList = when;
	}
		
	//---
		
	public int GetState() {
			
		return pcb.ProcessState;
	}
		
	public void SetState(int State) {
			
		pcb.ProcessState = State;
	}
	
	//---
	
	public int GetBasePriority() {
		
		return pcb.BaseProcessPriority;
	}
	
	public void SetBasePriority(int priority) {
		
		pcb.BaseProcessPriority = priority;
	}
	
	//---
	
	public int GetCurrentPriority() {
			
		return pcb.CurrentProcessPriority;
	}
		
	public void SetCurrentPriority(int Priority) {
			
		pcb.CurrentProcessPriority = Priority;
	}
	
	//---
		
	public int GetHowLongWaiting() {
			
		return pcb.howLongWaiting;
	}
		
	public void SetHowLongWaiting(int howLong) {
			
		pcb.howLongWaiting = howLong;
	}	
		
	//---
	
	public boolean GetBlocked() {
		
		return pcb.blocked;
	}
	
	public void SetBlocked(boolean blockedState) {
		
		pcb.blocked = blockedState;
	}
	
	//---
	
	public PCB GetPCB() {
		
		return pcb;
	}
	
	public void SetPCB(PCB yourPCB) {
		
		pcb = yourPCB;
	}
}
