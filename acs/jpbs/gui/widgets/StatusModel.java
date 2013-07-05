package acs.jpbs.gui.widgets;

import acs.jpbs.status.IPbsObject;
import acs.jpbs.status.PbsServerHandler;

import com.trolltech.qt.QtBlockedSlot;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.Qt.ItemDataRole;
import com.trolltech.qt.gui.QTreeModel;

public class StatusModel extends QTreeModel {

	public StatusModel(QObject parent) {
		super(parent);
	}

	@Override
	@QtBlockedSlot
	public Object child(Object arg0, int arg1) {
		if(arg0 == null && PbsServerHandler.getInstance() != null) {
			return PbsServerHandler.getInstance();
		} else if(arg0 instanceof IPbsObject) {
			return ((IPbsObject)arg0).child(arg1);
		} else return null;
	}

	@Override
	@QtBlockedSlot
	public int childCount(Object arg0) {
		if(arg0 == null && PbsServerHandler.getInstance() != null) {
			return 1;
		} else if(arg0 instanceof IPbsObject) {
			return ((IPbsObject)arg0).childCount();
		} else return 0;
	}

	@Override
	@QtBlockedSlot
	public String text(Object arg0) {
		if(arg0 == null && PbsServerHandler.getInstance() != null) {
			return PbsServerHandler.getInstance().getName();
		} else if (arg0 instanceof IPbsObject) {
			return ((IPbsObject)arg0).getName();
		} else return null;
	}
	
	@Override
	public Object data(Object value, int role) {
		Object result = null;
		if(value instanceof IPbsObject) {
			switch(role) {
			case ItemDataRole.DisplayRole:
				result = ((IPbsObject)value).getName();
				break;
			default:
				result = null;
			}
		}
		return result;
	}
	
	public void reload() {
		this.reset();
		((StatusTree)this.parent()).expand(null);
	}
}
