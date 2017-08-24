package co.insou.chomp.command;

import java.util.List;

import com.google.inject.Inject;

import co.insou.chomp.Chomp;

public final class CommandStop implements Command {

	private final Chomp chomp;

	@Inject
	private CommandStop(Chomp chomp)
	{
		this.chomp = chomp;
	}

	@Override
	public void execute(List<String> arguments)
	{
		Chomp.print("Stopping Chomp...");
		this.chomp.end();
	}

	@Override
	public String commandName()
	{
		return "stop";
	}

	@Override
	public String[] aliases()
	{
		return new String[] {
				"s"
		};
	}

}
