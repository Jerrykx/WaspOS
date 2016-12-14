package commandInterpreter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import fileSystem.FileSystem;
import memoryManagement.RAM;
import processesCommunication.Communication;
import processesManagement.PCB;
import processesManagement.Process;
import processesManagement.ProcessesManagement;
import processorManager.ProcessorManager;

public class Interpreter {
	// ProcesMangment.RUNNING.SetPCB(pcbcos);
	/*
	 * 
	 * XR -- Czytanie komunikatu XS -- Wysłanie komunikatu String processName,
	 * String communicate XS <String>,<String> XN -- Znalezienie PCB String
	 * processName XN <String> XY -- Uruchomienie procesu String processName XY
	 * <String> XC -- Utworzenie procesu String processName, String fileName XC
	 * <String><String> XD -- Usunięcie procesu String processName XD <String>
	 * XZ -- Zatrzymanie procesu String processName XZ <String>
	 * 
	 * <> commandInterpreter <> + * AD -- Dodawanie String Register, String
	 * Register || String Register, float number AD <String>,<String> || AD
	 * <String>,<int> + * SB -- Odejmowanie String Register, String Register ||
	 * String Register, float number SB <String>,<String> || SB <String>,<int> +
	 * * MU -- Mnożenie String Register, String Register || String Register,
	 * float number MU <String>,<String> || MU <String>,<int> + * JM -- Jump to
	 * adress String labelName JM <String> + * MV -- Move String Register,
	 * String Register MV <String>,<String> + * $: -- Etykieta JM String
	 * labelName+ ":" ex. MyLabel: + * HLT -- Koniec programu + * MN -- Move
	 * number String Register, INT Number MV <String>,<INT>
	 * 
	 * <> fileManger <> MF -- Create file String Name, String content CR
	 * <String>,<String> WF -- Zapisanie do pliku String Name WR
	 * <String>,<String> DF -- Delete file String Name DF <String> PO -- Print
	 * Output String Register
	 * 
	 */

	// Labels for JM commands is saved in:
	private HashMap<String, Integer> labels;

	// counter to read from memory
	private int commandCounter;

	// other counter not matter - too many explain
	private int otherCounter;

	// Create Box for PCB
	private PCB PCBbox;

	private RAM RAM;

	private FileSystem fileSystem;

	private ProcessesManagement processesManagment;

	private ArrayList<String> rejestry;
	
	private ArrayList<String> rozkazy;
	
	private boolean manySpace = false;
	
	private boolean found = false;
	
	private int stan = 0; // command = 0, param1 = 1; param2 = 2;

	public Interpreter(RAM ram, FileSystem fileSystem, ProcessesManagement processesManagment) {
		labels = new HashMap<String, Integer>();
		PCBbox = new PCB();
		this.RAM = ram;
		this.fileSystem = fileSystem;
		this.processesManagment = processesManagment;

		commandCounter = 0;
		otherCounter = 0;

		rejestry = new ArrayList<String>();
		rozkazy = new ArrayList<String>();
		// [*] USTALONE ROZKAZY [*] //

		// ROZKAZY ARYTMETYCZNE DWUARGUMENTOWE
		rozkazy.add("AD"); // String Register + String Register/int Number
		rozkazy.add("SB"); // String Register - String Register/int Number
		rozkazy.add("MU"); // String Register * String Register/int Number
		rozkazy.add("MV"); // String Register <- String Register/int Number
		rozkazy.add("MN"); // String Register <- INT Number

		// ROZKAZY DWUARGUMENTOWE
		rozkazy.add("XC"); // String processName, String fileName
		rozkazy.add("XS"); // String processName, String communicate
		rozkazy.add("WF"); // String FileName, String Content
		rozkazy.add("WR"); // String FileName, String Register

		// ROZKAZY JEDNOARGUMENTOWE
		rozkazy.add("XD"); // String processName
		rozkazy.add("XN"); // String processName
		rozkazy.add("XY"); // String processName
		rozkazy.add("XZ"); // String processName
		rozkazy.add("JN"); // String Address
		rozkazy.add("JM"); // String label
		rozkazy.add("MF"); // String Name
		rozkazy.add("DF"); // String name
		rozkazy.add("PO");

		// ROZKAZY BEZARGUMENTOWE

		rozkazy.add("XR"); // Czytanie komunikatu
		rozkazy.add("HLT"); // END PROGRAM

		// [*] USTALONE REJESTRY [*] //
		rejestry.add("A");
		rejestry.add("B");
		rejestry.add("C");
		rejestry.add("D");

		manySpace = false;
		found = false;
		stan = 0; // command = 0, param1 = 1; param2 = 2
	}

