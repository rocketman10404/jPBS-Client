package acs.jpbs;


public class Launcher extends Thread {
	public Launcher() { }
	
	public void run() {
		jPBSMain main = jPBSMain.getInstance();
		main.connect();
		main.run();
	}
}
