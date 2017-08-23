package co.insou.chomp.http;

import com.google.inject.AbstractModule;

import co.insou.chomp.service.ServiceRegistry;

public final class HttpModule extends AbstractModule {

	public static HttpModule create()
	{
		return new HttpModule();
	}

	private HttpModule()
	{

	}

	@Override
	protected void configure()
	{
		this.bind(ServiceRegistry.class).toInstance(HttpServiceRegistry.create());
	}

}
