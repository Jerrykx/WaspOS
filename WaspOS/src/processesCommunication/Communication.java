package processesCommunication;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import processesManagement.ProcessesManagement;
import syncMethod.Lock;

public class Communication {	
	/**
	 * nie wiem co z lockami :/
	 */
	private static String logs;
	
	static Lock lock;
	
	private static ProcessesManagement processesManagement;
	
	public Communication(ProcessesManagement processesManagement) {
		Communication.processesManagement = processesManagement;
		logs = "";
		lock = new Lock("cos");
	}
	
	public static void write(String processName, String msg) {
		logs += " |Try write " + msg + " to " + processName;
		try {
			FileWriter file = new FileWriter("komunikacja" + ".box", true);
			BufferedWriter out = new BufferedWriter(file);
			out.write(msg + "\n");
			out.close();
			file.close();
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		lock.lock(processesManagement.getProcess(processName));
	}
	
	public static String read(String processName) {
		lock.unlock(processesManagement.getProcess(processName));
		logs += " |Try read from " + processName;
		try {
			File file = new File("komunikacja" + ".box");
			if(file.exists()) {
				Scanner sc = new Scanner(file);
				ArrayList<String> arrayList = new ArrayList<String>();
				
				while(sc.hasNextLine())
					arrayList.add(sc.nextLine()); 
				sc.close();
				
				PrintWriter pw = new PrintWriter("komunikacja" + ".box");
			    for(int i = 1; i < arrayList.size(); i++)
			    	pw.println(arrayList.get(i));
			    pw.close();
			    lock.lock(processesManagement.getProcess(processName));
			    if(arrayList.size() > 0) {
			    	logs += " |read " + arrayList.get(0);
			    	return arrayList.get(0);
			    } else {
			    	logs += " |Process " + processName + " want read from empty box.";
			    	return "empty box";
			    }
			} else System.out.println(logs += "Read process message box error. This box does not exist. ");
		} catch(FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
		return "";
	}
	
	public static void deleteProcessMsgBox(String processName) {
		File file = new File(processName + ".box");
	    
		if(file.delete()) {
			logs += (" |" + file.getName() + " deleted! ");
		} else {
			logs += (" |" + file.getName() + " delete failed! ");
		}
	}
	
	public static void printLogs() {
		System.out.println(logs);
	}
}
