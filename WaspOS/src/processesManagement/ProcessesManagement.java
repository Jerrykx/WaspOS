package processesManagement;
import java.util.LinkedList;
import java.util.List;

import memoryManagement.RAM;
import processesCommunication.Communication;

public class ProcessesManagement extends Process {

	// TO JEST KLASA Z METODAMI DLA WAS RESZTY NIE RUSZAC
	
	//***ZMIENNE*****************************************************************************************
	
	public List<Process> processesList;
	
	//---pomocnicze--------------------------------------------------------------------------------------
	
	private ID_Overseer idoverseer;
	
	private ProcessStateOverseer stateOverseer;
	
	private List<Integer> finishedProcessList;
	
	private RAM RAM;
	
	private int processNumber;
	
	//***METODY******************************************************************************************
	
	//---Konstruktor-------------------------------------------------------------------------------------
	
	public ProcessesManagement(RAM RAM) {
		this.RAM = RAM;
		processesList = new LinkedList<Process>();
		idoverseer = new ID_Overseer();
		stateOverseer = new ProcessStateOverseer();
		finishedProcessList = new LinkedList<Integer>();
		processNumber = 1;
	}
	
	//===Dodaj/Usun Procesy===============================================================================
	
	//---

	public int NewProcess_XC(String ProgramPath_Original, String Name){
		int i = FindProcessWithName(Name);
		  
		  if(i != -1) {
		   
		   System.out.println("You can't create two programs with the same name while using command XC");
		   return -1;
		  }
		  
		  Process process = new Process();
		  int id = idoverseer.PickID();
		  String s = Integer.toString(id);
		  process.CreateProcess(id,Name, processNumber);
		  processesList.add(process); 
		  processNumber++;
		  
		  RAM.loadDataProcess(s, ProgramPath_Original);
		  CheckStates();
		 return 0;
	}
	
	public void NewProcess_forUser(String ProgramPath_Original, String Name) {
		Process process = new Process();
		int id = idoverseer.PickID();	
		int i = FindProcessWithName(Name);
		
		if(i != -1) {
			Name = Name + id;
		}
		String s = Integer.toString(id);
		process.CreateProcess(id,ProgramPath_Original, Name, processNumber);
		processesList.add(process);
		processNumber++;
		
		RAM.loadDataProcess(s, ProgramPath_Original);
		CheckStates();
	}
	
	public  Process NewProcess_EmptyProcess(String Name) {
		Process process = new Process();
		process.CreateProcess(-1, Name, -1);
		process.SetBasePriority(0);
		process.SetCurrentPriority(0);
		CheckStates();
		return process;
	}
	
	//---
	
	private void  DeleteProcess() {
		for (int i = 0; i < finishedProcessList.size(); i++) {
			int index = FindProcessWithID(finishedProcessList.get(i));
			RAM.deleteProcessData(processesList.get(index).GetName());
			processesList.remove(index);
		}
		finishedProcessList.clear();
	}
	
	public void DeleteProcessWithID(int ID) {
		int index = FindProcessWithID(ID);
		processesList.remove(index);
	}
	
	public void DeleteProcessWithName_XD(String name) {
		int index = FindProcessWithName(name);
		RAM.deleteProcessData(name);
		processesList.remove(index);
	}
	
	//---sprawdz stany -> poszukiwanie procesow do usunieica---------------------------------------------
	
	public void CheckStates() {
		for (int i = 0; i < processesList.size(); i++) {
			if(processesList.get(i).pcb.ProcessState == stateOverseer.finished) {
				finishedProcessList.add(processesList.get(i).pcb.ProcessID);
			}
		}
		
		DeleteProcess();
	}
	
	//===Szukanie procesow===============================================================================
	
	//---
	
	public int FindProcessWithID(int ID) {
		Process proces_kopia;
		
		for(int i = 0; i < processesList.size(); i++) {
			proces_kopia = processesList.get(i);
			if(proces_kopia.GetID() == ID) {
				return i;
			}	
		}
		
		return -1;
	}
	
