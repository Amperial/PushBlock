package net.pl3x.pushblock.exception;

public class WorldNotFoundException extends Exception {
	private static final long serialVersionUID = 363553562015457787L;
	
	public WorldNotFoundException() {
		super("World not found!");
	}
}
