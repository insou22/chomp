package co.insou.chomp.http;

import java.io.FileReader;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.inject.Inject;

import co.insou.chomp.Chomp;
import co.insou.chomp.bean.Beans;
import co.insou.chomp.config.ChompConfiguration;
import co.insou.chomp.config.UndertowConfiguration;
import co.insou.chomp.util.except.Try;
import io.undertow.Undertow;
import io.undertow.Undertow.Builder;
import io.undertow.server.handlers.BlockingHandler;

public final class UndertowRouter {

	private final Undertow undertow;
	private final Gson gson;
	private final HttpRequestHandler handler;

	@Inject
	private UndertowRouter(Gson gson, HttpRequestHandler handler, ChompConfiguration config)
	{
		this.gson = gson;
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

	private UndertowConfiguration configureUndertow()
	{
		Class<? extends UndertowConfiguration> configType = Beans.build(UndertowConfiguration.class);

		return this.gson.fromJson(this.readConfigFile(), configType);
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

	private JsonReader readConfigFile()
	{
		return new JsonReader(Try.to(() -> new FileReader("config.json")));
	}

	private void printBlankLine()
	{
		System.out.println();
	}

}
