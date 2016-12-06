package ProcessorManager;
//import ProcessesManagment.Proces; => wrzucanie procesu do RUNNING i NEXTTRY, zwracanie danych procesu
//import ProcessesManagment.ProcessesManagment => dostep do processesList

// USTAWI� POCZ�TKOWE WARTO�CI P�L RUNNING I NEXTTRY (!)

/**
 * Pole przechowuj�ce aktualnie uruchomiony proces.
 * 
 * @author �UKASZ WOLNIAK
 */
//Proces RUNNING; 

/**
 * Pole przechowuj�ce kandydata na kolejny proces do uruchomienia.
 * Przed pobraniem procesu z NEXTTRY i wrzuceniu go do RUNNING, sprawdzane jest czy w li�cie proces�w nie ma procesu o wy�szym piorytecie. 
 * Je�eli takowy jest, to on zostaje wrzucony do pola RUNNING, a pole NEXTTRY si� nie zmienia.
 * 
 * @author �UKASZ WOLNIAK
 */
//Proces NEXTTRY;


public class processorManager {

/**
 * P�tla sprawdzaj�ca, czy w polu NEXTTRY jest na pewno proces o najwy�szym piorytecie.
 * Je�eli na li�cie jest proces o wi�kszym piorytecie - zmieniane jest pole NEXTTRY.
 * 
 * @author �UKASZ WOLNIAK
 */
for(Proces processFromList : processesList){
	if(processFromList.GetCurrentPriority()>NEXTTRY.GetCurrentPriority()){
	NEXTTRY = processFromList;
	}
}

;;

}
