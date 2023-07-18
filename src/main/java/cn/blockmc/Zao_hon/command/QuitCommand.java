package cn.blockmc.Zao_hon.command;

import cn.blockmc.Zao_hon.TexasPokerServer;
import cn.blockmc.Zao_hon.UserClient;

public class QuitCommand implements ICommand{

	@Override
	public String getName() {
		return "quit";
	}

	@Override
	public String getDescription() {
		return "quit the game";
	}

	@Override
	public boolean canBeConsole() {
		return false;
	}

	@Override
	public boolean handle(CommandSender sender, String[] args) {
		UserClient client = (UserClient) sender;
		TexasPokerServer.get().clientQuit(client.getName());
		return true;
	}

}
