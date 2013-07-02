package acs.jpbs.gui;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.trolltech.qt.core.QRect;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QDesktopWidget;
import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QWidget;

import acs.jpbs.core.PbsJob;
import acs.jpbs.core.PbsQueue;
import acs.jpbs.core.PbsServer;
import acs.jpbs.net.jPBSClientInterface;
import acs.jpbs.net.jPBSServerInterface;
import acs.jpbs.utils.Logger;

public class jPBSMain extends QWidget implements jPBSClientInterface {
	private final int WIDTH = 500;
	private final int HEIGHT = 350;
	private jPBSServerInterface server;
	private static QLabel console;
	
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
		
		Logger.logInfo("Connecting...");
		try {
			server = (jPBSServerInterface)Naming.lookup("jPBS-Server");
			UnicastRemoteObject.exportObject(this);
			server.register(this);
		} catch(Exception e) {
			Logger.logException("Client unable to connect to server.", e);
		}
	}
	
	private void initGui() {
		// Initialize layout managers
		QGridLayout gridMain = new QGridLayout(this);
		
		// Initialize widgets
		QPushButton okButton = new QPushButton("Ok", this);
		QPushButton quitButton = new QPushButton("Quit", this);
		console = new QLabel("", this);
		
		// Configure widgets
		console.setStyleSheet("QLabel { background-color: #ffffff; border: 1px solid #000000 }");
		
		// Bind buttons
		okButton.clicked.connect(QApplication.instance(), "aboutQt()");
		quitButton.clicked.connect(QApplication.instance(), "quit()");
		
		// Populate layout managers
		gridMain.addWidget(console, 0, 0, 1, 3);
		gridMain.addWidget(okButton, 1, 1);
		gridMain.addWidget(quitButton, 1, 2);
		
		// Configure layout managers
		gridMain.setColumnStretch(0, 1);
		gridMain.setRowStretch(0, 1);
	}
	
	public void updateServer(PbsServer newServer) throws RemoteException { }
	
	public void updateQueue(PbsQueue newQueue) throws RemoteException { }
	
	public void updateJob(PbsJob newJob) throws RemoteException { }
	
	public static void main(String args[]) {
		QApplication.initialize(args);
		new jPBSMain();
		QApplication.exec();
	}
}
