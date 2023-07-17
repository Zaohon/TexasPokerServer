package cn.blockmc.Zao_hon;

import cn.blockmc.Zao_hon.command.CommandHandler;
import cn.blockmc.Zao_hon.command.CommandSender;

public class ChatHandler implements CommandHandler{

	@Override
	public boolean handle(CommandSender sender, String cmd, String[] args) {
		String name = sender instanceof TexasPokerServer?"admin":((UserClient)sender).getName();
		String msg = "{" + name + "}"+cmd;
		TexasPokerServer.get().broadcast(msg);
		return true;
		
		
	}

}
