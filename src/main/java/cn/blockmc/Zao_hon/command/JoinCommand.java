package cn.blockmc.Zao_hon.command;

import cn.blockmc.Zao_hon.UserClient;
import cn.blockmc.Zao_hon.game.Game;

public class JoinCommand implements ICommand {

	@Override
	public String getName() {
		return "join";
	}

	@Override
	public String getDescription() {
		return "join a game room";
	}

	@Override
	public boolean canBeConsole() {
		return false;
	}

	@Override
	public boolean handle(CommandSender sender, String[] args) {
		UserClient client = (UserClient) sender;
		if (args.length != 1) {
			client.sendMessage("please enter the id of ur room like,/join 110");
			return true;
		}
		String a = args[0];
		try {
			int id = Integer.valueOf(a);
			Game game = Game.getGame(id);
			if(game.isFull()) {
				client.sendMessage("this room is full");
				return true;
			}
//			client.sendMessage("u have joined room id "+id);
			game.userJoin(client);
		} catch (Exception e) {
			client.sendMessage("id must be a number");
			return true;
		}

		return true;
	}

}
