package processorManager;
import ProcessesManagment.Proces; 
import ProcessesManagment.ProcessesManagment;

// Pole howLongWaiting + getter/setter - GRACJAN + getter whenCameToList (co proces dodany on ma sobie to inkrementowa� co 1)!
// + wywolanie INTERPRETERA!
// + POSTARZANIE PROCESOW - jesli howLongWaitnig = 3, dynamiczny proces + 1, howLongWaiting = 0!
// + Gdy schodzi z RUNNING proces, dynamicznyPiorytet = bazowy!
// + MOJE GETTERY : Co jest akurat w RUNNING i NEXTTRY + MOZE TEKSTY GDY WYOWLUJE FUNKCJE POSZCZEGOLNE(?)!
// + zabecpieczenie przed wartoscia NULL/0 w RUNNING i NEXTTRY!
// ? WSZYSTKO W RUN ?
// ? JAREK ZWROCI MI ZERO TO ZNOW DZIALAM ?
// !!! PROTEZY OD JARKA I GRACJANA !!!
// PROGRAMY Z PLIKU!!!
// SHELL!!

/**
 * Proces bezczynno�ci, bym m�g� por�wnywa� procesy z listy z polem RUNNING i NEXTTRY.
 * 
 * @author �UKASZ WOLNIAK
 */
Proces idleProcess = new Proces();
idleProcess.CreateProcess(0,idleProcess, 0);
idleProcess.SetBasePririty(0);
idleProcess.SetCurrentPririty(0);
idleProcess.SetWhenCameToList(0);

/**
 * Pole przechowuj�ce aktualnie uruchomiony proces.
 * 
 * @author �UKASZ WOLNIAK
 */
public static Proces RUNNING = idleProcess; 

/**
 * Pole przechowuj�ce kandydata na kolejny proces do uruchomienia.
 * 
 * @author �UKASZ WOLNIAK
 */
public static Proces NEXTTRY = idleProcess;

/**
 * Funkcja sprawdzaj�ca, czy w polu RUNNING jest na pewno proces o najwy�szym piorytecie z dostepnych tych na liscie procesow + FCFS.
 * Je�eli na li�cie jest proces o wi�kszym piorytecie - zmieniane jest pole NEXTTRY.
 * 
 * @author �UKASZ WOLNIAK
 */
private void checkRUNNING(){
	for(Proces processFromList : processesList){
		if((processFromList.GetCurrentPriority() >= RUNNING.GetCurrentPriority()) && processFromList.GetState()==1){
			if(processFromList.GetCurrentPriority() == RUNNING.GetCurrentPriority()){
				if(processFormList.WhenCameToList() < RUNNING.processFormList.WhenCameToList()){
					RUNNING = SetCurrentPriority(GetBasePriority());
					RUNNING = processFromList;
					RUNNING = RUNNING.SetState(2);
				}
			}
			else{
				RUNNING = SetCurrentPriority(RUNNING.GetBasePriority());
				RUNNING = processFromList;
				RUNNING = RUNNING.SetState(2);
			}
		}
	}
}

/**
 * Funkcja sprawdzaj�ca, czy w polu NEXTTRY jest na pewno kolejny proces o najwy�szym piorytecie + FCFS.
 * Je�eli na li�cie jest proces o wi�kszym piorytecie - sprawdzamy czy czasem te piorytet nie jest aby w polu RUNNING, zmieniane jest pole NEXTTRY.
 * 
 * @author �UKASZ WOLNIAK
 */
private void checkNEXTTRY(){
	for(Proces processFromList : processesList){
		if((processFromList.GetCurrentPriority() >= NEXTTRY.GetCurrentPriority()) && (processFromList.GetState()==1  || processFromList.GetState()==3)){
			if(RUNNING!=processFromList){
				if(processFromList.GetCurrentPriority() == NEXTTRY.GetCurrentPriority()){
					if(processFormList.WhenCameToList() < NEXTTRY.processFormList.WhenCameToList()){
						NEXTTRY = SetCurrentPriority(NEXTTRY.GetBasePriority());
						NEXTTRY = processFromList;
					}
				}
				else{
					NEXTTRY = SetCurrentPriority(NEXTTRY.GetBasePriority());
					NEXTTRY = processFromList;
				}
			}
		}
	}
}

