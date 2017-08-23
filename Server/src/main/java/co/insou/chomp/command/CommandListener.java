package co.insou.chomp.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import co.insou.chomp.Chomp;

@Singleton
public final class CommandListener {

	private final Chomp chomp;
	private final CommandUnknown unknown;
	private final Provider<CommandContainer> containerProvider;
	private final Scanner scanner = new Scanner(System.in);
	private final Map<String, Command> commands = new HashMap<>();

	@Inject
	private CommandListener(Chomp chomp, CommandUnknown unknown, Provider<CommandContainer> containerProvider)
	{
		this.chomp = chomp;
		this.unknown = unknown;
		this.containerProvider = containerProvider;
	}

	public void begin()
	{
		this.containerProvider.get().loadCommands();

		while (Chomp.RUNNING.get())
		{
			String line = this.scanner.nextLine();

			if (StringUtils.isBlank(line))
			{
				continue;
			}

			String[] parts = line.split("\\s");

			Command command = this.commands.getOrDefault(parts[0].toLowerCase(), this.unknown);

			command.execute(this.getArguments(parts));
		}
	}

	private List<String> getArguments(String[] parts)
	{
		List<String> arguments = new ArrayList<>();

		for (int i = 1; i < parts.length; i++)
		{
			arguments.add(parts[i]);
		}

		return arguments;
	}

	public void registerCommand(Command command)
	{
		this.commands.put(command.commandName().toLowerCase(), command);

		for (String alias : command.aliases())
		{
			this.commands.put(alias.toLowerCase(), command);
		}
	}

}
