package cn.blockmc.Zao_hon;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.List;

import cn.blockmc.Zao_hon.command.CommandSender;

public class UserClient implements CommandSender{
	private String name;
	private Socket socket;
	private BufferedWriter bw = null;
	private ServerReadThread thread;
	private int chip;
	public UserClient(String name,Socket socket,ServerReadThread thread) {
		this.name = name;
		this.socket = socket;
		this.thread = thread;
		this.chip = 1000;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public void sendMessages(List<String> list) {
		list.forEach(str->sendMessage(str));
	}
	
	@Override
	public void sendMessage(String str) {
		try {
			if(bw ==null) {
				bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			}
			bw.write(str+"\r\n");
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int getChip() {
		return chip;
	}
	
	public int setChip(int chip) {
		return this.chip=chip;
	}
	
	public int addChip(int chip) {
		return this.chip+=chip;
	}
	
	public int subChip(int chip) {
		return this.chip-=chip;
	}
}