/**
 * Funkcja rozwi�zuj�ca problem g�odzenia piorytet�w.
 * Co rozkaz zwi�kszany jest piorytet procesu o najmniejszym piorytecie z listy.
 * Wywolane zostaje rownie� sprawdzenie pola najpierw RUNNING (czy czasem zwiekszony proces nie ma wiekszego piorytetu niz ten w RUNNING) oraz NEXTTRY (jezeli nie ma wiekszy od tego w RUNNING, to moze ma wiekszy chociaz od tego w NEXTTRY).
 * ZALOZENIE : 2 procesy nie moga w tej samej chwili wejsc na liste (wiec wyiberamy tlyko jeden glodujacy a nie grupe glodujacych).
 *
 * @author �UKASZ WOLNIAK
 */
private void checkStarving(){

}
/**
 * Funkcja zwi�ksza pole howLongWaiting - je�eli proces nie jest aktywny, zwi�kszamy jego pole howLongWaiting o 1.
 * 
 * @author �UKASZ WOLNIAK
 */
private void changeWaiting(){
	
}

/**
 * Funkcja "postarzaj�ca" procesy.
 * Je�eli jaki� proces czeka 3 rozkazy (howLongWaiting) i wszedl najpozniej na liste to jego obecny piorytet jest zwi�kszany o 1 i howLongWaiting jest zerowane.
 *  Na koncu sprawdzamy pola NEXTTRY i RUNNING;
 *  
 * @author �UKASZ WOLNIAK
 */
private void checkAging(){
	private Proces weakestProcess;
	for(Proces processFromList : processesList){
		if(processFromList.GetHowLongWaiting==3){
			if(processFromList.GetWhenCameToList > weakestProcess.GetWhenCameToList()){
				weakestProcess = processFromList;
			}
		}
	}
	for(Proces processFromListCheck : processesList){
		if(processFromListCheck.GetID() == weakestProcess.GetID()){
			processFromListCheck.SetCurrentPriority(processFormListCheck()+1);
		}
	}
	compareNEXTTRYandRUNNING();
}

/**
 *  Funkcja por�wnuj�ca pola NEXTTRY i RUNNING.
 *  Je�eli proces w NEXTTRY : nie jest zablokowany, ma wi�kszy piorytet od procesu w RUNNING lub ma rowny piorytet, ale wcze�niej wszed� na list�, to zamieniane jest pole RUNNING, a pole NEXTTRY odpowiednio uzupe�niane. 
 * 	je�eli nie, do podmiany pola RUNNING wyszukiwany jest proces z listy RUNNING.
 *  @author �UKASZ WOLNIAK
 */
private void comapreNEXTTRYandRUNNING(){
	//SPRAWDZENIE CZY NA PEWNO W NEXTTRY JEST PROCES O NAJWYZCZYM PIORYTECIE
	checkNEXTTRY();
	if(NEXTTRY.GetBlocked()==false){
		if(NEXTTRY.GetCurrentPriority()>= RUNNING.GetCurrentPriority()){
			if(NEXTTRY.GetCurrentPriority() == RUNNING.GetCurrentPriority()){
				if(NEXTTRY.GetWhenCameToList() < RUNNING.GetWhenCameToList()){
					RUNNING.SetCurrentPriority(RUNNING.GetBasePriority());
					RUNNING = NEXTTRY;
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
 * Instancja klasy wywo�ywana zawsze gdy proces zostanie zako�czony lub zablokowany.
 * 
 * @author �UKASZ WOLNIAK
 */
public class ProcessorManager {
		//Jezeli proces zosta� zablokowany lub zakonczony, pod RUNNING podstawiamy proces bezczynno�ci.
		if(RUNNING.GetState()==3 || RUNNING.GetState==4){
			RUNNING.SetCurrentPriority(RUNNING.GetBasePriority()); 
			RUNNING = idleProcess;
		}
		//JEZELI PROCES NIE JEST ZABLOKOWANY, PIORYTET MA WIEKSZY, MOMENT WEJSCIA NA LISTE MNIEJSZY - WRZUCAMY GO DO RUNNING, NEXTTRY ODPOWIEDNIO ZMIENIAMY - funkcja compareRUNNINGandNEXTTRY
		compareNEXTTRYandRUNNING();
		// SPRAWDZAMY CZY JAKIS GLODUJE, 
		checkStarving();
	}