package acs.jpbs.status;

import acs.jpbs.core.PbsQueue;
import acs.jpbs.core.PbsServer;

public class PbsQueueHandler implements IPbsObject {
	private PbsQueue ref;
	private int row;
	
	public PbsQueueHandler(PbsQueue q) {
		this.ref = q;
	}
	
	public PbsQueueHandler(String qName) {
		this.ref = PbsServer.getInstance().getQueue(qName);
	}
	
	public IPbsObject child(int index) {
		return new PbsJobHandler(this.ref.getJobByIndex(index));
	}
	
	public int childCount() {
		return this.ref.getNumJobs();
	}
	
	public String getName() {
		return this.ref.getName();
	}
	
	public int getRow() {
		return this.row;
	}
	
	public void setRow(int _row) {
		this.row = _row;
	}
}
