package cn.blockmc.Zao_hon.threads;

import java.io.IOException;
import java.net.Socket;

import org.slf4j.Logger;

import cn.blockmc.Zao_hon.Application;
import cn.blockmc.Zao_hon.TexasPokerServer;
import cn.blockmc.Zao_hon.UserClient;
import cn.blockmc.Zao_hon.UserSocket;
import cn.blockmc.Zao_hon.storage.UserInfo;
import cn.blockmc.Zao_hon.storage.UserStorage;

public class ServerReadThread extends Thread {
	private static Logger log = Application.logger;
	private TexasPokerServer instance;
	private Socket socket;

	public ServerReadThread(Socket socket) {
		instance = TexasPokerServer.get();
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			UserSocket us = new UserSocket(socket);
			us.sendMsg("Enter ur name:");
			String name = us.readLine();

			UserInfo info = UserStorage.get().loadUser(name);
			UserClient client = new UserClient(info, us);
			instance.clientJoin(name, client);
			client.sendMessage("[System]Welcome to TexasPoker♠," + name);
			client.sendMessage("[System]Enjoy ur game");

			while (client.isOnline()) {
				String str = us.readLine();
				if (str == null) {
					instance.clientQuit(name);
					break;
				}
				// log.debug(name + " send " + str);
				instance.commandExecute(client, str, null);
			}
			instance.clientQuit(name);
		} catch (IOException e) {
			log.info(socket.getRemoteSocketAddress() + " disconnecting");
		}
		if (!socket.isClosed()) {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}