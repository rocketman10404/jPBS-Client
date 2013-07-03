package acs.jpbs;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import acs.jpbs.core.PbsJob;
import acs.jpbs.core.PbsQueue;
import acs.jpbs.core.PbsServer;
import acs.jpbs.gui.GuiMain;
import acs.jpbs.net.jPBSClientInterface;
import acs.jpbs.net.jPBSServerInterface;
import acs.jpbs.utils.Logger;

public class jPBSMain implements jPBSClientInterface {
	private static jPBSServerInterface server;
	private static PbsServer pbsServer = null;
	public static GuiMain gui;
	
	public jPBSMain() {
		gui = GuiMain.getInstance();
		
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
	
	public static void logInfo(String _info) {
		Logger.logInfo(_info);
		gui.printToConsole(_info);
	}
	
	public static void run() {
		new jPBSMain();
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
	
	public static void updateQueueLocal(PbsQueue newQueue) {
		PbsQueue origQueue = pbsServer.getQueue(newQueue.getName());
		if(origQueue ==  null) {
			pbsServer.addQueue(newQueue);
		} else {
			origQueue.makeCopy(newQueue);
		}
	}
	
	public static void updateServerLocal(PbsServer newServer) {
		if(pbsServer == null) pbsServer = newServer;
		else {
			pbsServer.makeCopy(newServer);
		}
	}
	
	public void updateJob(PbsJob newJob) throws RemoteException {
		updateJobLocal(newJob);
	}
	
	public void updateQueue(PbsQueue newQueue) throws RemoteException {
		updateQueueLocal(newQueue);
	}
	
	public void updateServer(PbsServer newServer) throws RemoteException { 
		updateServerLocal(newServer);
	}
}
