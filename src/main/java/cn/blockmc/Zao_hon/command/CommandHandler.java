package cn.blockmc.Zao_hon.command;

import cn.blockmc.Zao_hon.UserClient;

public interface CommandHandler {
	
	boolean handle(UserClient client,String cmd,String[] args);
}
