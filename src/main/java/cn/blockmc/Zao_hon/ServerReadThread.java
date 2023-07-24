package cn.blockmc.Zao_hon;

import java.io.IOException;
import java.net.Socket;

import org.slf4j.Logger;

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
			String name = socket.getRemoteSocketAddress().toString();
			UserClient client = new UserClient(name, socket, this);
			client.sendMessage("Enter ur name:");
			name = client.readLine();

			client.setName(name);
			instance.clientJoin(name, client);
			client.sendMessage("[System]Welcome to TexasPoker," + name);
			client.sendMessage("[System]Enjoy ur game");

			while (client.isOnline()) {
				String str = client.readLine();
				if (str == null) {
					instance.clientQuit(name);
					break;
				}
				log.debug(name + " send " + str);
				instance.commandExecute(client, str, null);
			}
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