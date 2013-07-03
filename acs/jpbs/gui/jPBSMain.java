package acs.jpbs.gui;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
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
	private static jPBSServerInterface server;
	private static PbsServer pbsServer = null;
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
		
		logInfo("Connecting...");
		try {
			Registry registry = LocateRegistry.getRegistry();
			server = (jPBSServerInterface)registry.lookup("jPBS-Server");
			UnicastRemoteObject.exportObject(this, 0);
			server.register(this);
			logInfo("Connected to server.");
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
	
	public static void logInfo(String _info) {
		Logger.logInfo(_info);
		console.setText(console.text()+"\n"+_info);
	}
	
	public void updateServer(PbsServer newServer) throws RemoteException { 
		updateServerLocal(newServer);
	}
	
	public static void updateServerLocal(PbsServer newServer) {
		if(pbsServer == null) pbsServer = newServer;
		else {
			pbsServer.makeCopy(newServer);
		}
	}
	
	public void updateQueue(PbsQueue newQueue) throws RemoteException {
		updateQueueLocal(newQueue);
	}
	
	public static void updateQueueLocal(PbsQueue newQueue) {
		PbsQueue origQueue = pbsServer.getQueue(newQueue.getName());
		if(origQueue ==  null) {
			pbsServer.addQueue(newQueue);
		} else {
			origQueue.makeCopy(newQueue);
		}
	}
	
	public void updateJob(PbsJob newJob) throws RemoteException {
		updateJobLocal(newJob);
	}
	
	public static void updateJobLocal(PbsJob newJob) {
		PbsQueue jQueue = pbsServer.getQueue(newJob.getQueueName()); 
		if(jQueue == null) {
			jQueue = new PbsQueue(newJob.getQueueName(), pbsServer);
			jQueue.addJob(newJob);
			pbsServer.addQueue(jQueue);
		} else {
			PbsJob origJob = jQueue.getJob(newJob.getId());
			if(origJob == null) {
				jQueue.addJob(newJob);
			} else {
				origJob.makeCopy(newJob);
			}
		}
	}

	
	public static void main(String args[]) {
		QApplication.initialize(args);
		new jPBSMain();
		QApplication.exec();
		try {
			updateServerLocal(server.getServerObject());
			for(PbsQueue q : server.getQueueArray()) {
				updateQueueLocal(q);
			}
			for(PbsJob j : server.getJobArray()) {
				updateJobLocal(j);
			}
		} catch(Exception e) {
			Logger.logException("Error retreiving PBS data", e);
		}
		logInfo("Server data retreived successfully");
		logInfo("PBS Server: "+pbsServer.getHostName());
		logInfo("Queues: "+pbsServer.getNumQueues()+" total");
		logInfo("Jobs: "+pbsServer.getNumJobs()+" total");
	}
}
