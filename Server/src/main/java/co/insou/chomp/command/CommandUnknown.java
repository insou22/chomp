package co.insou.chomp.command;

import java.util.List;

import co.insou.chomp.Chomp;

public class CommandUnknown implements Command {

	@Override
	public void execute(List<String> arguments)
	{
		Chomp.print("Unknown command. Type help or h for help.");
	}

	@Override
	public String commandName()
	{
		return null;
	}

}
