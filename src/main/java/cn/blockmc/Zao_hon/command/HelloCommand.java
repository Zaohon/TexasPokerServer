package cn.blockmc.Zao_hon.command;

import cn.blockmc.Zao_hon.UserClient;

public class HelloCommand implements ICommand{

	@Override
	public String getName() {
		return "hello";
	}

	@Override
	public String getDescription() {
		return "say hello to console and get a response";
	}

	@Override
	public boolean canBeConsole() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean handle(CommandSender client, String[] args) {
		client.sendMessage("nice to meet u , it is TexasPokerServer made by zao_hon");
		return true;
	}

}
