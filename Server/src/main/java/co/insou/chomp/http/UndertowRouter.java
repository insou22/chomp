package co.insou.chomp.http;

import com.google.inject.Inject;

import co.insou.chomp.Chomp;
import co.insou.chomp.config.ChompConfiguration;
import co.insou.chomp.config.UndertowConfiguration;
import io.undertow.Undertow;
import io.undertow.Undertow.Builder;
import io.undertow.server.handlers.BlockingHandler;

public final class UndertowRouter {

	private final Undertow undertow;
	private final HttpRequestHandler handler;

	@Inject
	private UndertowRouter(HttpRequestHandler handler, ChompConfiguration config)
	{
		this.handler = handler;

		this.undertow = this.buildUndertow(config.getHttp());
	}

	public void start()
	{
		Chomp.print("Launching HTTP server...\n");
		this.undertow.start();
		this.printBlankLine();
		Chomp.print("Successfully launched HTTP server");
	}

	public void stop()
	{
		Chomp.print("Shutting down HTTP server...");
		this.undertow.stop();
		Chomp.print("Successfully shut down HTTP server.");
	}

	private Undertow buildUndertow(UndertowConfiguration config)
	{
		Builder builder = Undertow.builder();

		builder.addHttpListener(config.getPort(), config.getHostname());
		builder.setHandler(new BlockingHandler(this.handler));
		builder.setIoThreads(config.getIoThreads());
		builder.setWorkerThreads(config.getWorkerThreads());

		return builder.build();
	}

	private void printBlankLine()
	{
		System.out.println();
	}

}
