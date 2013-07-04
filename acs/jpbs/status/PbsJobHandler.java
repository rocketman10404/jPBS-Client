package acs.jpbs.status;

import acs.jpbs.core.PbsJob;

public class PbsJobHandler implements IPbsObject {
	private PbsJob ref;
	private int row;
	
	public PbsJobHandler(PbsJob j) {
		this.ref = j;
	}
	
	public IPbsObject child(int index) {
		return null;
	}
	
	public int childCount() { return 0; }
	
	public String getName() {
		return Integer.toString(this.ref.getId());
	}
	
	public int getRow() {
		return this.row;
	}
	
	public void setRow(int _row) {
		this.row = _row;
	}
}
