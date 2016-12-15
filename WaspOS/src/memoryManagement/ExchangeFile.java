package memoryManagement;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class ExchangeFile {
	File exchange = new File("exchangefile.txt");

	// stworzenie pliku wymiany
	public ExchangeFile() {
		try {
			if (this.exchange.exists()) {
				this.exchange.delete();
				this.exchange.createNewFile();
			} else
				this.exchange.createNewFile();
		} catch (IOException e) {
			System.out.println("Memory error: creating exchange file failed");
		}
	}

	// ladowanie kodu procesu do pliku wymiany
	public PageTable loadProcessData(String process_name, String fileName) {
		try {
			// odczytanie danych z pliku
			File process = new File(fileName);
			Scanner read = new Scanner(process);
			int pageIndex = 0;

			String processData = new String("");
			// wczytanie danych do stringa
			while (read.hasNextLine()) {
				processData += read.nextLine();
				processData += ';';
			}

			read.close();
			// dopelnienie zeby liczba znakow byla podzielna przez 16
			while (processData.length() % 16 != 0) {
				processData += " ";
			}

			File kopia = new File("exchangefile_copy.txt");
			PrintWriter push = new PrintWriter(kopia);

			// zapis danych do pliku wymiany
			for (int i = 0; i < processData.length() / 16; i++) {
				// System.out.println(processData.substring(pageIndex*16,
				// pageIndex*16+16));
				// zapisanie nr procesu i nr strony programu
				push.println(process_name + " " + pageIndex);

				// zapisanie strony
				push.println(processData.substring(pageIndex * 16, pageIndex * 16 + 16));
				pageIndex++;
			}

			// przepisanie tego co bylo
			Scanner scan = new Scanner(this.exchange);
			while (scan.hasNextLine()) {
				push.println(scan.nextLine());
			}
			push.close();
			scan.close();

			// zmiana pliku tymczasowego w domyslny plik wymiany
			this.exchange.delete();
			kopia.renameTo(this.exchange);
			// zwrócenie tablicy stronic procesu ktory zostal zaladowany do
			// pamieci
			PageTable ret = new PageTable(processData.length(), process_name);
			return ret;

		} catch (FileNotFoundException e) {
			System.out.println("Memory error: loading data to exchange file failed");
		}
		PageTable ret = null;
		return ret;

	}

	// usuwanie danych procesu z pliku wymiany
	public void deleteProcessData(String process_name) {
		try {
			Scanner read = new Scanner(this.exchange);
			File newfile = new File("exchangefile_copy.txt");
			PrintWriter push = new PrintWriter(newfile);

			// wpisywanie do pliku tymczasowego wszystkich danych oprocz danych
			// usuwanego procesu
			while (read.hasNextLine()) {
				String procName = read.next();
				int pageNum = read.nextInt();
				read.nextLine();
				String chc = read.nextLine();
				if (!process_name.equals(procName)) {
					push.println(procName + " " + pageNum);
					push.println(chc);
				}

			}
			read.close();
			push.close();
			// zmiana pliku tymczasowego w domyslny plik wymiany
			this.exchange.delete();
			newfile.renameTo(this.exchange);
		} catch (FileNotFoundException e) {
			System.out.println("Memory error: deleting data from exchange file failed");
		}

	}

	// dostan ramke o danym nr procesu i ramki
	public String getFrame(String processName, int pageNumber) {
		try {
			Scanner search = new Scanner(exchange);
			File newFile = new File("exchangefile_copy.txt");
			PrintWriter writeToNewFile = new PrintWriter(newFile);
			String check = null;
			int pageNr = -1;
			String procName = "", rightFrame = "";
			boolean rightScan = false;
			// szukamy zadana stronice
			while (search.hasNextLine()) {
				check = search.nextLine();
					String divide[] = check.split(" ");
					procName = divide[0];
					pageNr = Integer.parseInt(divide[1]);
					// jezeli to szukana stronica
					if (processName.equals(procName) && pageNumber == pageNr) {
						rightFrame = search.nextLine();
						rightScan = true;

					} else { // jezeli to nie ta stronica, wpisujemy do pliku
							// tymczasowego
					writeToNewFile.println(procName + " " + pageNr);
					writeToNewFile.println(search.nextLine());
				}
			}
			search.close();
			writeToNewFile.close();

			// zamieniamy plik tymczasowy na wlasiwy plik wymiany
			this.exchange.delete();
			newFile.renameTo(this.exchange);

			if (rightScan)
				return rightFrame;
			else
				return null;

		} catch (FileNotFoundException e) {
			System.out.println("Memory error: getting page form exchange file failed");
			return null;
		}
	}

	// dodaje stronice do pliku wymiany
	public void addPageToExchangefile(String processName, int pageNumber, String data) {
		try {
			FileWriter add = new FileWriter(this.exchange, true);
			add.write(processName + " " + pageNumber + "\n" + data + "\n");
			add.close();
		} catch (IOException e) {
			System.out.println("Memory error: write to exchange file failed ");
		}
	}

}