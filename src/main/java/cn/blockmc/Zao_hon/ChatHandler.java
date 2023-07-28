package cn.blockmc.Zao_hon;

import cn.blockmc.Zao_hon.command.CommandHandler;
import cn.blockmc.Zao_hon.command.CommandSender;
import cn.blockmc.Zao_hon.game.Game;
import cn.blockmc.Zao_hon.game.Room;

public class ChatHandler implements CommandHandler{

	@Override
	public boolean handle(CommandSender sender, String cmd, String[] args) {
		String name = sender instanceof TexasPokerServer?"admin":((UserClient)sender).getName();
		
		if(Room.getRoom(name)!=null) {
			Room room = Room.getRoom(name);
			return room.handle(sender, cmd, args);
		}
		
		String msg = "[lobby]" + name + " says "+cmd;
		TexasPokerServer.get().broadcast(msg);
		return true;
		
	}

}
