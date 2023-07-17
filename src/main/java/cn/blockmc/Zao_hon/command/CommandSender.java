package cn.blockmc.Zao_hon.command;

import java.util.List;

public interface CommandSender {
	void sendMessage(String str);
	void sendMessages(List<String> list);
}
