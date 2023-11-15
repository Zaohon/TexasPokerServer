package cn.blockmc.Zao_hon;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class UserSocket {
	private Socket socket;
	private BufferedWriter bw = null;
	private BufferedReader bi = null;

	public UserSocket(Socket socket) {
		this.socket = socket;

	}

	public void sendMsg(String str) {
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

		while (!socket.isClosed()) {
			int lenth = i.read(bytes, byteOffset, 2);
			if (lenth == 1) {
				byte b = bytes[byteOffset];
				if (b == 3) {
					return null;

				} else if (b == 8) {
					socket.getOutputStream().write(new byte[] { 0x08, 0x20, 0x08 });
					byteOffset = Math.max(0, byteOffset - 1);
				} else {
					byteOffset++;
				}
			} else if (lenth == 2) {
				// byteOffset+=2;
				break;
			}
		}
		if (byteOffset == 0) {
			return readLine();
		}
		String str = new String(bytes, 0, byteOffset);
		return str;
	}
}
