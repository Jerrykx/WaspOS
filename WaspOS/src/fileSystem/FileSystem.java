package fileSystem;

import java.util.ArrayList;
import java.util.List;

import	fileSystem.File;

public class FileSystem {
	
	FileSystem(){
		
	}
	
	//dysk (tablica bajt�ww(zank�w))
	char[] hardDrive = new char[1024];
	
	//wektor bitowy
	boolean[] bitVector = new boolean[32];
	
	//lista wpis�w katalogowych gdzie kazdy wpis jest obiektem klasy File
	List<File> catalogFile = new ArrayList<File>();		 
	
	public boolean createFile(String name, int size){	return true;}
	
	public boolean deleteFile(String name){	return true;}
	
	public boolean readFile(){	return true;}
	
	public boolean writteFile(){	return true;}
	
}