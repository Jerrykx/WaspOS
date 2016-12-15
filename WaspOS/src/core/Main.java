package core;

import java.io.IOException;

public class Main {
	public static void main(String[] args) {
		try {
			new Shell();
		} catch(IOException io) {
			System.out.println(io.getMessage());
			System.out.println("im overloaded!");
		}
	}
}
