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
			us.sendMsg("请输入你的名字:");
			String name = us.readLine();

			UserInfo info = UserStorage.get().loadUser(name);
			UserClient client = new UserClient(info, us);
			instance.clientJoin(name, client);
			client.sendMessage("欢迎来到线上德州扑克♠," + name);
			client.sendMessage("Enjoy ur game");
			client.sendMessage("输入/help查看可使用的指令");
			client.sendMessage("当前在线玩家" + instance.getOnlines() + "人");
			client.sendMessage("你当前有筹码" + client.getChip() + "$");

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