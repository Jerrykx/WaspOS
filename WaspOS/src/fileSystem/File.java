package fileSystem;

public class File {
	
	File(){
		
	}
	
	//unikalna nazwa identyfikujaca plik na dysku twardym 
	String name;
	
	//numer bloku w kt�rem znajduje si� "blok inteksowy"
	int file_block;
	
	//"rozmiar" pliku na dysku twardym
	int size;
	
}
