package acs.jpbs.status;

public interface IPbsObject {
	public String getName();
	
	public IPbsObject child(int index);
	
	public int childCount();
	
	public int getRow();
	
	public void setRow(int row);
}
