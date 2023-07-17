package cn.blockmc.Zao_hon;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.List;

import cn.blockmc.Zao_hon.command.CommandHandler;

public class UserClient {
	private String name;
	private Socket socket;
	public UserClient(String name,Socket socket) {
		this.name = name;
		this.socket = socket;
	}
	
	public String getName() {
		return name;
	}
	
	public void sendMessages(List<String> list) {
		list.forEach(str->sendMessage(str));
	}
	
	public void sendMessage(String str) {
		try {
			OutputStream os = socket.getOutputStream();
    		PrintStream ps = new PrintStream(os);
    		ps.println(str);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
