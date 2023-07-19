package cn.blockmc.Zao_hon;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import cn.blockmc.Zao_hon.command.CommandDispatcher;
import cn.blockmc.Zao_hon.command.CommandHandler;
import cn.blockmc.Zao_hon.command.CommandSender;

public class TexasPokerServer implements CommandSender {
	private static Logger logger = Application.logger;
	private static TexasPokerServer instance = null;

	public static TexasPokerServer get() {
		if (instance == null) {
			instance = new TexasPokerServer();
		}
		return instance;
	}

	public TexasPokerServer() {
		this.setCommandExecutor(new CommandDispatcher(instance));
		this.setChatExecutor(new ChatHandler());
		new ServerConsoleThread().start();
	}

	private CommandHandler commandExecutor = null;
	private CommandHandler chatExecutor = null;
	private HashMap<String, UserClient> clients = new HashMap<String, UserClient>();

	public void clientJoin(String name, UserClient client) {
		this.broadcast(name + " has joined the game");
		clients.put(name, client);
	}

	public void clientQuit(String name) {
		this.broadcast(name + " has quited the game");
		clients.remove(name);
	}

	public UserClient getClient(String name) {
		return clients.get(name);
	}

	public boolean commandExecute(CommandSender client, String cmd, String[] x) {
		if (cmd.startsWith("/")) {
			cmd = cmd.substring(1);
			String[] args = cmd.split(" ");
			cmd = args[0];
			args = Arrays.copyOfRange(args, 1, args.length);
			return commandExecutor.handle(client, cmd, args);
		} else {
			return chatExecutor.handle(client, cmd, x);
		}
	}

	public void setChatExecutor(CommandHandler handler) {
		this.chatExecutor = handler;
	}

	public void setCommandExecutor(CommandHandler handler) {
		this.commandExecutor = handler;
	}

	@Override
	public void sendMessage(String str) {
		logger.info(str);
	}

	@Override
	public void sendMessages(List<String> list) {
		list.forEach(str -> sendMessage(str));
	}

	public void broadcast(String str) {
		clients.values().forEach(client -> client.sendMessage(str));
		this.sendMessage(str);
	}
	
	public boolean isUserOnline(String name) {
		return clients.containsKey(name);
	}

}
