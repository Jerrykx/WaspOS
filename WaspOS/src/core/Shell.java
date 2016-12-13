package core;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.regex.Pattern;

import commandInterpreter.Interpreter;
import fileSystem.FileSystem;
import memoryManagement.*;
import processorManager.ProcessorManager;
import processesManagement.ProcessesManagement;

public class Shell {
	private RAM RAM;
	private FileSystem fileSystem;
	private ProcessesManagement processesManagment;
	private ProcessorManager processorManager;
	private Interpreter interpreter;
	private BufferedReader in;
	
	private String string;
	
	private HashMap<String, String> allowedCommands;
	
	public Shell() throws FileNotFoundException, IOException {
		allowedCommands = new HashMap<String, String>();
		allowedCommands.put("exit", "exit from system");
		allowedCommands.put("help", "exactly what you are reading");
		allowedCommands.put("cred", "\\m/");
		allowedCommands.put("logo", "draw wasp");
		allowedCommands.put("load", "load program");
		allowedCommands.put("pram", "print content of ram");
		allowedCommands.put("preg", "print all registers");
		allowedCommands.put("plis", "print processes");
		allowedCommands.put("prun", "print running field");
		allowedCommands.put("pnex", "print nexttry field");
		allowedCommands.put("step", "do one step on processor");
		
		new Processor();
		
		RAM = new RAM();
		fileSystem = new FileSystem();
		processesManagment = new ProcessesManagement(RAM);
		interpreter = new Interpreter(RAM, fileSystem, processesManagment);
		processorManager = new ProcessorManager(processesManagment, interpreter);
		
		in = new BufferedReader(new InputStreamReader(System.in));
		
		System.out.println("Welcome in WaspOS 2016");
		help();
		
		do {
			System.out.println("Type command:");
			string = in.readLine().trim();
			string = string.toLowerCase();
			
			if(!allowedCommands.containsKey(string)) {
				System.out.println("Undefined command!");
			}
			
			switch(string) {
			case "help": help(); break;
			case "logo": drawLogo(); break;
			case "load": load(); break;
			case "pram": dram(); break;
			case "preg": dreg(); break;
			case "plis": plis(); break;
			case "prun": prun(); break;
			case "pnex": pnex(); break;
			case "step": step(); break;
			}		
		} while(!string.equals("exit"));
		
		in.close();
		System.out.println("Panda3!");
	}
	
	private void help() {
		System.out.println("All allowed commands:");
		Object[] tab = allowedCommands.keySet().toArray();
		for(int i = 0; i < allowedCommands.size(); i++) {
			System.out.println("->" + tab[i] + " - " + allowedCommands.get(tab[i]));
		}
	}
	
	private void drawLogo() throws FileNotFoundException, IOException {
		BufferedReader br = new BufferedReader(new FileReader("wasp.logo"));
		String line = null;
		while ((line = br.readLine()) != null) {
		  System.out.println(line);
		}
		br.close();
	}
	
	private void load() throws IOException {
		System.out.println("Enter path of program.");
		switch(in.readLine()) {
		case "program1.txt":
			processesManagment.NewProcess_forUser("program1.txt", "program1");
			break;
		case "program2.txt":
			processesManagment.NewProcess_forUser("program2.txt", "program2");
			break;
		case "program3.txt":
			processesManagment.NewProcess_forUser("program3.txt", "program3");
			break;
		default: System.out.println("This file does not exist."); return;
		}
	}
	
	private void dram() {
		
	}
	
	private void dreg() {
		Processor.printAllRegisters();
	}
	
	private void plis() throws IOException {
		System.out.println("Type ID or name of process to print. \"all\" to print all existing processes");
		String s = in.readLine().trim();
		
		if(s.contains("all")) {
			processesManagment.printProcessListInformations();
			return;
		}
		
		if(!Pattern.compile("[0-9]+").matcher(s).matches()) {
			return;
		}
		processesManagment.printProcessInformations(Integer.parseInt(s));
	}
	
	private void prun() {
		
	}
	
	private void pnex() {
		
	}
	
	private void step() {
		processorManager.Scheduler();
	}
}
