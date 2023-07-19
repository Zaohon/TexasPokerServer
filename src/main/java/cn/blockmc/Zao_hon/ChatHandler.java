package cn.blockmc.Zao_hon;

import cn.blockmc.Zao_hon.command.CommandHandler;
import cn.blockmc.Zao_hon.command.CommandSender;
import cn.blockmc.Zao_hon.game.Game;

public class ChatHandler implements CommandHandler{

	@Override
	public boolean handle(CommandSender sender, String cmd, String[] args) {
		String name = sender instanceof TexasPokerServer?"admin":((UserClient)sender).getName();
		
		if(Game.inGame(name)) {
			Game game = Game.getGame(name);
			game.handle(sender, cmd, args);
			return true;
		}
		
		String msg = "{" + name + "}"+cmd;
		TexasPokerServer.get().broadcast(msg);
		return true;
		
	}

}
