package cn.blockmc.Zao_hon.threads;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import cn.blockmc.Zao_hon.Application;
import cn.blockmc.Zao_hon.TexasPokerServer;

public class ServerSocketThread extends Thread {

	@Override
	public void run() {

		Application.logger.info("server closed");
	}

}