	//---
	
	public int FindProcessWithName(String name) {
		for(int i = 0; i < processesList.size(); i++) {
			if(processesList.get(i).GetName().equals(name)) {
				return i;
			}	
		}
		return -1;
	}
	
	//===Get/Set===========================================================================================
	
	//---
	
	public int GetIDwithName(String name) {
		
		int index = FindProcessWithName(name);
		return processesList.get(index).GetID();
	}
	
	//---
	
	public String GetNameWithID(int ID) {
		
		int index = FindProcessWithID(ID);
		return processesList.get(index).GetName();
	}
	
	//---
	
	public int GetWhenCameToListWithID(int ID) {
			
		int index = FindProcessWithID(ID);
		return processesList.get(index).GetWhenCameToList();
	}
		
	public void SetWhenCameToListWithID(int ID, int whenCametoList) {
			
		int index = FindProcessWithID(ID);
		processesList.get(index).SetWhenCameToList(whenCametoList);
	}
	
	//---
	
	public int GetStateWithID(int ID) {
			
		int index = FindProcessWithID(ID);
		return processesList.get(index).GetState();
	}
		
	public void SetState(int ID, int State) {
			
		int index = FindProcessWithID(ID);
		processesList.get(index).SetState(State);
	}
	
	//---
	
	public int GetBasePriorityWithID(int ID) {
			
		int index = FindProcessWithID(ID);
		return processesList.get(index).GetBasePriority();
	}
	
	public int SetBasePriorityWithID(int ID, int priority) {
		
		int index = FindProcessWithID(ID);
		return processesList.get(index).GetBasePriority();
	}
	
	//---
	
	public int GetCurrentPrirityWithID(int ID) {
			
		int index = FindProcessWithID(ID);
		return processesList.get(index).GetCurrentPriority();
	}
		
	public void SetCurrentPririty(int ID, int Priority) {
			
		int index = FindProcessWithID(ID);
		processesList.get(index).SetCurrentPriority(Priority);
	}
	
	//---
	
	public int GetHowLongWaitingWithID(int ID) {
			
		int index = FindProcessWithID(ID);
		return processesList.get(index).pcb.howLongWaiting;
	}
		
	public void SetHowLongWaitingWithID(int ID, int howLong) {
			
		int index = FindProcessWithID(ID);
		processesList.get(index).SetHowLongWaiting(howLong);
	}
	
	//---
	
	public boolean GetBlockedWithID(int ID) {
		
		int index = FindProcessWithID(ID);
		return processesList.get(index).GetBlocked();
	}
	
	public void SetBlocked(int ID, boolean blockedState) {
		
		int index = FindProcessWithID(ID);
		processesList.get(index).SetBlocked(blockedState);
	}
	
	//---
	
	public PCB GetPCBWithID(int ID) {
		
		int index = FindProcessWithID(ID);
		return processesList.get(index).pcb;
	}
	
	//===Shell=============================================================================================
	
	public void printProcessListInformations() {
		System.out.println("------------------------------");
		System.out.println("Processes list:");
		
		for(int i = 0; i < processesList.size(); i++) {
			System.out.println( i + ". " + processesList.get(i).GetID() + ", " + processesList.get(i).GetName());
		}
	}

	public void printProcessInformations(int ID) {	
		int index = FindProcessWithID(ID);
		
		if(index >= 0 && index <= processesList.size()-1)
			processesList.get(index).printInformations();
		else
			System.out.println("This process does not exist");
	}
	
	/**
	 * @warning niebezpieczna. uzycie nazwy, ktora nie istnieje powoduje ArrayBoundOfException [-1]
	 * @param name
	 * @return process by ID.
	 */
	public Process getProcess(String name) {
		return processesList.get(FindProcessWithName(name));
	}
}
