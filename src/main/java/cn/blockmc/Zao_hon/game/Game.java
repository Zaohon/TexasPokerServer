package cn.blockmc.Zao_hon.game;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import cn.blockmc.Zao_hon.Application;
import cn.blockmc.Zao_hon.UserClient;
import cn.blockmc.Zao_hon.command.CommandHandler;
import cn.blockmc.Zao_hon.command.CommandSender;

public class Game implements CommandHandler {
	enum GAME_STAGE {
		WAITING, READY, BEFORE_ROLL, AFTER_ROLL, TRANSFER, RIVER;
	}

	private static HashMap<Integer, Game> games = new HashMap<Integer, Game>();
	private static HashMap<String, Game> userGame = new HashMap<String, Game>();

	public static Game getGame(int id) {
		games.putIfAbsent(id, new Game(id));
		return games.get(id);
	}

	public static Game getGame(String name) {
		return userGame.get(name);
	}

	public static boolean inGame(String name) {
		return userGame.containsKey(name);
	}

	private static final int MAX_USERS = 5;
	private static final int MIN_USERS = 2;
	private GAME_STAGE stage = GAME_STAGE.WAITING;
	private Poker poker;
	private PokerCard[] sharedCards = new PokerCard[5];
	private LinkedHashMap<String, UserClient> users = new LinkedHashMap<String, UserClient>();
	private HashMap<String, Boolean> isOut = new HashMap<String, Boolean>();
	private HashMap<String, PokerCard[]> userCards = new HashMap<String, PokerCard[]>();
	private String optionUser = "";
	private Option option;
	private int id;

	private int pot;
	private int roundPot;

	public Game(int id) {
		this.id = id;
		poker = new Poker();
	}

	public int getID() {
		return id;
	}

	private void process(GAME_STAGE stage) {
		this.stage = stage;
		Application.logger.debug("Room " + id + " process to stage " + stage);
		switch (stage) {
		case READY:
			new GameCountingThread().start();
			break;
		case BEFORE_ROLL:
			new BeforeRollThread().start();
			break;
		case AFTER_ROLL:
			new AfterRollThread().start();
			break;
		case TRANSFER:
			new TransferThread().start();
			break;
		case RIVER:
			new RiverThread().start();
			break;
		default:
			break;
		}
	}

	public void userJoin(UserClient client) {
		users.put(client.getName(), client);
		userGame.put(client.getName(), this);
		info(client.getName() + " has joined the room,there are " + users.size() + " now");
		if (users.size() >= MIN_USERS) {
			this.process(GAME_STAGE.READY);
		}
	}

	public void userQuit(String name) {
		users.remove(name);
		userGame.remove(name);
	}

	public boolean isFull() {
		return users.size() >= MAX_USERS;
	}

	public void broadcast(String str) {
		users.values().forEach(u -> u.sendMessage(str));
	}

	private void info(String str) {
		String msg = "[GAME INFO]" + str;
		this.broadcast(msg);
	}

	private void info(UserClient user, String str) {
		String msg = "[GAME INFO]" + str;
		user.sendMessage(msg);
	}

	public int getUserSize() {
		return users.size();
	}

	public GAME_STAGE getStage() {
		return stage;
	}

	private void reset() {
		option = null;
		optionUser = "";
		stage = GAME_STAGE.WAITING;
		poker = new Poker();
		userCards.clear();
		isOut.clear();
	}

	private void newRound() {
		for (Entry<String, UserClient> entry : users.entrySet()) {
			String name = entry.getKey();
			UserClient user = entry.getValue();
			if (isOut.get(name)) {
				continue;
			}
			info(user, "Now it is ur move!");
			info(user, "Choose Check , Bet , or Fold");
			this.optionUser = name;
			while (option == null) {
//				Application.logger.debug("waiting for option");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			Application.logger.debug(option.toString());

			OptionType type = option.getType();
			int m = option.getPot();

			switch (type) {
			case BET:
				this.info(optionUser + " just bet " + m + "$");
				pot += m;
				break;
			case CHECK:
				this.info("dont check pls,u can just bet now , u are making the server crashed,u fucking asshole");
				break;
			case FOLD:
				this.info("dont fold pls,u can just bet now , u are making the server crashed,u fucking asshole");
				break;
			case RAISE:
				break;
			default:
				break;
			}
			optionUser = "";
			option = null;
		}
		this.info("this round over,the pot is coming to " + pot);
	}

	@Override
	public boolean handle(CommandSender sender, String cmd, String[] args) {
		UserClient user = (UserClient) sender;
		String name = user.getName();
		if (cmd.equalsIgnoreCase("bet") || cmd.equalsIgnoreCase("check") || cmd.equalsIgnoreCase("fold")) {
			if (stage == GAME_STAGE.WAITING) {
				this.info(user, "game havnt started");
				return true;
			}

			if (!optionUser.equals(name)) {
				this.info(user, "It is not ur turn yet!");
				return true;
			}
			if (cmd.equalsIgnoreCase("bet")) {
				int money = 100;
				option = new Option(OptionType.BET, 100);
			} else if (cmd.equalsIgnoreCase("fold")) {
				option = new Option(OptionType.FOLD);
			} else if (cmd.equalsIgnoreCase("check")) {
				option = new Option(OptionType.CHECK);
			}
			return true;
		} else if (cmd.equalsIgnoreCase("quit")) {
			this.userQuit(name);
			info(user,"u have quited the room " + id);
		} else {
			String msg = name + " says " + cmd;
			this.broadcast(msg);
		}
		return true;
	}

	class GameCountingThread extends Thread {
		@Override
		public void run() {
			int time = 5;
			for (int i = time; i > 0; i--) {
				info("Game Will Start in " + i + " seconds");
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
			try {
				Thread.sleep(1000);
				info("Game Start!");
				Thread.sleep(1000);
				info("Entering Before Roll Stage,Check ur card!");
				Thread.sleep(1000);
				users.forEach((name, user) -> {
					isOut.put(name, false);
					PokerCard[] cards = new PokerCard[2];
					cards[0] = poker.popCard();
					cards[1] = poker.popCard();
					userCards.put(getName(), cards);
					user.sendMessage("u get:" + cards[0].getDesc() + "," + cards[1].getDesc());
				});
				newRound();
				process(GAME_STAGE.AFTER_ROLL);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

	class AfterRollThread extends Thread {
		@Override
		public void run() {
			try {
				Thread.sleep(1000);
				info("Entering After Roll Stage,reveal shared cards:");
				for (int i = 0; i < 3; i++) {
					Thread.sleep(1000);
					PokerCard card = poker.popCard();
					sharedCards[i] = card;
					info("{" + card.toString() + "}");
				}
				newRound();
				process(GAME_STAGE.TRANSFER);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	class TransferThread extends Thread {
		@Override
		public void run() {
			try {
				Thread.sleep(1000);
				info("Entering Transfer stage,reveal transfer card:");
				PokerCard card = poker.popCard();
				sharedCards[3] = card;
				info("{" + card.toString() + "}");
				newRound();
				process(GAME_STAGE.RIVER);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

	class RiverThread extends Thread {
		@Override
		public void run() {
			try {
				Thread.sleep(1000);
				info("Entering river stage,reveal the last card:");
				Thread.sleep(1000);
				PokerCard card = poker.popCard();
				sharedCards[4] = card;
				info("{" + card.toString() + "}");
				Thread.sleep(1000);
				newRound();
				Thread.sleep(1000);
				info("the game is finished , winner is zao_hon , who got the full pot , conguration!");
				Thread.sleep(1000);
				info("game reseting..");
				reset();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}
}