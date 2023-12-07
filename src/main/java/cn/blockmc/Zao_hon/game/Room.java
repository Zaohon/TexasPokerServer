package cn.blockmc.Zao_hon.game;

import java.util.HashMap;
import java.util.LinkedHashMap;

import cn.blockmc.Zao_hon.Application;
import cn.blockmc.Zao_hon.UserClient;
import cn.blockmc.Zao_hon.command.CommandHandler;
import cn.blockmc.Zao_hon.command.CommandSender;

public class Room implements CommandHandler {
	public final static boolean QUICK_MODE = true;

	enum RoomStage {
		WAITING, READY, PLAYING;
	}

	private static final int MAX_USERS = 5;
	private static final int MIN_USERS = 2;
	private LinkedHashMap<String, UserClient> users = new LinkedHashMap<String, UserClient>();
	private RoomStage stage = RoomStage.WAITING;
	private Game game;
	private int id;

	public Room(int id) {
		this.id = id;
	}

	public int getID() {
		return id;
	}

	public void userJoin(UserClient client) {
		users.put(client.getName(), client);
		userRooms.put(client.getName(), this);
		info(client.getName() + " joined the room,there are " + users.size() + " players now");

		checkStart();
	}

	public void userQuit(String name) {
		users.remove(name);
		userRooms.remove(name);
		info(name + " quit the room,there are " + users.size() + "players now");
	}

	private void info(UserClient user, String str) {
		String msg = "[ROOM INFO]" + str;
		user.sendMessage(msg);
	}

	private void info(String str) {
		String msg = "[ROOM INFO]" + str;
		this.broadcast(msg);
	}

	private void checkStart() {
		if (users.size() >= MIN_USERS) {
			this.process(RoomStage.READY);
		}
	}

	private void process(RoomStage stage) {
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
			info("Room ready");
			new GameCountingThread().start();
			break;
		case PLAYING:
			info("Game Started");
			this.game = new Game(users, this);
			break;
		}
	}

	public boolean isFull() {
		return users.size() == MAX_USERS;
	}

	public void broadcast(String str) {
		users.values().forEach(u -> u.sendMessage(str));
	}

	public void start() {

	}

	@Override
	public boolean handle(CommandSender sender, String str, String[] args) {
		UserClient user = (UserClient) sender;
		String name = user.getName();

		String[] strs = str.split(" ");
		String cmd = strs[0];

		if (cmd.equalsIgnoreCase("quit")) {
			this.userQuit(name);
			info(user, "u have quited the room " + id);
			return true;
		}

		if (stage == RoomStage.PLAYING) {
			try {
				Option option = null;
				OptionType type = OptionType.valueOf(cmd.toUpperCase());
				String bet = strs.length == 1 ? "0" : strs[1];

				option = new Option(type, Integer.valueOf(bet));
				this.game.handleOption(name, option);
				return true;
			} catch (IllegalArgumentException e) {

			} catch (Exception e) {
				String err = e.getMessage();
				info(user, err);
			}
		}

		this.broadcast("[CHAT]" + name + ":" + str);

		return true;
	}

	private static HashMap<String, Room> userRooms = new HashMap<String, Room>();

	private static HashMap<Integer, Room> rooms = new HashMap<Integer, Room>();

	public static Room getRoom(int id) {
		rooms.putIfAbsent(id, new Room(id));
		return rooms.get(id);
	}

	public static Room getRoom(String user) {
		return userRooms.get(user);
	}

	public void reset() {
		this.process(RoomStage.WAITING);
	}

	class GameCountingThread extends Thread {

		private boolean checkUserSize() {
			return users.size() >= MIN_USERS;
		}

		private void delay(int t) {
			if (!QUICK_MODE) {
				try {
					Thread.sleep(1 * 1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		public void run() {
			if (!QUICK_MODE) {
				info("Game Will Start in 20 seconds");
				for (int i = 0; i < 10; i++) {
					delay(1);
				}
				info("Game Will Start in 10 seconds");
				for (int i = 0; i < 5; i++) {
					delay(1);
				}
			}
			info("Game Will Start in 5 seconds");
			for (int i = 1; i > 5; i++) {
				info("Game Will Start in " + (5 - i) + " seconds");
				delay(1);
			}
			if (checkUserSize()) {
				process(RoomStage.PLAYING);
			} else {
				info("Game cant start bcsof too few players");
			}

		}
	}

}
