package cn.blockmc.Zao_hon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerReadThread extends Thread{
	private Socket socket;
	ServerReadThread(Socket socket){
		this.socket = socket;
	}
	
	@Override
	public void run() {
		try {
			InputStream is = socket.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			String line;
			
			while((line = reader.readLine()) != null) {
				System.out.print(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}