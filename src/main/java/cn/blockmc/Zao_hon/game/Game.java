package cn.blockmc.Zao_hon.game;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

import cn.blockmc.Zao_hon.Application;
import cn.blockmc.Zao_hon.TexasPokerServer;
import cn.blockmc.Zao_hon.UserClient;

public class Game {
	private static final int MAX_USERS = 5;
	private static final int MIN_USERS = 2;
	private GAME_STAGE stage = GAME_STAGE.WAITING;
	private Poker poker;
	private LinkedHashMap<String, UserClient> users = new LinkedHashMap<String, UserClient>();
	private HashMap<String,PokerCard[]> userCards = new HashMap<String,PokerCard[]>();
	private int id;

	public Game(int id) {
		this.id = id;
		poker = new Poker();
	}

	public int getID() {
		return id;
	}

	public void start() {
		new GameCountingThread().start();
	}

	private void process(GAME_STAGE stage) {
		this.stage = stage;
		Application.logger.debug("Room " + id + " process to stage " + stage);
		switch (stage) {
		case READY:

			new GameCountingThread().start();
			break;
		case BEFORE_ROLL:
		default:
			new BeforeRollThread().start();
			break;

		}
	}

	public void userJoin(UserClient client) {
		users.put(client.getName(), client);
		broadcast(client.getName() + "has joined the room,there are " + users.size() + " now");
		if (users.size() >= MIN_USERS) {
			this.process(GAME_STAGE.READY);
		}
	}

	public void userQuit(String name) {
		users.remove(name);
	}

	public boolean isFull() {
		return users.size() >= MAX_USERS;
	}

	public void broadcast(String str) {
		String msg = "[GAME INFO]" + str;
		users.values().forEach(u -> u.sendMessage(msg));
	}

	public int getUserSize() {
		return users.size();
	}

	private static HashMap<Integer, Game> games = new HashMap<Integer, Game>();

	public static Game getGame(int id) {
		games.putIfAbsent(id, new Game(id));
		return games.get(id);
	}

	public GAME_STAGE getStage() {
		return stage;
	}

	enum GAME_STAGE {
		WAITING, READY, BEFORE_ROLL, AFTER_ROLL, TRANSFER, RIVER;
	}

	class GameCountingThread extends Thread {
		@Override
		public void run() {
			int time = 5;
			for (int i = time; i > 0; i--) {
				broadcast("Game Will Start in " + i + " seconds");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			process(GAME_STAGE.BEFORE_ROLL);
		}
	}

	class BeforeRollThread extends Thread {
		@Override
		public void run() {
			broadcast("Game Start!");
			broadcast("Entering Before Roll Stage,Check ur card!");
			users.forEach((name,user)->{
				PokerCard[] cards = new PokerCard[2];
				cards[0] =poker.popCard();
				cards[1] =poker.popCard();
				userCards.put(getName(), cards);
				user.sendMessage("u get:"+ cards[0].getDesc()+","+cards[1].getDesc());
			});
			
			
		}
	}

}