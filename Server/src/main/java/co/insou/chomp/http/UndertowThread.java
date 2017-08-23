package co.insou.chomp.http;

import java.util.concurrent.atomic.AtomicBoolean;

import co.insou.chomp.Chomp;
import io.undertow.Undertow;

public class UndertowThread extends Thread {

	private final Undertow undertow;
	private final AtomicBoolean running = new AtomicBoolean(false);

	public UndertowThread(Undertow undertow)
	{
		this.undertow = undertow;
	}

	@Override
	public void run()
	{
		if (!this.running.getAndSet(true))
		{
			this.undertow.start();
			Chomp.print("Listening for HTTP requests...");
		}
	}

	public void end()
	{
		if (this.running.getAndSet(false))
		{
			this.undertow.stop();
			Chomp.print("Stopped listening for HTTP requests.");
		}
	}

}
