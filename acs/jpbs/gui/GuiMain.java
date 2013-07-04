package acs.jpbs.gui;

import acs.jpbs.Launcher;
import acs.jpbs.jPBSMain;
import acs.jpbs.gui.widgets.StatusTree;
import acs.jpbs.status.IPbsObject;
import acs.jpbs.status.PbsServerHandler;

import com.trolltech.qt.core.QRect;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QCloseEvent;
import com.trolltech.qt.gui.QDesktopWidget;
import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QTextEdit;
import com.trolltech.qt.gui.QWidget;

public class GuiMain extends QWidget {
	private final int WIDTH = 500;
	private final int HEIGHT = 700;
	
	public final Signal1<String> printSignal = new Signal1<String>();
	public final Signal3<IPbsObject, IPbsObject, Integer> updateModelSignal = new Signal3<IPbsObject, IPbsObject, Integer>();
	public final Signal0 updateTreeSignal = new Signal0();
	
	private static QTextEdit console;
	private static StatusTree sTree;
	private static GuiMain instance = null;
	
	private GuiMain() {
		QDesktopWidget qdw = new QDesktopWidget();
		QRect qr = qdw.screenGeometry(qdw.primaryScreen());
		
		int screenWidth = qr.width();
		int screenHeight = qr.height();
		int x = (screenWidth - WIDTH) / 2;
		int y = (screenHeight - HEIGHT) / 2;
		
		this.initGui();
		this.resize(WIDTH, HEIGHT);
		this.move(x,y);
		this.setWindowTitle("jPBS Client");
		this.show();
	}
	
	public static GuiMain getInstance() {
		if(instance == null) {
			instance = new GuiMain();
		}
		return instance;
	}
	
	@SuppressWarnings("unused")
	public static void main(String args[]) {
		System.setProperty("com.trolltech.qt.thread-check", "no");
		QApplication.initialize(args);
		GuiMain me = getInstance();
		new Launcher().start();
		QApplication.exec();
		System.exit(0);
	}
	
	@Override
	public void closeEvent(QCloseEvent event) {
		jPBSMain.getInstance().disconnect();
		event.accept();
	}
	
	private void initGui() {
		// Initialize layout managers
		QGridLayout gridMain = new QGridLayout(this);
		
		// Initialize widgets
		sTree = new StatusTree(this);
		QPushButton okButton = new QPushButton("Ok", this);
		QPushButton quitButton = new QPushButton("Quit", this);
		console = new QTextEdit("", this);
		
		console.setReadOnly(true);
		
		// Configure widgets
		console.setStyleSheet("QTextEdit { background-color: #ffffff; border: 1px solid #000000 }");
		
		// Bind signals
		okButton.clicked.connect(QApplication.instance(), "aboutQt()");
		quitButton.clicked.connect(this, "close()");
		QApplication.instance().lastWindowClosed.connect(QApplication.instance(), "quit()");
		this.printSignal.connect(console, "insertPlainText(String)");
		this.updateModelSignal.connect(sTree.model(), "setupModelData(IPbsObject, IPbsObject, Integer)");
		this.updateTreeSignal.connect(sTree, "updateMe()");
		
		// Populate layout managers
		gridMain.addWidget(sTree, 0, 0, 1, 3);
		gridMain.addWidget(console, 1, 0, 1, 3);
		gridMain.addWidget(okButton, 2, 1);
		gridMain.addWidget(quitButton, 2, 2);
		
		// Configure layout managers
		gridMain.setColumnStretch(0, 1);
		gridMain.setRowStretch(0, 1);
		gridMain.setRowStretch(1, 1);
	}
	
	public void printToConsole(String info) {
		this.printSignal.emit("\n"+info);
	}
	
	public void updateStatus() {
		this.updateModelSignal.emit(PbsServerHandler.getInstance(), null, 0);
		this.updateTreeSignal.emit();
	}
}
