package cn.blockmc.Zao_hon;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
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
		try(BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))){
//			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			bw.write("enter ur name:\r\n");
			bw.flush();

			
			UserClient client = null;
			InputStream i = socket.getInputStream();
			BufferedReader b = new BufferedReader(new InputStreamReader(i));
			String name = b.readLine();
			client = new UserClient(name, socket, this);
			instance.clientJoin(name, client);
			client.sendMessage("Welcome to TexasPoker," + name);
			client.sendMessage("Enjoy ur game");
			String str;
			while (instance.isUserOnline(name) && (str = b.readLine()) != null) {
				log.debug(name + " send " + str);
				instance.commandExecute(client, str, null);
			}

		} catch (IOException e) {
			log.info(socket.getRemoteSocketAddress() + " disconnecting");
		} 
	}

}