	public int RUN(Process RUNNING) {
		// Put to Box a PCB from current Process
		PCBbox = RUNNING.GetPCB();
		// Set porgram variable
		String program = "";

		// Copy PCB to rejetrs of Procesor
		setValue("A", PCBbox.A);
		setValue("B", PCBbox.B);
		setValue("C", PCBbox.C);
		setValue("D", PCBbox.D);

		labels = PCBbox.labels; // Load map of labels

		// First is uden to load chars of program
		commandCounter = PCBbox.commandCounter;

		// Second used to interpret commands
		otherCounter = PCBbox.commandCounter;

		program = getProgram(RUNNING.GetName());
		work(program, labels);
		
		
		setValue("PCBbox.A", getValue("A"));
		setValue("PCBbox.B", getValue("B"));
		setValue("PCBbox.C", getValue("C"));
		setValue("PCBbox.D", getValue("D"));
		RUNNING.pcb.A=core.Processor.A; //TODO Bardzo but.
		RUNNING.pcb.B=core.Processor.B; //i tu tez
		RUNNING.pcb.C=core.Processor.C; //i tu
		RUNNING.pcb.D=core.Processor.D; //i tu
		setValue("PCBbox.commandCounter", commandCounter);
		PCBbox.labels = labels;

		RUNNING.SetPCB(PCBbox);
		return 0;
	}

	private int getValue(String param1) {
		switch (param1) {
		case "A":
			return core.Processor.A;
		case "B":
			return core.Processor.B;
		case "C":
			return core.Processor.C;
		case "D":
			return core.Processor.D;
		// case "RUNNING.PCB.A": return RUNNING.PCB.A;
		// case "RUNNING.PCB.B": return RUNNING.PCB.B;
		// case "RUNNING.PCB.C": return RUNNING.PCB.C;
		// case "RUNNING.PCB.D": return RUNNING.PCB.D;
		// case "RUNNING.PCB.commandCounter": return RUNNING.PCB.commandCounter;
		}
		return 0;
	}

	private int setValue(String param1, int value) {
		switch (param1) {
		case "A":
			core.Processor.A = value;
			break;
		case "B":
			core.Processor.B = value;
			break;
		case "C":
			core.Processor.C = value;
			break;
		case "D":
			core.Processor.D = value;
			break;
		case "PCBbox.A":
			PCBbox.A = value;
			break;
		case "PCBbox.B":
			PCBbox.B = value;
			break;
		case "PCBbox.C":
			PCBbox.C = value;
			break;
		case "PCBbox.D":
			PCBbox.D = value;
			break;
		case "PCBbox.commandCounter":
			PCBbox.commandCounter = commandCounter; //TODO !!! metoda buta w drzwiach, poprawi�.
			break;
		}
		return 0;
	}

	// Checking: Is command is the label?
	private boolean isLabel(StringBuilder command) {
		if (command.length() > 0 && command.charAt(command.length() - 1) == ':') {
			return true;
		} else {
			return false;
		}
	}

	private String getProgram(String procesName) {
		char znak;
		String program = "";

		while (true) {
			// Load char from RAM 
			znak = RAM.getCommand(commandCounter, procesName);
			program += znak;
			commandCounter++;
			
			if (znak == '\n') {
				break;
			}
		}
		return program;
	}

