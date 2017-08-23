package co.insou.chomp.command;

import com.google.inject.Inject;
import com.google.inject.Injector;

final class CommandContainer {

	private final CommandListener listener;
	private final Injector injector;

	@Inject
	private CommandContainer(CommandListener listener, Injector injector)
	{
		this.listener = listener;
		this.injector = injector;
	}

	public void loadCommands()
	{
		this.registerCommands(new Class[] {
				CommandHelp.class,
				CommandStop.class
		});
	}

	private <T extends Command> void registerCommands(Class<T>[] commands)
	{
		for (Class<T> command : commands)
		{
			this.listener.registerCommand(this.injector.getInstance(command));
		}
	}

}
