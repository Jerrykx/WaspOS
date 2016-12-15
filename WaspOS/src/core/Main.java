package core;

public class Main {
	public static void main(String[] args) {
		try {
			new Shell();
		} catch(Exception io) {
			System.out.println(io.getMessage());
			System.out.println("im overloaded!");
		}
	}
}
