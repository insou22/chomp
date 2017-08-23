package co.insou.chomp.command;

import java.util.List;

public interface Command {

	void execute(List<String> arguments);

	String commandName();

	default String[] aliases()
	{
		return new String[0];
	}

}
