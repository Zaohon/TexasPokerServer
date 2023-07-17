package cn.blockmc.Zao_hon.command;

import java.util.HashMap;
import cn.blockmc.Zao_hon.TexasPokerServer;
import cn.blockmc.Zao_hon.UserClient;

public class CommandDispatcher implements CommandHandler {
	private TexasPokerServer server;
	private HashMap<String, ICommand> mCommands = new HashMap<String,ICommand>();
	public CommandDispatcher(TexasPokerServer server) {
		this.server = server;
		this.registerCommand(new HelloCommand());
	}
	
	public void registerCommand(ICommand command) {
		mCommands.put(command.getName().toLowerCase(), command);
	}

	@Override
	public boolean handle(CommandSender sender, String cmd, String[] args) {
		if(!mCommands.containsKey(cmd)) {
			sender.sendMessage("command not found");
			return false;
		}
		ICommand command = mCommands.get(cmd);
		command.handle(sender, args);
		return true;
	}
	

}