	// Follow the recognized command
	public void doCommand(String rozkaz, String param1, String param2, boolean argDrugiJestRejestrem,
			HashMap<String, Integer> labels) {
		switch (rozkaz) {
		case "AD": // Dodawanie wartości
			if (argDrugiJestRejestrem) {
				setValue(param1, getValue(param1) + getValue(param2));
			} else {
				setValue(param1, getValue(param1) + Integer.parseInt(param2));
			}
			break;

		case "MU": // Mnożenie wartości
			if (argDrugiJestRejestrem) {
				setValue(param1, getValue(param1) * getValue(param2));
			} else {
				setValue(param1, getValue(param1) * Integer.parseInt(param2));
			}
			break;

		case "SB": // Odejmowanie wartości
			if (argDrugiJestRejestrem) {
				setValue(param1, getValue(param1) - getValue(param2));
			} else {
				setValue(param1, getValue(param1) - Integer.parseInt(param2));
			}
			break;

		case "MV": // Umieszczenie wartości
			if (argDrugiJestRejestrem) {
				setValue(param1, getValue(param2));
			} else {
				setValue(param1, Integer.parseInt(param2));
			}
			break;

		case "MN": // Umieszczenie wartości MN
			if (argDrugiJestRejestrem) {
				setValue(param1, getValue(param2));
			} else {
				setValue(param1, Integer.parseInt(param2));
			}
			break;

		case "JM": // Skok do etykiety JM
			if (labels.containsKey(param1) && getValue("C") != 0) {
				
				//setValue("C", labels.get(param1));
				setValue("C", getValue("C")-1);
				commandCounter = labels.get(param1);
			}
			break;

		case "HLT": // Koniec programu
			ProcessorManager.RUNNING.SetState(4);
			break;

		case "XR": // czytanie komunikatu;
			ProcessorManager.RUNNING.pcb.receivedMsg = Communication.read(ProcessorManager.RUNNING.GetName());
			break;
		case "XS": // -- Wysłanie komunikatu;
			Communication.write(param1, param2);
			break;
		case "XN": // -- znalezienie PCB (param1);
			//setValue("A", processesManagment.FindProcessWithName(param1));
			core.Processor.A=processesManagment.GetIDwithName(param1);
			break;

		case "XC": {// -- tworzenie procesu (param1);
			File file = new File(param2);
			if(!file.exists())  {
				try {
					FileWriter fw = new FileWriter(param2, true);
					BufferedWriter out = new BufferedWriter(fw);
					out.write("HLT");
					out.close();
					fw.close();
				} catch(FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			processesManagment.NewProcess_forUser(param2, param1);
			processesManagment.getProcess(param1).SetState(3);
			break; }
		case "XY": // -- Uruchomienie procesu
			processesManagment.getProcess(param1).SetState(1);
			processesManagment.getProcess(param1).SetCurrentPriority(ProcessorManager.MAX_PRIORITY+1);
			break;
		case "XD": // -- usuwanie procesu (param1);
			processesManagment.getProcess(param1).SetState(4);
			processesManagment.DeleteProcessWithName_XD(param1);
			break;
		case "XZ": // -- Zatrzymanie procesu
			processesManagment.getProcess(param1).SetState(3);
			// stopProces(param1);
			break;

		case "MF": // -- Create file
			fileSystem.createFileWithContent(param1, param2);
			break;
		case "WF":
			fileSystem.appendToFile(param1, param2);
			break;
		case "WR":
			fileSystem.appendToFile(param1, Integer.toString(getValue(param2)));
			break;
		case "DF": // -- Delete file
			// deleteFile(paramI) //TODO
			File file = new File(param1 + ".txt");
			file.delete();
			fileSystem.deleteFile(param1);
			break;
		case "RF": // -- Print Output
			//TODO
			break;
		}
	}

	public void work(String program, HashMap<String, Integer> labels) {
		StringBuilder command = new StringBuilder();
		StringBuilder param1 = new StringBuilder();
		StringBuilder param2 = new StringBuilder();
		
		for (Character c : program.toCharArray()) {
			if (c == '\n') {
				otherCounter++;
				stan = 0;
				found = false;
				
				System.out.println(command.toString() + " " + param1.toString() + " " + 
				param1.toString() + " executor: " +ProcessorManager.RUNNING.GetName());
				
				boolean rozkazToEtykieta = isLabel(command);
				boolean poprawnoscRejestru = rejestry.contains(param1.toString());
				boolean poprawnoscRozkazu = rozkazy.contains(command.toString());
				boolean argDrugiJestRejestrem = rejestry.contains(param2.toString());

				if (rozkazToEtykieta) {
					String temp = "";
					for (Character z : (command.toString()).toCharArray()) {
						if (z != ':') {
							temp += z;
						} else {
							labels.put(temp, (otherCounter));
						}
					}
					
				} else if (poprawnoscRozkazu) {
					doCommand(command.toString(), param1.toString(), param2.toString(), argDrugiJestRejestrem, labels);
				}

				command.delete(0, command.length());
				param1.delete(0, param1.length());
				param2.delete(0, param2.length());
				continue;
			}

			if (c == ',') {
				stan++;
				otherCounter++;
				found = true;
				manySpace = true;
				continue;
			}

			if (c == ' ') {
				otherCounter++;
				if (stan == 1 && !found) {
					continue;
				}
				if (manySpace) {
					continue;
				} else {
					stan++;
					manySpace = true;
				}
			}

			if (c != ' ') {
				otherCounter++;
				manySpace = false;
				switch (stan) {
				case 0:
					command.append(c);
					break;
				case 1:
					param1.append(c);
					break;
				case 2:
					param2.append(c);
					break;
				}
			}
		}
	}
}
