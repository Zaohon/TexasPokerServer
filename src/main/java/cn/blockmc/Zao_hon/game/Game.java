package cn.blockmc.Zao_hon.game;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import cn.blockmc.Zao_hon.Application;
import cn.blockmc.Zao_hon.UserClient;
import cn.blockmc.Zao_hon.game.pokergroup.AbstractPokerGroup;
import cn.blockmc.Zao_hon.game.pokergroup.PokerGroupFactory;

public class Game {
	enum GameStage {
		UNKNOWN,BEFORE_ROLL, AFTER_ROLL, TRANSFER, RIVER;
	}


	private Room room;
	private GameStage stage = GameStage.UNKNOWN;
	private Poker poker = new Poker();
	private PokerCard[] sharedCards = new PokerCard[5];
	private LinkedHashMap<String, UserClient> users = new LinkedHashMap<String, UserClient>();
	private LinkedList<String> tableUsers = new LinkedList<String>();
	private HashMap<String, PokerCard[]> pocketCards = new HashMap<String, PokerCard[]>();
	private String optionUser = "";
	private Option option;
	private int totalPot;

	private HashMap<String, Integer> userBets = new HashMap<String, Integer>();
	private int roundPot = 100;

	public Game(LinkedHashMap<String, UserClient> users, Room room) {
		this.users = users;
		this.room = room;
		this.process(GameStage.BEFORE_ROLL);
	}

	private void process(GameStage stage) {
		if (this.stage == stage) {
			return;
		}
		this.stage = stage;
		Application.logger.debug("Room " + room.getID() + " process to stage " + stage);
		switch (stage) {
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

	public GameStage getStage() {
		return stage;
	}

	private int finishCount = 0;

	private void newRound() {
		for (String name : tableUsers) {
			UserClient user = users.get(name);

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
	}

	public void handleOption(String name, Option option) throws Exception {
		OptionType type = option.getType();
		int bet = option.getPot();
		
		int betBefore = userBets.getOrDefault(name, 0);
		int betLeast = roundPot - betBefore;
		
		
		if(!name.equals(optionUser)) {
			throw new Exception("It is not ur turn!");
		}
		
		switch (type) {
		case BET:
	//		int money = roundPot - userBets.getOrDefault(name, 0);
//			if (money < 0) {
//				throw new Exception("u dont have enough money to follow,try all-in,after it is supported");
//			}
			if(bet==0) {
				this.option = new Option(OptionType.BET,betLeast);
				break;
			}

			if (bet < betLeast) {
				throw new Exception("u must bet as equal or bigger than " + betLeast);
			}
			if (bet > betLeast) {
				this.handleOption(name, new Option(OptionType.RAISE,bet+betBefore));
			}
			this.option = new Option(OptionType.BET, betLeast);
			break;
		case RAISE:
			if (bet <= betLeast) {
				throw new Exception("u must raise at least more than " + betLeast);
			}
			this.option = option;
			break;
		case FOLD:
			this.option = option;
			break;
		case CHECK:
			this.option = option;
			break;
		}

	}

	private String getSharedCards(int lenth) {
		String str = "";
		for (int i = 0; i < lenth; i++) {
			str += sharedCards[i];
			str += i != lenth - 1 ? "," : ".";
		}
		return str;
	}

	class BeforeRollThread extends Thread {
		@Override
		public void run() {
			for (String name : users.keySet()) {
				tableUsers.add(name);
			}

			info("Game Start!");
			delay(1);
			info("Entering Before Roll Stage,Check ur card!");
			delay(1);

			for (String name : tableUsers) {
				UserClient user = users.get(name);
				PokerCard[] cards = new PokerCard[2];
				cards[0] = poker.popCard();
				cards[1] = poker.popCard();
				pocketCards.put(name, cards);
				info(user, "u get:" + cards[0] + "," + cards[1]);
			}
			newRound();

			if (tableUsers.size() == 1) {
				String user = tableUsers.element();
				info("Game finished,all players fold,except the winner " + user + " who got " + totalPot + " $!");
				delay(1);
				info("game reseting..");
				room.reset();
				return;
			}
			process(GameStage.AFTER_ROLL);

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

			if (tableUsers.size() == 1) {
				String user = tableUsers.element();
				info("Game finished,all players fold,except the winner " + user + " who got " + totalPot + " $!");
				delay(1);
				info("game reseting..");
				room.reset();
				return;
			}

			process(GameStage.TRANSFER);
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

			if (tableUsers.size() == 1) {
				String user = tableUsers.element();
				info("Game finished,all players fold,except the winner " + user + " who got " + totalPot + " $!");
				delay(1);
				info("game reseting..");
				room.reset();
				return;
			}

			process(GameStage.RIVER);

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

			if (tableUsers.size() == 1) {
				String user = tableUsers.element();
				info("Game finished,all players fold,except the winner " + user + " who got " + totalPot + " $!");
				delay(1);
				info("game reseting..");
				room.reset();
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
			room.reset();

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