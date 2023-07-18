package cn.blockmc.Zao_hon;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.blockmc.Zao_hon.command.CommandDispatcher;

public class Application {
	protected static Logger logger = LoggerFactory.getLogger(TexasPokerServer.class);
	
	public static void main(String[] args) throws IOException{

		logger.debug("服务端启动");
		logger.debug("Waiting for connection");
		TexasPokerServer server = TexasPokerServer.get();
		ServerSocket serverSocket = new ServerSocket(1818);
		while(true) {
			Socket socket = serverSocket.accept();
			ServerReadThread thread = new ServerReadThread(socket);
			thread.start();
			logger.info(socket.getRemoteSocketAddress()+" connecting");
		}
	}
}
