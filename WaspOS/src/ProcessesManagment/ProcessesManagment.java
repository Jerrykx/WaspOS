package ProcessesManagment;
import java.lang.Object;
import java.util.LinkedList;
import java.util.List;

public class ProcessesManagment extends Proces {

	// TO JEST KLASA Z METODAMI DLA WAS RESZTY NIE RUSZAC
	
	//===ZMIENNE=========================================================================================
	
	public List<Proces> processesList;
	
	private ID_Overseer overseer;
	
	//===METODY==========================================================================================
	
	//---Konstruktor-------------------------------------------------------------------------------------
	
	public ProcessesManagment(){
		
		processesList = new LinkedList<Proces>();
		overseer = new ID_Overseer();
	}
	
	//---Dodaj nowy proces--------------------------------------------------------------------------------

	public void NewProcess(){
		
		Proces process = new Proces();
		process.CreateProcess(overseer.PickID());
	
		processesList.add(process);
	}

	public void NewProcess(String ProgramPath_Original){
		
		Proces process = new Proces();
		process.CreateProcess(overseer.PickID(),ProgramPath_Original);
	
		processesList.add(process);
	}
	
	public void NewProcess(String ProgramPath_Original, String Name) {
		
		Proces process = new Proces();
		process.CreateProcess(overseer.PickID(),ProgramPath_Original, Name);;
	
		processesList.add(process);
	}
	
	//---usun proces-------------------------------------------------------------------------------------
	
	private void  DeleteProcess() {
		
		//processesList.get(0).pcb.ProcessState
		
		
		
		
		
		
		
		
		
		
		
	}
	
	//---sprawdz stany -> poszukiwanie procesow do usunieica---------------------------------------------
	
	private void CheckStates() {
		
		
		
		
		
		
		
		
		
	}
	
	
	//---odszukiwanie procesu o podanym ID---------------------------------------------------------------
	
	public int FindProcessWithID(int ID) {
		
		Proces proces_kopia;
		
		for(int i = 0; i < processesList.size(); i++) {
			
			proces_kopia = processesList.get(i);
			
			if(proces_kopia.GetID() == ID) {
				
				return i;
			}	
		}
		
		return -1;
	}
	
	//---blocked get set-----------------------------------------------------------------------------------
	
	public boolean GetBlockedWithID(int ID) {
		
		int index = FindProcessWithID(ID);
		Proces proces_kopia = processesList.get(index);
		return proces_kopia.GetBlocked();
	}
	
	public void SetBlocked(int ID, boolean blockedState) {
		
		int index = FindProcessWithID(ID);
		Proces proces_kopia = processesList.get(index);
		proces_kopia.SetBlocked(blockedState);
	}
	
	//---stan procesu get i set----------------------------------------------------------------------------
	
	public int GetStateWithID(int ID) {
		
		int index = FindProcessWithID(ID);
		Proces proces_kopia = processesList.get(index);
		return proces_kopia.GetState();
	}
	
	public void SetState(int ID, int State) {
		
		int index = FindProcessWithID(ID);
		Proces proces_kopia = processesList.get(index);
		proces_kopia.SetState(State);
	}
	
	//---pierwotny priorytet procesu get-------------------------------------------------------------------
	
	public int GetBasePriorityWithID(int ID) {
		
		int index = FindProcessWithID(ID);
		Proces proces_kopia = processesList.get(index);
		return proces_kopia.GetBasePriority();
	}
	
	//---obecny priorytet procesu get set------------------------------------------------------------------
	
	public int GetCurrentPrirityWithID(int ID) {
		
		int index = FindProcessWithID(ID);
		Proces proces_kopia = processesList.get(index);
		return proces_kopia.GetCurrentPriority();
	}
	
	public void SetCurrentPririty(int ID, int Priority) {
		
		int index = FindProcessWithID(ID);
		Proces proces_kopia = processesList.get(index);
		proces_kopia.SetCurrentPriority(Priority);
	}

	//---odczytaj dane procesu----------------------------------------------------------------------------
	
	public void ReadProcessInformations(int ID) {
		
		int index = FindProcessWithID(ID);
		Proces proces_kopia = processesList.get(index);
		proces_kopia.ReadInformations();
	}
}