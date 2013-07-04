package acs.jpbs.gui.widgets;

import acs.jpbs.status.PbsServerHandler;

import com.trolltech.qt.gui.QTreeView;
import com.trolltech.qt.gui.QWidget;

public class StatusTree extends QTreeView {
	public StatusTree() {
		this(null);
	}
	
	public StatusTree(QWidget parent) {
		StatusModel model = new StatusModel(this);
		setModel(model);
		show();
	}
	
	public void updateMe() {
		this.expandAll();
		this.update();
	}
}
