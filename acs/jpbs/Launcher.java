package acs.jpbs;


public class Launcher extends Thread {
	public Launcher() { }
	
	public void run() {
		new jPBSMain();
		jPBSMain.run();
	}
}
