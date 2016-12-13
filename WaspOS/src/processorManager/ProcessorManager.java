package processorManager;

import commandInterpreter.Interpreter;
import processesManagement.Process;
import processesManagement.ProcessesManagement;

public class ProcessorManager {
	// + PETLA WHILE WSPOLPRACY Z JARKIEM TELEGRAMEM WIECZORKIEM!
	// PROGRAMY Z PLIKU !!!
	// SHELL !!!
	// STATIKI CZY INSTANCJE KLASY !!!
	
	/**
	 * Proces bezczynno�ci, bym m�g� por�wnywa� procesy z listy z polem RUNNING i NEXTTRY oraz do wykorzystania gdy z pola NEXTTRY proces przechodzi do RUNNING (na chwile do momentu sprawdzenia pola NEXTTRY).
	 * 
	 * @author �UKASZ WOLNIAK
	 */
	
	private Process idleProcess;
	
	/**
	 * Pole przechowuj�ce aktualnie uruchomiony proces.
	 * 
	 * @author �UKASZ WOLNIAK
	 */
	public static Process RUNNING;
	
	/**
	 * Pole przechowuj�ce kandydata na kolejny proces do uruchomienia.
	 * 
	 * @author �UKASZ WOLNIAK
	 */
	public static Process NEXTTRY;
	
	private ProcessesManagement processesManagment;
	
	private Interpreter interpreter;
	
	public ProcessorManager(ProcessesManagement processesManagment, Interpreter interpreter) {
		this.processesManagment = processesManagment;
		this.interpreter = interpreter;
		idleProcess = processesManagment.NewProcess_EmptyProcess("idleProcess");
		RUNNING = idleProcess; 
		NEXTTRY = idleProcess;
	}
	
	
	/**
	 * Funkcja sprawdzaj�ca, czy w polu RUNNING jest na pewno proces o najwy�szym piorytecie z dostepnych tych na liscie procesow + zastosowanie algorytmu FCFS.
	 * 
	 * @author �UKASZ WOLNIAK
	 */
	private void checkRUNNING(){
		for(Process processFromList : processesManagment.processesList){
			if((processFromList.GetCurrentPriority() >= RUNNING.GetCurrentPriority()) && processFromList.GetState()==1){
				if(processFromList.GetCurrentPriority() == RUNNING.GetCurrentPriority()){
					if(processFromList.GetWhenCameToList() < RUNNING.GetWhenCameToList()){
						RUNNING.SetCurrentPriority(RUNNING.GetBasePriority());
						RUNNING = processFromList;
						RUNNING.SetState(2);
					}
				}
				else{
					RUNNING.SetCurrentPriority(RUNNING.GetBasePriority());
					RUNNING = processFromList;
					RUNNING.SetState(2);
				}
			}
		}
	}
	
	/**
	 * Funkcja sprawdzaj�ca, czy w polu NEXTTRY jest na pewno kolejny proces o najwy�szym piorytecie + zastosowanie algorytmu FCFS.
	 * Je�eli na li�cie jest proces o wi�kszym piorytecie; jezeli taki proces istnieje, (lub ma taki sam piorytet ale wczesniej wszedl do listy) i nie jest ACTIVE to wstawiamy go do NEXTTRY.
	 * 
	 * @author �UKASZ WOLNIAK
	 */
	private void checkNEXTTRY(){
		for(Process processFromList : processesManagment.processesList){
			if((processFromList.GetCurrentPriority() >= NEXTTRY.GetCurrentPriority()) && (processFromList.GetState()==1  || processFromList.GetState()==3)){
					if(processFromList.GetCurrentPriority() == NEXTTRY.GetCurrentPriority()){
						if(processFromList.GetWhenCameToList() < NEXTTRY.GetWhenCameToList()){
							NEXTTRY.SetCurrentPriority(NEXTTRY.GetBasePriority());
							NEXTTRY = processFromList;
						}
				}
					else{
						NEXTTRY.SetCurrentPriority(NEXTTRY.GetBasePriority());
						NEXTTRY = processFromList;
					}
			}
		}
	}
	
