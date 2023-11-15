package cn.blockmc.Zao_hon.command;

import cn.blockmc.Zao_hon.UserClient;

public class MoneyCommand implements ICommand {

	@Override
	public String getName() {
		return "money";
	}

	@Override
	public String getDescription() {
		return "check ur money";
	}

	@Override
	public boolean canBeConsole() {
		return false;
	}

	@Override
	public boolean handle(CommandSender sender, String[] args) {
		UserClient client = (UserClient) sender;
		client.sendMessage("you have " + client.getChip() + "$ chips");
		return true;
	}

}
