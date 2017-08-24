package co.insou.chomp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.AbstractModule;

import co.insou.chomp.gson.GsonProvider;
import co.insou.chomp.http.HttpModule;

public final class ChompModule extends AbstractModule {

	static ChompModule create()
	{
		return new ChompModule();
	}

	private ChompModule()
	{

	}

	@Override
	protected void configure()
	{
		this.bind(Gson.class).toInstance(GsonProvider.getGson());

		this.install(HttpModule.create());
	}

	private Gson buildGson()
	{
		return new GsonBuilder().create();
	}

}
