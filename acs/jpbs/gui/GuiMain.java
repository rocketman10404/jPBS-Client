package acs.jpbs.gui;

import acs.jpbs.Launcher;

import com.trolltech.qt.core.QRect;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QDesktopWidget;
import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QTextEdit;
import com.trolltech.qt.gui.QWidget;

public class GuiMain extends QWidget {
	private final int WIDTH = 500;
	private final int HEIGHT = 350;
	
	public final Signal1<String> printSignal = new Signal1<String>();
	
	private static QTextEdit console;
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
	
	private void initGui() {
		// Initialize layout managers
		QGridLayout gridMain = new QGridLayout(this);
		
		// Initialize widgets
		QPushButton okButton = new QPushButton("Ok", this);
		QPushButton quitButton = new QPushButton("Quit", this);
		console = new QTextEdit("", this);
		//console.setReadOnly(true);
		
		// Configure widgets
		console.setStyleSheet("QTextEdit { background-color: #ffffff; border: 1px solid #000000 }");
		
		// Bind buttons
		okButton.clicked.connect(QApplication.instance(), "aboutQt()");
		quitButton.clicked.connect(QApplication.instance(), "quit()");
		this.printSignal.connect(console, "insertPlainText(String)");
		
		// Populate layout managers
		gridMain.addWidget(console, 0, 0, 1, 3);
		gridMain.addWidget(okButton, 1, 1);
		gridMain.addWidget(quitButton, 1, 2);
		
		// Configure layout managers
		gridMain.setColumnStretch(0, 1);
		gridMain.setRowStretch(0, 1);
	}
	
	public void printToConsole(String info) {
		this.printSignal.emit("\n"+info);
	}
	
	@SuppressWarnings("unused")
	public static void main(String args[]) {
		System.setProperty("com.trolltech.qt.thread-check", "no");
		QApplication.initialize(args);
		GuiMain me = getInstance();
		new Launcher().start();
		QApplication.exec();
	}
}
