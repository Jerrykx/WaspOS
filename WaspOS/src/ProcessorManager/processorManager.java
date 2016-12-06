package ProcessorManager;
import ProcessesManagment.Proces; 
import ProcessesManagment.ProcessesManagment

// USTAWI� POCZ�TKOWE WARTO�CI P�L RUNNING I NEXTTRY (!)

/**
 * Pole przechowuj�ce aktualnie uruchomiony proces.
 * 
 * @author �UKASZ WOLNIAK
 */
public Proces RUNNING; 

/**
 * Pole przechowuj�ce kandydata na kolejny proces do uruchomienia.
 * Przed pobraniem procesu z NEXTTRY i wrzuceniu go do RUNNING, sprawdzane jest czy w li�cie proces�w nie ma procesu o wy�szym piorytecie. 
 * Je�eli takowy jest, to on zostaje wrzucony do pola RUNNING, a pole NEXTTRY si� nie zmienia.
 * 
 * @author �UKASZ WOLNIAK
 */
public Proces NEXTTRY;

/**
 * Pole przechowuj�ce liczbe proces�w wys�anych na RUNNING. 
 * Co trzy procesy (gdy pole jest modulo 3 bez reszty) zwiekszany jest o 1 obecny piorytet (currentPiority) procesu o najmniejszym piorytecie (currentPiority).
 * 
 * @author �UKASZ WOLNIAK
 */
private int sendProcessToRunningCounter = 0;

/**
 * Funkcja sprawdzaj�ca, czy w polu RUNNING jest na pewno proces o najwy�szym piorytecie z dostepnych tych na liscie procesow.
 * Je�eli na li�cie jest proces o wi�kszym piorytecie - zmieniane jest pole NEXTTRY.
 * 
 * @author �UKASZ WOLNIAK
 */
private void checkRUNNING(){
	for(Proces processFromList : processesList){
		if(processFromList.GetCurrentPriority() > RUNNING.GetCurrentPriority()){
			RUNNING = processFromList;
		}
	}
	return true;
}

/**
 * Funkcja sprawdzaj�ca, czy w polu NEXTTRY jest na pewno kolejny proces o najwy�szym piorytecie.
 * Je�eli na li�cie jest proces o wi�kszym piorytecie - zmieniane jest pole NEXTTRY.
 * 
 * @author �UKASZ WOLNIAK
 */
private void checkNEXTTRY(){
	for(Proces processFromList : processesList){
		if(processFromList.GetCurrentPriority() > NEXTTRY.GetCurrentPriority()){
			if(RUNNING!=processFromList){
				NEXTTRY = processFromList;
			}
		}
	}
	return true;
}

private void checkStarving(){
	if(sendProcessToRunningCounter%3){
		private Proces weakestProcess = processesList.get(1);
		for(Proces processFromList : processesList){
			if(processFromList.GetCurrentPriority() < weakestProcess.GetCurrentPriority()){
				weakestProcess = processFromList;
			}
		}
		for(Proces processFromList : processesList){
			if(processFromList.GetID() == weakestProcess.GetID()){
				processFromList.SetCurrentPriority(processFromList.GetCurrentPiority()+1);
			}
		}
		checkRUNNING();
		checkNEXTTRY();
	}
}

public class processorManager {
	checkRUNNING();
	checkNEXTTRY();
	
	if(NEXTTRY.GetBlocked()){
		
	}

/**
 * Instrukcja warunkowa + P�tla rozwi�zuj�ca problem g�odzenia piorytet�w.
 * Je�eli zmienna sendProcessToRunningCounter jest podzielna bez reszty przez 3 (czyl i co 3 wyslane procesy), to obecny piorytet procesu o najmniejszym piorytecie jest zwi�kszany o 1.
 * Wywolane zostaje rownie� sprawdzenie pola najpierw RUNNING (czy czasem zwiekszony proces nie ma wiekszego piorytetu niz ten w RUNNING) oraz NEXTTRY (jezeli nie ma wiekszy od tego w RUNNING, to moze ma wiekszy chociaz od tego w NEXTTRY).
 * @author �UKASZ WOLNIAK
 */

	checkStarving();
}
