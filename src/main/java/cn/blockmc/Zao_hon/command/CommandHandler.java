package cn.blockmc.Zao_hon.command;

import cn.blockmc.Zao_hon.UserClient;

public interface CommandHandler {
	
	boolean handle(CommandSender sender,String cmd,String[] args);
}
