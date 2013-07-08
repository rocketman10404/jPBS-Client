package acs.jpbs.status;

import acs.jpbs.core.PbsQueue;
import acs.jpbs.core.PbsServer;

public class PbsServerHandler implements IPbsObject {
	private static PbsServer ref = null;
	private static PbsServerHandler instance = null;
	
	private PbsServerHandler() { }
	
	public void addQueue(PbsQueue q) {
		if(ref != null) {
			ref.addQueue(q);
		}
	}
	
	public IPbsObject child(int index) {
		if(ref == null) return null;
		else return new PbsQueueHandler(ref.getQueue(index));
	}
	
	public int childCount() {
		if(ref == null) return 0;
		else return ref.getNumQueues();
	}
	
	public static PbsServerHandler getInstance() {
		if(instance == null) {
			ref = PbsServer.getInstance();
			instance = new PbsServerHandler();
		}
		return instance;
	}
	
	public String getName() {
		if(ref == null) return "";
		else return ref.getHostName();
	}
	
	public int getNumJobs() {
		if(ref == null) return 0;
		else return ref.getNumJobs();
	}
	
	public int getNumQueues() {
		if(ref == null) return 0;
		else return ref.getNumQueues();
	}
	
	public PbsQueue getQueue(String name) {
		if(ref == null) return null;
		else return ref.getQueue(name);
	}
	
	public int getRow() { return 0; }
	
	public PbsServer getServer() {
		return ref;
	}
		
	public static void loadServerData() {
		ref = PbsServer.getInstance();
	}
	
	public void setRow(int row) { }
}
