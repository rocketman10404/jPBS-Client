package acs.jpbs.gui.widgets;

import com.trolltech.qt.gui.QTreeView;
import com.trolltech.qt.gui.QWidget;

public class StatusTree extends QTreeView {
	StatusModel model;
	
	public StatusTree() {
		this(null);
	}
	
	public StatusTree(QWidget parent) {
		this.model = new StatusModel(this);
		this.setModel(this.model);
		this.header().hide();
		this.show();
	}
}
