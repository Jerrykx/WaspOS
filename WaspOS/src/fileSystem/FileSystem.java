package fileSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import	fileSystem.File;

public class FileSystem {

	//podstawowe sta�e na kt�rych opieram dzia�eni dysku czyli jego wielko��, rozmiar bloku i liczba tych �e blok�w
	private final int DISK_SIZE = 1024;
	private final int BLOCK_SIZE = 32;
	private final int NR_OF_BLOCKS = DISK_SIZE/BLOCK_SIZE;
	
	//dysk (tablica bajt�w(zank�w))
	private char[] hardDrive = new char[DISK_SIZE];
		
	//wektor bitowy
	private boolean[] bitVector = new boolean[NR_OF_BLOCKS];
		
	//lista wpis�w katalogowych gdzie kazdy wpis jest obiektem klasy File
	private List<File> mainCatalog = new ArrayList<File>();
	
	//konstruktor nie robi nic ciekawego poprostu 
	//wype�nia tablice zankiem # by �atwiej by�o j� czyta�
	//dla pewnosci wypelnia bitVector zerami
	public FileSystem(){
		for(int i=0; i < DISK_SIZE; i++) hardDrive[i] = '#';
		for(int i=0; i < NR_OF_BLOCKS; i++) bitVector[i] = false;
	}
	
	//funkcja tworz�ca plik wraz z dopisaniem do niego zawarto�ci
	public void createFileWithContent(String name, String content){
		createEmptyFile(name);
		appendToFile(name, content);
	}
	
	//funkcja tworz�ca pusty plik
	public void createEmptyFile(String name){

		if(!isExistFileWithSameName(name)){
			int indexFreeBlock = findFreeBlock();
			if(indexFreeBlock != -1){
				//System.out.println(indexFreeBlock);
				mainCatalog.add(new File(name, indexFreeBlock, 0));
				bitVector[indexFreeBlock] = true;
			}
			else System.out.println("ERROR");
		}
		else System.out.println("ERROR");
	}
	
	//funkcja dopisuje do pliku dane
	public void appendToFile(String name, String content){
		int indexChar = 0;
		String charTable = "";
		if(isExistFileWithSameName(name)){
			//System.out.println("wszystko gra");
			if(isEnoughMemory(name, content.length())){
				if(isFileHavePlace(name)){
					for(; indexChar<getNrFreePlaceInLastBlock(name) && indexChar<content.length() ; indexChar++){
						charTable+= content.charAt(indexChar);
					}
					//System.out.println(charTable+" "+charTable.length());
					//doda� znaki do ostatniego bloku
					fillDataBusyBlock(name,charTable);
					//zmieni� rozmiar pliku
					increaseSizeFile(name, charTable.length());
				}
				
				while(indexChar < content.length()){
					charTable="";
					for(int i = 0; i<BLOCK_SIZE && indexChar<content.length(); i++, indexChar++){
						charTable+= content.charAt(indexChar);
					}
					//System.out.println(charTable+" "+charTable.length());
					int index = findFreeBlock();
					//doda� nowy wpis do bloku indeksowego pliku
					addEntryToBlockIndex(name, index);
					//doda� znaki do nowego bloku
					fillDataNewBlock(index, charTable);
					bitVector[index] = true;
					//zminie� rozmiar pliku
					increaseSizeFile(name, charTable.length());
				}
			}
			else System.out.println("ERROR");
		}
		else System.out.println("ERROR");
		
	}
	
	//funkcja odczytuj�ca dane z pliku
	public String getFileContent(String name){
		String data = "";
		
		if(isExistFileWithSameName(name)){
			int size = getSizeFile(name);
			if(size > 0){
				int blocks = size/BLOCK_SIZE;
				if(size%BLOCK_SIZE != 0)blocks++;
				String indexBlock = getBlockIndexFile(name);
				for(int i=0; i<size/BLOCK_SIZE; i++){
					
					int index = (int)indexBlock.charAt(i)*BLOCK_SIZE;
					for(int j=0; j<BLOCK_SIZE; j++){
						
						data+= hardDrive[index];
						index++;		
					}
				}
				if(size%BLOCK_SIZE > 0){
					int index = (int)indexBlock.charAt(blocks-1)*BLOCK_SIZE;
					for(int i=0; i<size%BLOCK_SIZE; i++){
						data+= hardDrive[index];
						index++;
					}
				}
			}
			else System.out.println("ERROR");
		}
		else System.out.println("ERROR");
		return data;
	}
	
