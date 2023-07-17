package cn.blockmc.Zao_hon;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.blockmc.Zao_hon.command.CommandHandler;


public class TexasPokerServer {
	protected static Logger logger = LoggerFactory.getLogger(TexasPokerServer.class);
	
	public static void main(String[] args) throws IOException{
		instance = get();
		logger.debug("服务端启动");
		logger.debug("Waiting for connection");
		ServerSocket serverSocket = new ServerSocket(1818);
		while(true) {
			Socket socket = serverSocket.accept();
			ServerReadThread thread = new ServerReadThread(socket);
			thread.start();
			logger.info(socket.getRemoteSocketAddress()+" connecting");
		}
	}
	


	private static TexasPokerServer instance = null;
	public static TexasPokerServer get() {
		if(instance == null) {
			instance = new TexasPokerServer();
		}
		return instance;
	}
	
	private CommandHandler commandExecutor = null;
	private CommandHandler chatExecutor = null;
	
	private LinkedList<Socket> sockets = new LinkedList<Socket>();
	private HashMap<String,UserClient> clients = new HashMap<String,UserClient>();
	
	public void clientJoin(String name,UserClient client) {
		logger.info(name+" has joined");
		clients.put(name, client);
	}
	public void clientQuit(String name) {
		logger.info(name+" has quited");
		clients.remove(name);
	}
	
	public UserClient getClient(String name) {
		return clients.get(name);
	}
	
	
	public boolean commandExecute(UserClient client,String cmd,String[] args) {
		if(cmd.startsWith("/")) {
			cmd = cmd.substring(1);
			return commandExecutor.handle(client, cmd, args);
		}else {
			return chatExecutor.handle(client, cmd, args);
		}
	}

}