	/**
	 *  Funkcja por�wnuj�ca pola NEXTTRY i RUNNING.
	 *  Je�eli proces w NEXTTRY : nie jest zablokowany, ma wi�kszy piorytet od procesu w RUNNING lub ma rowny piorytet, ale wcze�niej wszed� na list�, to zamieniane jest pole RUNNING, a pole NEXTTRY odpowiednio uzupe�niane. 
	 * 	je�eli nie, do podmiany pola RUNNING wyszukiwany jest proces z listy RUNNING.
	 * 
	 *  @author �UKASZ WOLNIAK
	 */
	private void compareNEXTTRYandRUNNING(){
		checkNEXTTRY();
		if(NEXTTRY.GetBlocked()==false){
			if(NEXTTRY.GetCurrentPriority()>= RUNNING.GetCurrentPriority()){
				if(NEXTTRY.GetCurrentPriority() == RUNNING.GetCurrentPriority()){
					if(NEXTTRY.GetWhenCameToList() < RUNNING.GetWhenCameToList()){
						RUNNING.SetCurrentPriority(RUNNING.GetBasePriority());
						RUNNING = NEXTTRY;
						RUNNING.SetState(2);
						NEXTTRY = idleProcess;
						checkNEXTTRY();
					}
					else{
						checkRUNNING();
					}
				}
				else{
					RUNNING.SetCurrentPriority(RUNNING.GetBasePriority());
					RUNNING = NEXTTRY;
					RUNNING.SetState(2);
					NEXTTRY = idleProcess;
					checkNEXTTRY();
				}
			}
			else{
				checkRUNNING();
			}
		}
		else{
			checkRUNNING();
		}
	}
	
	/**
	 * Funkcja rozwi�zuj�ca problem g�odzenia piorytet�w.
	 * Co rozkaz zwi�kszany jest piorytet procesu o najmniejszym piorytecie z listy a jezeli sa dwa o takim samy, wybierany jest ten co wczesniej wszedl na liste.
	 * Wywolane zostaje rownie� funkcja compareNEXTTRYandWAITING.
	 *
	 * @author �UKASZ WOLNIAK
	 */
	private void checkStarving(){
		Process weakProcess = processesManagment.processesList.get(0);
		for(Process processFromList : processesManagment.processesList){
				if(processFromList.GetCurrentPriority() <= weakProcess.GetCurrentPriority()){
					if(processFromList.GetCurrentPriority() == weakProcess.GetCurrentPriority()){
					if(processFromList.GetWhenCameToList() < weakProcess.GetWhenCameToList()){
						weakProcess = processFromList;
					}	
				}
					else{
						weakProcess = processFromList;
					}
	
			}
		}
		for(Process processFromListCheck : processesManagment.processesList){
			if(processFromListCheck.GetID() == weakProcess.GetID()){
				processFromListCheck.SetCurrentPriority(processFromListCheck.GetCurrentPriority()+1);
			}
		}
		compareNEXTTRYandRUNNING();
	}
	/**
	 * Funkcja zwi�ksza pole howLongWaiting co rozkaz - je�eli proces nie jest aktywny, zwi�kszamy jego pole howLongWaiting o 1.
	 * ZALOZENIE: Nie zwiekszamy pola howLongWaiting procesu aktywnemu, zakonczonemu, ale tez (!) zablokowanemu.
	 * 
	 * @author �UKASZ WOLNIAK
	 */
	private void changeWaiting(){
		for(Process processFromList : processesManagment.processesList){
			if(processFromList.GetState()!=2 && processFromList.GetState()!=4 && processFromList.GetState()!=4){
				processFromList.SetHowLongWaiting(processFromList.GetHowLongWaiting()+1);
			}
		}
	}
	
