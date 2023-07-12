package cn.blockmc.Zao_hon;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TexasPokerServer {
	protected static Logger logger = LoggerFactory.getLogger("TexasPokerServer");
	
	
	public static void main(String[] args) throws IOException {
		System.out.print("服务端启动");
		ServerSocket serverSocket = new ServerSocket(1818);
		
		while(true) {
			Socket socket = serverSocket.accept();
			ServerReadThread thread = new ServerReadThread(socket);
			thread.start();
			logger.info("客户端加入");
		}
		
		
		
		
		//serverSocket.close();
	}
	

}
