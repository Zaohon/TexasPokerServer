package cn.blockmc.Zao_hon.game;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

import cn.blockmc.Zao_hon.Application;
import cn.blockmc.Zao_hon.UserClient;
import cn.blockmc.Zao_hon.command.CommandHandler;
import cn.blockmc.Zao_hon.command.CommandSender;
import cn.blockmc.Zao_hon.game.pokergroup.AbstractPokerGroup;
import cn.blockmc.Zao_hon.game.pokergroup.PokerGroupFactory;

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
	private Poker poker = new Poker();
	private PokerCard[] sharedCards = new PokerCard[5];
	private LinkedHashMap<String, UserClient> roomUsers = new LinkedHashMap<String, UserClient>();
	private LinkedList<String> tableUsers = new LinkedList<String>();
	private HashMap<String, PokerCard[]> pocketCards = new HashMap<String, PokerCard[]>();
	private String optionUser = "";
	private Option option;
	private int id;
	private int totalPot;

	private HashMap<String, Integer> userBets = new HashMap<String, Integer>();
	private int roundPot = 100;

	public Game(int id) {
		this.id = id;
	}

	public int getID() {
		return id;
	}

	private void process(GAME_STAGE stage) {
		if (this.stage == stage) {
			return;
		}
		this.stage = stage;
		Application.logger.debug("Room " + id + " process to stage " + stage);
		switch (stage) {
		case WAITING:
			checkStart();
			break;
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
		roomUsers.put(client.getName(), client);
		userGame.put(client.getName(), this);
		info(client.getName() + " has joined the room,there are " + roomUsers.size() + " now");
		checkStart();
	}

	private void checkStart() {
		if (roomUsers.size() >= MIN_USERS) {
			this.process(GAME_STAGE.READY);
		}
	}

	public void userQuit(String name) {
		roomUsers.remove(name);
		tableUsers.remove(name);
		userGame.remove(name);
		info(name + " quit the room,there are " + roomUsers.size() + " now");
	}

	public boolean isFull() {
		return roomUsers.size() >= MAX_USERS;
	}

	public void broadcast(String str) {
		roomUsers.values().forEach(u -> u.sendMessage(str));
	}

	private void info(String str) {
		String msg = "[GAME INFO]" + str;
		this.broadcast(msg);
	}

	private void info(UserClient user, String str) {
		String msg = "[GAME INFO]" + str;
		user.sendMessage(msg);
	}

	public int getRoomSize() {
		return roomUsers.size();
	}

	public GAME_STAGE getStage() {
		return stage;
	}

	private void reset() {
		option = null;
		optionUser = "";
		poker = new Poker();
		sharedCards = new PokerCard[5];
		pocketCards.clear();
		tableUsers.clear();
		totalPot = 0;
		userBets.clear();
		roundPot = 0;
		process(GAME_STAGE.WAITING);
	}

	private int finishCount = 0;

	private void newRound() {
		for (String name : tableUsers) {
			UserClient user = roomUsers.get(name);

			info(user, "Now it is ur move!");
			info(user, "Choose Check,Bet or Fold");
			this.optionUser = name;
			while (option == null) {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			OptionType type = option.getType();
			int m = option.getPot();

			switch (type) {
			case BET:
				this.info(optionUser + " bet " + roundPot + "$");
				totalPot += m;
				userBets.put(name, roundPot);
				finishCount++;
				break;
			case CHECK:
				this.info("dont check , fu ck u ,just bet");
				this.info(optionUser + " bet " + roundPot + "$");
				totalPot += m;
				userBets.put(name, roundPot);
				finishCount++;
				break;
			case FOLD:
				tableUsers.remove(name);
				this.info(optionUser + " fold");
				break;
			case RAISE:
				int newPot = m + userBets.getOrDefault(name, 0);
				this.info(optionUser + " raise to " + newPot + "$");
				totalPot += m;
				roundPot = newPot;
				userBets.put(name, roundPot);
				finishCount = 1;
				break;
			default:
				break;
			}
			optionUser = "";
			option = null;
			if (finishCount == tableUsers.size()) {
				finishCount = 0;
				return;
			}

		}
		newRound();

//		for (Entry<String, UserClient> entry : roomUsers.entrySet()) {
//		String name = entry.getKey();
//		UserClient user = entry.getValue();
//		if (isOut.get(name)) {
//			continue;
//		}
//		info(user, "Now it is ur move!");
//		info(user, "Choose Check,Bet or Fold");
//		this.optionUser = name;
//		while (option == null) {
//			delay(1);
//		}
//		OptionType type = option.getType();
//		int m = option.getPot();
//
//		switch (type) {
//		case BET:
//			this.info(optionUser + " just follow with " + roundPot + "$");
//			totalPot += m;
//			roundBets.put(name, roundPot);
//			break;
//		case CHECK:
//			this.info("dont check pls,u can just bet now , u are making the server crashed,u fucking asshole");
//			break;
//		case FOLD:
//			this.info("dont folgood pls,u can just bet now , u are making the server crashed,u fucking asshole");
//			break;
//		case RAISE:
//			int newPot = m + roundBets.getOrDefault(name, 0);
//			this.info(optionUser + " just raise to " + newPot + "$");
//			totalPot += m;
//			roundPot = newPot;
//			roundBets.put(name, roundPot);
//			finishCount = 0;
//			break;
//		default:
//			break;
//		}
//		optionUser = "";
//		option = null;
//		finishCount++;
//		if (finishCount == users.size()) {
//			finishCount = 0;
//			return;
//		}
//
//	}
//	newRound();

//		for (Entry<String, UserClient> entry : roomUsers.entrySet()) {
//			String name = entry.getKey();
//			UserClient user = entry.getValue();
//			if (isOut.get(name)) {
//				continue;
//			}
//			info(user, "Now it is ur move!");
//			info(user, "Choose Check,Bet or Fold");
//			this.optionUser = name;
//			while (option == null) {
//				delay(1);
//			}
//			OptionType type = option.getType();
//			int m = option.getPot();
//
//			switch (type) {
//			case BET:
//				this.info(optionUser + " just follow with " + roundPot + "$");
//				totalPot += m;
//				roundBets.put(name, roundPot);
//				break;
//			case CHECK:
//				this.info("dont check pls,u can just bet now , u are making the server crashed,u fucking asshole");
//				break;
//			case FOLD:
//				this.info("dont folgood pls,u can just bet now , u are making the server crashed,u fucking asshole");
//				break;
//			case RAISE:
//				int newPot = m + roundBets.getOrDefault(name, 0);
//				this.info(optionUser + " just raise to " + newPot + "$");
//				totalPot += m;
//				roundPot = newPot;
//				roundBets.put(name, roundPot);
//				finishCount = 0;
//				break;
//			default:
//				break;
//			}
//			optionUser = "";
//			option = null;
//			finishCount++;
//			if (finishCount == users.size()) {
//				finishCount = 0;
//				return;
//			}
//
//		}
//		newRound();
	}

	@Override
	public boolean handle(CommandSender sender, String str, String[] args) {
		UserClient user = (UserClient) sender;
		String name = user.getName();

		String[] strs = str.split(" ");
		String cmd = strs[0];

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
				int money = roundPot - userBets.getOrDefault(name, 0);
				if (money < 0) {
					this.info(user, "u dont have enough money to follow,try all-in,after it is supported");
				}

				if (strs.length > 1) {
					try {
						int bet = Integer.valueOf(strs[1]);
						if (bet < money) {
							this.info(user, "u must bet as equal or bigger than " + money);
							return true;
						}
//						if(bet>user.getChip()){
//							this.info(user,"u dont have enough money to bet,try all-in,but not now");
//							return true;
//						}
						if (bet > money) {
							option = new Option(OptionType.RAISE, bet);
							return true;
						}
					} catch (Exception e) {
						this.info(user, "u can only bet a number");
						return true;
					}
				}
				option = new Option(OptionType.BET, money);
				Application.logger.debug(option+" "+name+" "+optionUser);
			} else if (cmd.equalsIgnoreCase("fold")) {
				option = new Option(OptionType.FOLD);
			} else if (cmd.equalsIgnoreCase("check")) {
				option = new Option(OptionType.CHECK);
			}
			return true;
		} else if (cmd.equalsIgnoreCase("quit")) {
			this.userQuit(name);
			info(user, "u have quited the room " + id);
		} else {
			String msg = "[chat]" + name + ":" + str;
			this.broadcast(msg);
		}
		return true;
	}

	private String getSharedCards(int lenth) {
		String str = "";
		for (int i = 0; i < lenth; i++) {
			str += sharedCards[i];
			str += i != lenth - 1 ? "," : ".";
		}
		return str;
	}

	class GameCountingThread extends Thread {
		@Override
		public void run() {
			int time = 5;
			for (int i = time; i > 0; i--) {
				info("Game Will Start in " + i + " seconds");
				delay(1);
			}
			process(GAME_STAGE.BEFORE_ROLL);
		}
	}

	class BeforeRollThread extends Thread {
		@Override
		public void run() {
			for(String name:roomUsers.keySet()) {
				tableUsers.add(name);
			}
			
			info("Game Start!");
			delay(1);
			info("Entering Before Roll Stage,Check ur card!");
			delay(1);

			for (String name : tableUsers) {
				UserClient user = roomUsers.get(name);
				PokerCard[] cards = new PokerCard[2];
				cards[0] = poker.popCard();
				cards[1] = poker.popCard();
				pocketCards.put(name, cards);
				info(user, "u get:" + cards[0] + "," + cards[1]);
			}
			newRound();
			
			if(tableUsers.size()==1) {
				String user = tableUsers.element();
				info("Game finished,all players fold,except the winner "+user+" who got "+totalPot+" $!");
				delay(1);
				info("game reseting..");
				reset();
				return;
			}
			process(GAME_STAGE.AFTER_ROLL);

			info("this round over,the total pot is up to " + totalPot + "$");
		}
	}

	class AfterRollThread extends Thread {
		@Override
		public void run() {
			delay(1);
			info("Entering After Roll Stage,reveal shared cards:");
			for (int i = 0; i < 3; i++) {
				PokerCard card = poker.popCard();
				sharedCards[i] = card;
			}
			delay(1);
			info(getSharedCards(3));
			newRound();
			
			if(tableUsers.size()==1) {
				String user = tableUsers.element();
				info("Game finished,all players fold,except the winner "+user+" who got "+totalPot+" $!");
				delay(1);
				info("game reseting..");
				reset();
				return;
			}
			
			process(GAME_STAGE.TRANSFER);
			info("this round over,the total pot is up to " + totalPot + "$");
		}
	}

	class TransferThread extends Thread {
		@Override
		public void run() {

			delay(1);
			info("Entering Transfer stage,reveal transfer card:");
			PokerCard card = poker.popCard();
			sharedCards[3] = card;
			info(getSharedCards(4));
			newRound();
			
			if(tableUsers.size()==1) {
				String user = tableUsers.element();
				info("Game finished,all players fold,except the winner "+user+" who got "+totalPot+" $!");
				delay(1);
				info("game reseting..");
				reset();
				return;
			}
			
			process(GAME_STAGE.RIVER);

			
			info("this round over,the total pot is up to " + totalPot + "$");
		}
	}

	class RiverThread extends Thread {
		@Override
		public void run() {

			delay(1);
			info("Entering river stage,reveal the last card:");
			PokerCard card = poker.popCard();
			sharedCards[4] = card;
			info(getSharedCards(5));
			newRound();
			
			if(tableUsers.size()==1) {
				String user = tableUsers.element();
				info("Game finished,all players fold,except the winner "+user+" who got "+totalPot+" $!");
				delay(1);
				info("game reseting..");
				reset();
				return;
			}
			
			delay(1);
			String winner = "";
			AbstractPokerGroup winnerGroup = null;
			for (String name : tableUsers) {
				PokerCard[] hand = pocketCards.get(name);
				AbstractPokerGroup newGroup = PokerGroupFactory.findBestGroup(hand, sharedCards);
				info("player " + name + "'s best fitting card group is " + newGroup);

				if (winner == "" || newGroup.compareTo(winnerGroup) > 0) {
					winner = name;
					winnerGroup = newGroup;
				}
			}
			info("the game is finished , winner is " + winner + ",who got " + winnerGroup);
			delay(1);
			info("game reseting..");
			reset();

		}
	}

	private final static boolean QUICK_MODE = false;

	private static void delay(int t) {
		if (!QUICK_MODE) {
			try {
				Thread.sleep(1000 * t);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}