	//funkcja usuwaj�ca plik
	public void deleteFile(String name){
		if(isExistFileWithSameName(name)){
			//zwolnienie blokow pamieci
			int size = getSizeFile(name);
			if(size > 0){
				int blocks = size/BLOCK_SIZE;
				if(size%BLOCK_SIZE != 0)blocks++;
				String indexBlock = getBlockIndexFile(name);
				for(int i=0; i<size/BLOCK_SIZE; i++){
					int index = (int)indexBlock.charAt(i);
					bitVector[index] = false;
				}
				if(size%BLOCK_SIZE > 0){
					int index = (int)indexBlock.charAt(blocks-1);
					bitVector[index] = false;
				}
				//usuniecie wpisu katalogowego
			}
			bitVector[getIndexBlockIndexFile(name)] = false;
			removeDirectoryEntry(name);
		}
		else System.out.println("ERROR");
	}
	
	//______________________PONI�EJ_FUNKCJE_POMOCNICZE_____________________________________
	
	//sprawdza czy istnieje plik o takiej nazwie
	private boolean isExistFileWithSameName(String name){
		boolean exist = false;
		
		for(int i=0; i<mainCatalog.size(); i++){
			if(mainCatalog.get(i).name.equals(name)){
				exist = true;
			}
		}
		
		return exist;
	}
	
	//zwraca index pierwszego wolnego bloku w pamieci
	private int  findFreeBlock(){
		int freeBlock = -1;
		
		for(int i=0; i<NR_OF_BLOCKS; i++){
			if(bitVector[i] == false){
				freeBlock = i;
				break;
			}
		}
		
		return freeBlock;
	}
	
	//sprawdzenie czy wystarczy pami�ci by dopisa� dane do pliku
	public boolean isEnoughMemory(String name, int neededMemory){
		boolean isEnough = false;
		int freeMemory = BLOCK_SIZE * numberOfFreeBlocks();
		if(isFileHavePlace(name)){
			int size = getSizeFile(name);
			int howMany = size % 32;
			freeMemory+= howMany;
		}
		if(freeMemory >= neededMemory) isEnough = true;
		return isEnough;
	}
	
	//zwraca liczbe wolnych blokow danych
	public int numberOfFreeBlocks(){
		int nr = 0;
		
		for(int i=0; i<NR_OF_BLOCKS; i++){
			if(bitVector[i] == false) nr++;
		}
		
		return nr;
	}
	
	//sprawdzenie czy plik ma nie wype�niony w ca�o�ci bloku
	public boolean isFileHavePlace(String name){
		boolean have = false;
		if(getSizeFile(name) % BLOCK_SIZE != 0) have = true;
		return have;
	}
	
	//zwraca rozmiar pliku
	private int getSizeFile(String name){
		int size = 0;
		for(int i=0; i<mainCatalog.size(); i++){
			if(mainCatalog.get(i).name.equals(name)){
				size = mainCatalog.get(i).size;
			}
		}
		return size;
	}
	
	//zwraca index bloku indexowego
	private int getIndexBlockIndexFile(String name){
		int index = 0;
		for(int i=0; i<mainCatalog.size(); i++){
			if(mainCatalog.get(i).name.equals(name)){
			index = mainCatalog.get(i).block_index;
			}
		}
		return index;
	}
	
	//zwraca blok indeksowy
	public String getBlockIndexFile(String name){
		int index = getIndexBlockIndexFile(name)*BLOCK_SIZE;
		String charTab = "";
		for(int i=0; i<BLOCK_SIZE; i++){
			charTab+= hardDrive[index];
			index++;
		}
		return charTab;
	}
	
	//zwraca index ostatniego bloku indexowego
	//ufam tobie
	public int getIndexLastBlockFile(String name){
		int index = -1;
		int size = getSizeFile(name);
		int indexBlockIndexFile = getIndexBlockIndexFile(name);
		int howManyBlocks = size / BLOCK_SIZE - 1;
		if(size % BLOCK_SIZE != 0) howManyBlocks++;
		index = (int)hardDrive[indexBlockIndexFile*BLOCK_SIZE + howManyBlocks];
		return index;
	}
	
	//zwraca liczbe wolnych miejsc w ostatnim bloku pliku
	public int getNrFreePlaceInLastBlock(String name){
		int freePlace = 0;
		freePlace = BLOCK_SIZE - (getSizeFile(name) % BLOCK_SIZE);
		return freePlace;
	}
	
	//wype�nia dany blok znakami
	private void fillDataNewBlock(int indexBlock, String charTable){
		int index = indexBlock * BLOCK_SIZE;
		for(int i = 0; i<charTable.length(); i++, index++){
			hardDrive[index] = charTable.charAt(i);
		}
	}
	
