package co.insou.chomp;

import java.util.concurrent.atomic.AtomicBoolean;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import co.insou.chomp.command.CommandListener;
import co.insou.chomp.http.UndertowRouter;
import co.insou.chomp.preload.Preloader;

@Singleton
public final class Chomp {

	public static final AtomicBoolean RUNNING = new AtomicBoolean(false);

	public static void main(String[] args)
	{
		Injector injector = Guice.createInjector(ChompModule.create());

		Chomp chomp = injector.getInstance(Chomp.class);
		Chomp.RUNNING.set(true);
		chomp.start();
	}

	public static void print(String... lines)
	{
		for (String line : lines)
		{
			System.out.println("[Chomp] " + line);
		}
	}

	private final UndertowRouter router;
	private final Provider<CommandListener> commands;
	private final Preloader preloader;

	@Inject
	private Chomp(UndertowRouter router, Provider<CommandListener> commands, Preloader preloader)
	{
		this.router = router;
		this.commands = commands;
		this.preloader = preloader;
	}

	private void start()
	{
		this.printMotd();
		this.preload();
		this.startUndertowRouter();
		this.listenForCommands();
	}

	public void end()
	{
		this.router.stop();
		Chomp.RUNNING.set(false);
		Chomp.print("Successfully stopped Chomp Server.");
	}

	private void preload()
	{
		this.preloader.preload();
	}

	private void startUndertowRouter()
	{
		this.router.start();
	}

	private void listenForCommands()
	{
		this.commands.get().begin();
	}

	private void printMotd()
	{
		Chomp.print(
				" ______     __  __     ______     __    __     ______ ",
				"/\\  ___\\   /\\ \\_\\ \\   /\\  __ \\   /\\ \"-./  \\   /\\  == \\",
				"\\ \\ \\____  \\ \\  __ \\  \\ \\ \\/\\ \\  \\ \\ \\-./\\ \\  \\ \\  _-/",
				" \\ \\_____\\  \\ \\_\\ \\_\\  \\ \\_____\\  \\ \\_\\ \\ \\_\\  \\ \\_\\  ",
				"  \\/_____/   \\/_/\\/_/   \\/_____/   \\/_/  \\/_/   \\/_/  v1.0.0",
				""
		);
	}

}
