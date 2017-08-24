package co.insou.chomp.command;

import java.util.List;

import co.insou.chomp.Chomp;

public class CommandHelp implements Command {

	@Override
	public void execute(List<String> arguments)
	{
		Chomp.print("Commands:");
		Chomp.print("- help (h): Display this help page");
		Chomp.print("- stop (s): Stop the Chomp Webserver");
	}

	@Override
	public String commandName()
	{
		return "help";
	}

	@Override
	public String[] aliases()
	{
		return new String[]{
				"h"
		};
	}

}
