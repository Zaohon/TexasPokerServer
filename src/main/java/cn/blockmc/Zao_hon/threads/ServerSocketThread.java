package cn.blockmc.Zao_hon.threads;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import cn.blockmc.Zao_hon.Application;
import cn.blockmc.Zao_hon.TexasPokerServer;

public class ServerSocketThread extends Thread {

	@Override
	public void run() {
		Application.logger.info("server socket start");
		try (ServerSocket serverSocket = new ServerSocket(1818)) {
			while (!TexasPokerServer.get().isClose()) {
				Socket socket = serverSocket.accept();
				ThreadManager.get().createReadThread(socket);
				Application.logger.info(socket.getRemoteSocketAddress() + " connecting");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		TexasPokerServer.get().close();
		Application.logger.info("server socket closed");
	}

}