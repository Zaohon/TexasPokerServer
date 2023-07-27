package cn.blockmc.Zao_hon.threads;

import java.util.Scanner;

import cn.blockmc.Zao_hon.TexasPokerServer;

public class ServerConsoleThread extends Thread{
	private static TexasPokerServer instance = TexasPokerServer.get();
    @Override
    public void run(){
		Scanner sc = new Scanner(System.in);
		while(!instance.isClose()) {
			String msg = sc.nextLine();
			instance.commandExecute(instance, msg, null);
		}
    }

}
