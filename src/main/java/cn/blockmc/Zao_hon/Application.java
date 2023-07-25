package cn.blockmc.Zao_hon;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.blockmc.Zao_hon.game.Poker;
import cn.blockmc.Zao_hon.game.PokerCard;
import cn.blockmc.Zao_hon.game.pokergroup.AbstractPokerGroup;
import cn.blockmc.Zao_hon.game.pokergroup.PokerGroupFactory;

public class Application {
	public static Logger logger = LoggerFactory.getLogger(TexasPokerServer.class);
	
	public static void main(String[] args) throws IOException{
		
		
		Poker poker = new Poker();
		PokerCard[] hands = new PokerCard[2];
		for(int i=0;i<2;i++) {
			hands[i] = poker.popCard();
		}
		PokerCard[] shared = new PokerCard[5];
		for(int i=0;i<5;i++) {
			shared[i] = poker.popCard();
		}
		logger.debug("hand is "+PokerGroupFactory.printPokerCards(hands, 2));
		logger.debug("shared is "+PokerGroupFactory.printPokerCards(shared, 5));
		AbstractPokerGroup group = PokerGroupFactory.findBestGroup(hands, shared);
		logger.debug("best is "+group);

		logger.debug("服务端启动");
		logger.debug("Waiting for connection");
		TexasPokerServer server = TexasPokerServer.get();
		ServerSocket serverSocket = new ServerSocket(1818);
		while(true) {
			Socket socket = serverSocket.accept();
			ServerReadThread thread = new ServerReadThread(socket);
			thread.start();
			logger.info(socket.getRemoteSocketAddress()+" connecting");
		}
	}
}
