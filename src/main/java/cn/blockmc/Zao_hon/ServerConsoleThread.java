package cn.blockmc.Zao_hon;

import java.util.Scanner;

public class ServerConsoleThread extends Thread{
	private static TexasPokerServer instance = TexasPokerServer.get();
    @Override
    public void run(){
		Scanner sc = new Scanner(System.in);
		while(true) {
			String msg = sc.nextLine();
			instance.commandExecute(instance, msg, null);
		}
    }

}
