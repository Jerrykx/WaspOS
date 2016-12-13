package processesCommunication;

/**
 * G��wna klasa wymiany danych pomi�dzy procesami.
 * 
 * @author Gosak.
 */
public class Pipe {
	/**
	 * Podstawi� tu w�a�ciwy uchwyt do pliku na dysku. znakami \0
	 * b�d� oddzielane kolejne wiadomo�ci, je�eli plik mo�e zawiera� 
	 * wiele wiadomo�ci.
	 */
	String falseFile;
	
	/**
	 * TODO Podstawi� tu w�a�ciwy semafor.
	 * TODO Ogarn�� jego wykorzystanie.
	 */
	int falseSemaphore = 0;

	public Pipe(Process a, Process b) {
		// TODO utworzenie pliku do komunikacji na dysku
		falseFile = "";
		// TODO ustawienie nazwy pliku w PCB proces�w,
		a.pcb.fileName = "nazwaPliku";
		b.pcb.fileName = "nazwaPliku";
	}
	
	/**
	 * Tymczasowo plik jako string.
	 * 
	 * @return ostatnia wiadomosc w stringu.
	 */
	public String getOldestMessage() {
		//TODO blokada pliku, rozpoznawanie czy proces mo�e czyta� tak aby nie 
		//     odczyta� w�asnej wiadomo�ci,
		StringBuilder returned = new StringBuilder();
		char[] tab = falseFile.toCharArray();
		
		int i = 0;
		while(tab[i] != '\0') {
			returned.append(tab[i]);
			i++;
		}
		
		char[] newFileTab = falseFile.toCharArray();
		StringBuilder newFile = new StringBuilder();
		i = 0;
		while(i < falseFile.length() - returned.length()) {
			newFile.append(newFileTab[i]);
			i++;
		}
		falseFile = newFile.toString();
		//TODO odblokowanie pliku
		return returned.toString();
	}
	
		
	public void sendMessage(String msg) {
		//TODO je�eli dost�p do pliku jest mo�liwy to dodaj kolejn� wiadomo��
		// 	   w przeciwnym razie u�pij proces korzystaj�cy z pliku i nadpisz
		//     oraz poinformuj o mo�liwo�ci odczytania wiadomo�ci <PCB>
		falseFile += msg + '\0';
	}
}
