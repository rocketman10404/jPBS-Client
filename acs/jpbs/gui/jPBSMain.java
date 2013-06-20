package acs.jpbs.gui;

import com.trolltech.qt.core.QRect;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QDesktopWidget;
import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QWidget;

public class jPBSMain extends QWidget {
	private final int WIDTH = 500;
	private final int HEIGHT = 350;
	
	public jPBSMain() {
		QDesktopWidget qdw = new QDesktopWidget();
		QRect qr = qdw.screenGeometry(qdw.primaryScreen());
		
		int screenWidth = qr.width();
		int screenHeight = qr.height();
		
		int x = (screenWidth - WIDTH) / 2;
		int y = (screenHeight - HEIGHT) / 2;
		
		this.initGui();
		this.resize(WIDTH, HEIGHT);
		this.move(x, y);
		this.setWindowTitle("jPBS Client");
		this.show();		
	}
	
	private void initGui() {
		// Initialize layout managers
		QGridLayout gridMain = new QGridLayout(this);
		
		// Initialize widgets
		QPushButton okButton = new QPushButton("Ok", this);
		QPushButton quitButton = new QPushButton("Quit", this);
		QLabel consoleWidget = new QLabel("", this);
		
		// Configure widgets
		consoleWidget.setStyleSheet("QLabel { background-color: #ffffff; border: 1px solid #000000 }");
		
		// Bind buttons
		okButton.clicked.connect(QApplication.instance(), "aboutQt()");
		quitButton.clicked.connect(QApplication.instance(), "quit()");
		
		// Populate layout managers
		gridMain.addWidget(consoleWidget, 0, 0, 1, 3);
		gridMain.addWidget(okButton, 1, 1);
		gridMain.addWidget(quitButton, 1, 2);
		
		// Configure layout managers
		gridMain.setColumnStretch(0, 1);
		gridMain.setRowStretch(0, 1);
	}
	
	public static void main(String args[]) {
		QApplication.initialize(args);
		new jPBSMain();
		QApplication.exec();
	}
}
