package acs.jpbs.status;

import acs.jpbs.core.PbsServer;

public class PbsServerHandler implements IPbsObject {
	private static PbsServer ref = null;
	private static PbsServerHandler instance = null;
	
	private PbsServerHandler() { }
	
	public IPbsObject child(int index) {
		return new PbsQueueHandler(ref.getQueue(index));
	}
	
	public int childCount() {
		return ref.getNumQueues();
	}
	
	public static PbsServerHandler getInstance() {
		if(instance == null) {
			ref = PbsServer.getInstance();
			instance = new PbsServerHandler();
		}
		return instance;
	}
	
	public String getName() {
		return ref.getHostName();
	}
	
	public int getRow() { return 0; }
	
	public void setRow(int row) { }
}
