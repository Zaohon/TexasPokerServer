package cn.blockmc.Zao_hon.threads;

import java.net.Socket;
import java.util.HashMap;

public class ThreadManager {
	private static ThreadManager instance;

	public static ThreadManager get() {
		return instance;
	}

	public static ThreadManager init() {
		if (instance == null) {
			instance = new ThreadManager();
			instance.consoleThread.start();
			instance.socketThread.start();
		}
		return instance;
	}

	private HashMap<Socket, ServerReadThread> readThreads = new HashMap<Socket, ServerReadThread>();

	private ServerConsoleThread consoleThread = new ServerConsoleThread();

	private ServerSocketThread socketThread = new ServerSocketThread();

	public void createReadThread(Socket socket) {
		ServerReadThread thread = new ServerReadThread(socket);
		readThreads.put(socket, thread);
		thread.start();
	}

	public void stopReadThread(Socket socket) {

	}

}
