package ProcessorManager;
//import ProcessesManagment.Proces; => wrzucanie procesu do RUNNING i NEXTTRY, zwracanie danych procesu
//import ProcessesManagment.ProcessesManagment => dostep do processesList

// USTAWI� POCZ�TKOWE WARTO�CI P�L RUNNING I NEXTTRY (!)

/**
 * Pole przechowuj�ce aktualnie uruchomiony proces.
 * 
 * @author �UKASZ WOLNIAK
 */
Proces RUNNING; 

/**
 * Pole przechowuj�ce kandydata na kolejny proces do uruchomienia.
 * Przed pobraniem procesu z NEXTTRY i wrzuceniu go do RUNNING, sprawdzane jest czy w li�cie proces�w nie ma procesu o wy�szym piorytecie. 
 * Je�eli takowy jest, to on zostaje wrzucony do pola RUNNING, a pole NEXTTRY si� nie zmienia.
 * 
 * @author �UKASZ WOLNIAK
 */
Proces NEXTTRY;

/**
 * Pole przechowuj�ce liczbe proces�w wys�anych na RUNNING. 
 * Co trzy procesy (gdy pole jest modulo 3 bez reszty) zwiekszany jest o 1 obecny piorytet (currentPiority) procesu o najmniejszym piorytecie (currentPiority).
 * 
 * @author �UKASZ WOLNIAK
 */
private int sendProcessToRunningCounter = 0;

public class processorManager {

/**
 * P�tla sprawdzaj�ca, czy w polu NEXTTRY jest na pewno proces o najwy�szym piorytecie.
 * Je�eli na li�cie jest proces o wi�kszym piorytecie - zmieniane jest pole NEXTTRY.
 * 
 * @author �UKASZ WOLNIAK
 */
for(Proces processFromList : processesList){
	if(processFromList.GetCurrentPriority() > NEXTTRY.GetCurrentPriority()){
		NEXTTRY = processFromList;
	}
}

/*** ... ***/

/**
 * Instrukcja warunkowa + P�tla rozwi�zuj�ca problem g�odzenia piorytet�w.
 * Je�eli zmienna sendProcessToRunningCounter jest podzielna bez reszty przez 3 (czyl i co 3 wyslane procesy), to obecny piorytet procesu o najmniejszym piorytecie jest zwi�kszany o 1.
 * 
 * @author �UKASZ WOLNIAK
 */
if(sendProcessToRunningCounter%3){
	Proces weakestProcess = processesList.get(1);
	for(Proces processFromList : processesList){
		if(processFromList.GetCurrentPriority() < weakestProcess.GetCurrentPriority()){
			weakestProcess = processFromList;
		}
	}
	weakestProcess.SetCurrentPriority(weakestProcess.GetCurrentPiority()+1);
}
}