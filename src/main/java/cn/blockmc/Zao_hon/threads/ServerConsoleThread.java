package cn.blockmc.Zao_hon.threads;

import java.util.Scanner;

import cn.blockmc.Zao_hon.Application;
import cn.blockmc.Zao_hon.TexasPokerServer;

public class ServerConsoleThread extends Thread {
	private static TexasPokerServer instance = TexasPokerServer.get();

	@Override
	public void run() {
		Application.logger.info("server console start");
		Scanner sc = new Scanner(System.in);
		while (!instance.isClose()) {
			String msg = sc.nextLine();
			instance.commandExecute(instance, msg, null);
		}
		sc.close();
		Application.logger.info("server console start");
	}

}
