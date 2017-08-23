package co.insou.chomp.command;

import java.util.List;

import co.insou.chomp.Chomp;

public class CommandHelp implements Command {

	@Override
	public void execute(List<String> arguments)
	{
		Chomp.print("=========================");
		Chomp.print("|                       |");
		Chomp.print("|    Chomp Webserver    |");
		Chomp.print("|                       |");
		Chomp.print("=========================");
		Chomp.print("");
		Chomp.print("Commands:");
		Chomp.print("=> help (h)");
		Chomp.print("===> Display this help page");
		Chomp.print("");
		Chomp.print("=> stop");
		Chomp.print("===> Stop the Chomp Webserver");
	}

	@Override
	public String commandName()
	{
		return "help";
	}

	@Override
	public String[] aliases()
	{
		return new String[] {
				"h"
		};
	}

}
