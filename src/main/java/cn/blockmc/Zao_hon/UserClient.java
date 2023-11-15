package cn.blockmc.Zao_hon;

import java.util.List;

import cn.blockmc.Zao_hon.command.CommandSender;
import cn.blockmc.Zao_hon.storage.UserInfo;

public class UserClient implements CommandSender {

	private UserInfo info;
	private UserSocket socket;

	public UserClient(UserInfo info, UserSocket socket) {
		this.info = info;
		this.socket = socket;
	}

	public String getName() {
		return info.getName();
	}

	@Override
	public void sendMessages(List<String> list) {
		list.forEach(str -> socket.sendMsg(str));
	}

	@Override
	public void sendMessage(String str) {
		socket.sendMsg(str);
	}

	public boolean isOnline() {
		return TexasPokerServer.get().isUserOnline(info.getName());
	}

	public int getChip() {
		return info.getChip();
	}

	public void setChip(int chip) {
		info.setChip(chip);
	}

	public void addChip(int chip) {
		info.setChip(info.getChip() + chip);
	}

	public void subChip(int chip) {
		info.setChip(info.getChip() - chip);
	}

//	public void closeSocket() {
//		if (!this.socket.isClosed()) {
//			try {
//				this.socket.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//
//	}
}