	//zwieksza rozmiar pliku
	private void increaseSizeFile(String name, int n){
		for(int i=0; i<mainCatalog.size(); i++){
			if(mainCatalog.get(i).name.equals(name)){
				mainCatalog.get(i).size+= n;
			}
		}
	}
	
	//dodaje wpis do bloku indeksowego pliku
	private void addEntryToBlockIndex(String name, int index){
		int i = getIndexBlockIndexFile(name) * BLOCK_SIZE + getSizeFile(name) / BLOCK_SIZE;
		hardDrive[i] = (char) index;
		//System.out.println(i+" "+index);
	}
	
	//wype�nia ostatni blok znakami
	private void fillDataBusyBlock(String name, String charTable){
		int index = getIndexLastBlockFile(name) * BLOCK_SIZE + getSizeFile(name) % BLOCK_SIZE;
		for(int i=0; i<charTable.length(); i++){
			hardDrive[index] = charTable.charAt(i);
			index++;
		}
	}
	
	//usuwa wpis katalogowy pliku o danej nazwie
	private void removeDirectoryEntry(String name){
		for(int i=0; i<mainCatalog.size(); i++){
			if(mainCatalog.get(i).name.equals(name)){
				mainCatalog.remove(i);
			}
		}
	}
	
	//______________________PONI�EJ_FUNKCJE_DO_"DIAGNOSTYKI"_MODU�U________________________
	
	//fukcja s�u�y do wy�wietlania zawarto�ci tablicy dysku twardego
	public void showHardDrive(){
		System.out.println("hardDrive");
		int x = 1;
		for(int i = 0; i < DISK_SIZE; i++){
			if(i % 32 == 0){
				System.out.print(x+" ");
				if(x<10) System.out.print(" ");
				x++;
			}
			System.out.print(hardDrive[i]);
			if((i+1) % 8 == 0) System.out.print("\t");
			if((i+1) % 32 == 0) System.out.print("\n");
		}
		System.out.print("\n");
	}
	
	//fukcja s�u�y do wy�wietlania zawarto�ci vektora bit�w kt�ry przechowuje informacje o tym czy blok jest zaj�ty
	public void showBitVector(){
		System.out.println("bitVector");
		int y;
		for(int i = 0; i < NR_OF_BLOCKS; i++){
			if(bitVector[i] == false) y = 0;
			else y = 1;
			System.out.print(y+" ");
			if((i+1) % 4 == 0) System.out.print("\n");
		}
		System.out.print("\n");
	}

	//fukcja s�u�y do wy�wietlania zawarto�ci tabilcy dysku twardego oraz vektora bit�w
	public void showDiskAndVector(){
		System.out.println("hardDrive");
		int x = 0;
		int y;
		for(int i = 0; i < DISK_SIZE; i++){
			if(i % 32 == 0){
				System.out.print(x+"\t");
				x++;
			}
			
			if(hardDrive[i]<(char)31){
				System.out.print('$');
			}
			else{
				System.out.print(hardDrive[i]);
			}
			
			if(((i+1) % 8 == 0)&&(!((i+1) % 32 == 0))) System.out.print("  ");
			
			if((i+1)%BLOCK_SIZE == 0){
				if(bitVector[i/BLOCK_SIZE] == false) y = 0;
				else y = 1;
				System.out.print("     "+y);
			}
			
			if((i+1) % 32 == 0) System.out.print("\n");
		}
		System.out.print("\n");
	}
	
	//fukcja s�u�y do wy�wietlania zawarto�ci g��wnego katalogu czyli trzy podstawowe informacje o plikach nawa, blok indeksowy, zormiar
	public void showMainCatalog(){
		System.out.println("mainCatalog");
		int x;
		if(mainCatalog.isEmpty()){
			System.out.println("Main Catalog is Empty");
		}
		else{
			System.out.println("NR\tNAME\tB.I\tSIZE");
			for(int i = 0; i < mainCatalog.size(); i++){
				System.out.print((i+1)+"\t");
				System.out.print(mainCatalog.get(i).name+"\t");
				System.out.print(mainCatalog.get(i).block_index+"\t");
				System.out.print(mainCatalog.get(i).size);
				System.out.print("\n");
			}
		}
		System.out.print("\n");
	}
	
	
	public void x(){
		createFileWithContent("a","aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\naaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
	}
	
	public void y(){
		createFileWithContent("basn","Mrok wieczorny, babcia siwa\nprzy kominku g�ow� kiwa.");
	}
	
}