	/**
	 * Funkcja zwiekszajaca piorytet procesu czekajacego juz co najmniej 3 rozkazy - postarzanie procesow.
	 * Je�eli jaki� proces czeka co najmniej 3 rozkazy (howLongWaiting) (lub jezeli czekajacych jakas liczbe jest kilka, to ten ktory wszedl na liste wczesniej) to jego obecny piorytet jest zwi�kszany o 1 i howLongWaiting jest zerowane.
	 * Na koncu sprawdzamy pola NEXTTRY i RUNNING;
	 * ZALOZENIE : 2 procesy nie moga w tej samej chwili wejsc na liste (wiec wybieramy tylko jeden glodujacy a nie grupe glodujacych).
	 *  
	 * @author �UKASZ WOLNIAK
	 */
	private void checkAging(){
		Process weakestProcess = processesManagment.processesList.get(0);
		for(Process processFromList : processesManagment.processesList){
			if(processFromList.GetHowLongWaiting() >= 3){
				if(processFromList.GetHowLongWaiting() >= weakestProcess.GetHowLongWaiting()){
					if(processFromList.GetHowLongWaiting() == weakestProcess.GetHowLongWaiting()){
					if(processFromList.GetWhenCameToList() > weakestProcess.GetWhenCameToList()){
						weakestProcess = processFromList;
					}	
				}
					else{
						weakestProcess = processFromList;
					}
	
			}
		}
		}
		for(Process processFromListCheck : processesManagment.processesList){
			if(processFromListCheck.GetID() == weakestProcess.GetID()){
				processFromListCheck.SetCurrentPriority(processFromListCheck.GetCurrentPriority()+1);
				processFromListCheck.SetHowLongWaiting(0);
			}
		}
		compareNEXTTRYandRUNNING();
	}
	
	/**
	 * Funkcja przedstawiajaca aktualna zawartosc pola RUNNING.
	 * 
	 * @author �UKASZ WOLNIAK
	 */
	public void showRUNNING() {
		System.out.println("\n\nNow in RUNNING field exist process: \n\n");
		RUNNING.printInformations();
	}
	
	/**
	 * Funkcja przestawiajaca aktualna zawartosc pola NEXTTRY.
	 * 
	 * @author �UKASZ WOLNIAK
	 */
	public void showNEXTTRY(){
		System.out.println("\n\nNow in NEXTTRY field exist process: \n\n");
		NEXTTRY.printInformations();
	}
	
	/**
	 * ZALOZENIE : funkcja Scheuler jest wywo�ywana po kazdym rozkazie.
	 * 
	 * @author �UKASZ WOLNIAK
	 */
	public void Scheduler() {
			//Jezeli proces zosta� zablokowany lub zakonczony, pod RUNNING podstawiamy proces bezczynno�ci.
			if(RUNNING.GetState() == 3 || RUNNING.GetState() ==4){
				RUNNING.SetCurrentPriority(RUNNING.GetBasePriority()); 
				RUNNING = idleProcess;
			}
			//JEZELI PROCES NIE JEST ZABLOKOWANY, PIORYTET MA WIEKSZY, MOMENT WEJSCIA NA LISTE MNIEJSZY - WRZUCAMY GO DO RUNNING, NEXTTRY ODPOWIEDNIO ZMIENIAMY - funkcja compareRUNNINGandNEXTTRY
			compareNEXTTRYandRUNNING();
			// ROZWIAZUJEMY GLODOWANIE
			checkStarving();
			// ROZWIAZUJEMY CZEKANIE W NIESKONCZONOSC
			checkAging();
			// MINAL ROZKAZ, WIEC ZWIEKSZAMY CZEKANKO - wywolujemy tu a nie na poczatku bo przy pierwszym wyborze wysdzloby ze czekaly rozkaz procesy
			changeWaiting();
			//Uruchomienie INTERPRETERA
			interpreter.RUN(RUNNING);
	}
}