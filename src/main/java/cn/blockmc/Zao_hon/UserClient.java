package cn.blockmc.Zao_hon;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.List;

import cn.blockmc.Zao_hon.command.CommandSender;

public class UserClient implements CommandSender {
	private String name;
	private Socket socket;
	private BufferedWriter bw = null;
	private BufferedReader bi = null;
	private ServerReadThread thread;
	private int chip;

	public UserClient(String name, Socket socket, ServerReadThread thread) {
		this.name = name;
		this.socket = socket;
		this.thread = thread;
		this.chip = 1000;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public void sendMessages(List<String> list) {
		list.forEach(str -> sendMessage(str));
	}

	@Override
	public void sendMessage(String str) {
		try {
			if (bw == null) {
				bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			}
			bw.write(str + "\r\n");
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	public String readLine() throws IOException {
		byte[] bytes = new byte[256];
		int byteOffset = 0;
		InputStream i = socket.getInputStream();
		
		while(!socket.isClosed()) {
			int lenth = i.read(bytes, byteOffset, 2);
			if(lenth==1) {
				byte b = bytes[byteOffset];
				if(b==3) {
					throw new IOException("user quit");
				}else if(b == 8) {
					socket.getOutputStream().write(new byte[] { 0x08, 0x20, 0x08 });
				}else {
					byteOffset++;
				}
			}else if(lenth==2){
				//byteOffset+=2;
				break;
			}
		}
		String str = new String(bytes,0,byteOffset);
		Application.logger.debug(str+":"+byteOffset);
		return str;
	}

	public boolean isOnline() {
		return TexasPokerServer.get().isUserOnline(name);
	}

	public int getChip() {
		return chip;
	}

	public int setChip(int chip) {
		return this.chip = chip;
	}

	public int addChip(int chip) {
		return this.chip += chip;
	}

	public int subChip(int chip) {
		return this.chip -= chip;
	}
}
