package cn.blockmc.Zao_hon.command;

import java.util.List;

import cn.blockmc.Zao_hon.UserClient;

public interface ICommand {
	/**
	 * Gets the name of the command
	 */
	String getName();

	/**
	 * Gets the description of the command for the help system
	 */
	String getDescription();

	/**
	 * Can the sender of this command be a console?
	 */
	boolean canBeConsole();

	/**
	 * Called when this command is executed. By this time the permission has been
	 * checked, and if this command does not accept the console as a sender, that
	 * wont trigger this command.
	 * 
	 * @param sender The sender of this command. If canBeConsole() == false, this
	 *               will only ever be an instance of a Player
	 * @param label  The command name or the alias that was used to call this
	 *               command
	 * @param args   The arguments for this command
	 * @return True if this command was executed. False otherwise
	 */
	boolean handle(CommandSender sender, String[] args);
}
