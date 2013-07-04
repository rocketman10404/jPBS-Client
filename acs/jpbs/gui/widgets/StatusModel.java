package acs.jpbs.gui.widgets;

import acs.jpbs.status.IPbsObject;

import com.trolltech.qt.QtBlockedSlot;
import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.gui.QTreeModel;

public class StatusModel extends QTreeModel {

	public StatusModel(QObject parent) {
		super(parent);
	}

	@Override
	@QtBlockedSlot
	public Object child(Object arg0, int arg1) {
		if(arg0 instanceof IPbsObject) {
			return ((IPbsObject)arg0).child(arg1);
		} else return null;
	}

	@Override
	@QtBlockedSlot
	public int childCount(Object arg0) {
		if(arg0 instanceof IPbsObject) {
			return ((IPbsObject)arg0).childCount();
		} else return 0;
	}

	@Override
	@QtBlockedSlot
	public String text(Object arg0) {
		if(arg0 instanceof IPbsObject) {
			return ((IPbsObject)arg0).getName();
		} else return null;
	}
	
	public void setupModelData(IPbsObject data, IPbsObject parent, Integer counter) {
		this.setData(counter, 0, data);
		data.setRow(counter);
		if(parent != null) {
			QModelIndex idx = this.valueToIndex(parent);
			int childcount = parent.childCount();
			int first = counter - parent.getRow();
			this.insertRow(first, idx);
			//this.childrenInserted(idx, first, first);
		}
		for(int i=0; i<data.childCount(); i++) {
			int offset = (i == 0 ? counter + i + 1 : counter + i + 1 + data.child(i-1).childCount());
			this.setupModelData(data.child(i), data, offset);
		}
	}
}
