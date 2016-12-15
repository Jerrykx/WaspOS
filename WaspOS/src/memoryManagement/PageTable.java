package memoryManagement;

public class PageTable {
	// w jakich ramkach jest dana strona programu
	public int framesNumber[];
	// czy jest w ramie
	public boolean inRAM[];
	// nr procesu ktory jest w ramie
	public String processName;

	// konstruktor tworzacy dla procesu tablice stron
	public PageTable(int processDataSize, String procName) {
		int count;
		// obliczanie ile stron zajmuje dany program
		if (processDataSize % 16 == 0)
			count = processDataSize / 16;
		else
			count = processDataSize / 16 + 1;
		// ustawianie odpowiednich wartosci
		this.framesNumber = new int[count];
		this.inRAM = new boolean[count];
		this.processName = procName;
		// wypelnienie
		for (int i = 0; i < count; i++) {
			this.framesNumber[i] = -1;
			this.inRAM[i] = false;
			// System.out.println(framesNumber[i] +" "+ inRAM[i]+"A");
		}

	}

	// zwraca pozycje jaka w ramie ma dana stronica, zwraca -1 gdy stronicy nie
	// mma w ramie
	public int getPositionInRam(int pageNumber) {
		if (this.inRAM[pageNumber] == false)
			return -1;
		else
			return this.framesNumber[pageNumber];
	}

	// zwraca nr stronicy ktora jest w danej ramce
	public int getIndex(int pageInRam) {
		for (int i = 0; i < this.framesNumber.length; i++) {
			if (this.framesNumber[i] == pageInRam)
				return i;
		}
		return -1;
	}

	// wypisuje tablice stronic
	public void writePageTable() {
		for (int i = 0; i < this.framesNumber.length; i++)
			System.out.println(this.framesNumber[i] + " " + this.inRAM[i]);
	}
}