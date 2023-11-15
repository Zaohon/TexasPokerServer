package cn.blockmc.Zao_hon;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {
	public static Logger logger = LoggerFactory.getLogger(TexasPokerServer.class);

	public static void main(String[] args) throws IOException {

		// Test
		// TODO

		logger.debug("program start");

		TexasPokerServer server = TexasPokerServer.get();
		server.start();

		while (!server.isClose()) {

		}

		logger.debug("program end");
	}

}
