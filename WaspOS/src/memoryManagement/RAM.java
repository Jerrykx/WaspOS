package memoryManagement;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class RAM {
	private char ram[] = new char[256];
	private boolean freeFrame[] = new boolean[16];
	private String processNameInFrame[] = new String[16];
	private Queue<Integer> FIFO = new LinkedList<Integer>();
	public List<PageTable> pageTables = new ArrayList<PageTable>();
	private ExchangeFile exchangeFile = new ExchangeFile();

	// konstruktor, ustawienie podstawowych parametrow
	public RAM() {
		for (int i = 0; i < 16; i++) {
			this.freeFrame[i] = true;
			this.processNameInFrame[i] = "";
			for (int j = 0; j < 16; j++)
				this.ram[i * j] = ' ';
		}
	}

	// metoda dla interpretera, zwraca znak o ktory prosi interpreter
	public char getCommand(int programCounter, String processName) {
		// szukany znak
		char search;
		// index tablicy stron szukanego procesu w kolekcji pagetables
		int pageTableIndex = -1;
		// szukanie tablicy stron z odpowiednim procesem
		for (int i = 0; i < this.pageTables.size(); i++) {
			if (this.pageTables.get(i).processName.equals(processName))
				pageTableIndex = i;
		}

		// wyznaczenie nr strony ktora musi byc w ramie przed zwroceniem
		// odpowiedniego znaku
		int pageNumber = programCounter / 16;

		// sprawdzenie w ktorej ramce sa dane, zwraca -1 gdy nie ma w ramie
		// odpowiedniej strony
		int framePosition = pageTables.get(pageTableIndex).getPositionInRam(pageNumber);

		// zaladowanie strony do ramu jesli jej tam nie ma
		if (framePosition == -1) {
			String dane = this.exchangeFile.getFrame(processName, pageNumber);

			// szukanie wolnej ramki
			// jezeli wszystkie ramki sa zajete
			if (FIFO.size() == 16) {
				// algorytm fifo, pobieram index ramki która jest juz najdluzej
				// zajeta w ramie
				framePosition = FIFO.poll();

				// zaznaczenie w tablicy stron procesu którego pamiêæ zwalniamy
				for (int i = 0; i < this.pageTables.size(); i++) {
					// szukanie procesu ktory jest w ramce ktora chemy zwolnic
					if (this.pageTables.get(i).processName.equals(this.processNameInFrame[framePosition])) {
						// wyznaczenie odpowiednich wartosci i zwrocenie
						// stronicy do pliku wymiany
						int help = this.pageTables.get(i).getIndex(framePosition);
						this.pageTables.get(i).inRAM[help] = false;
						String data = "";
						for (int j = 0; j < 16; j++)
							data += ram[framePosition * 16 + j];
						this.exchangeFile.addPageToExchangefile(this.pageTables.get(i).processName, help, data);
					}
				}
				// wrzucenie do ramu wymaganych danych po zwolnieniu zajetej
				// ramki
				for (int j = 0; j < 16; j++)
					this.ram[framePosition * 16 + j] = dane.charAt(j);

				FIFO.add(framePosition);

			} else { // jezeli sa wolne ramki
				for (int i = 0; i < 16; i++) {
					// szukanie wolnej ramki
					if (this.freeFrame[i]) {
						// zapisanie do wolnej ramki odpowiednich danych
						framePosition = i;

						for (int j = 0; j < 16; j++)
							ram[framePosition * 16 + j] = dane.charAt(j);

						this.freeFrame[framePosition] = false;
						FIFO.add(framePosition);
						i = 16; // TODO
					}
				}

			}
			// ustawienie nazwy procesu ktory od teraz bedzie w tej ramce
			this.processNameInFrame[framePosition] = processName;
			// ustawienie odpowiednich danych w tablicy stron procesu ktoremu
			// przydzielamy pamiec
			pageTables.get(pageTableIndex).inRAM[pageNumber] = true;
			pageTables.get(pageTableIndex).framesNumber[pageNumber] = framePosition;
		}

		// zwrocenie odpowiedniego znaku
		int positionInFrame = programCounter % 16;
		search = ram[framePosition * 16 + positionInFrame];
		if (search == ';')
			return '\n';
		else
			return search;
	}

	// zaladowanie nowego procesu do pamieci
	public void loadDataProcess(String processName, String fileName) {
		PageTable help = exchangeFile.loadProcessData(processName, fileName);
		this.pageTables.add(help);
	}

	// usuwanie danego procesu z pamieci
	public void deleteProcessData(String processName) {

		// usuniecie danych z pamieci ram
		for (int i = 0; i < 16; i++) {
			if (this.processNameInFrame[i].equals(processName)) {
				this.freeFrame[i] = true;
				this.FIFO.remove(i);
				for (int j = 0; j < pageTables.size(); j++) {
					if (this.pageTables.get(j).processName.equals(processName))
						this.pageTables.remove(j);
				}

				for (int j = 0; j < 16; j++)
					ram[i * 16 + j] = ' ';
			}
		}

		// usuniecie danych z pliku wymiany
		this.exchangeFile.deleteProcessData(processName);
	}

	// wypisanie procesow bedacych w pamieci
	public void writeProcessesNamesInRam() {
		for (int i = 0; i < this.pageTables.size(); i++)
			System.out.println(pageTables.get(i).processName);
	}

	// wypisanie zawartosci ramu
	public void writeRAM() {
		for (int i = 0; i < 16; i++) {
			System.out.print(i + " ");
			for (int j = 0; j < 16; j++)
				System.out.print(ram[16 * i + j]);
			System.out.println();
		}
	}

	// wypisanie aktualnego stanu kolejki fifo
	public void writeQueue() {
		System.out.println(this.FIFO);
	}

}