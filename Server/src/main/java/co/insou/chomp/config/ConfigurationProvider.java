package co.insou.chomp.config;

import java.io.FileReader;

import com.google.gson.stream.JsonReader;
import com.google.inject.Provider;

import co.insou.chomp.bean.Beans;
import co.insou.chomp.gson.GsonProvider;
import co.insou.chomp.util.except.Try;

class ConfigurationProvider implements Provider<ChompConfiguration> {

	@Override
	public ChompConfiguration get()
	{
		return GsonProvider.getGson().fromJson(this.readConfigFile(), Beans.build(ChompConfiguration.class));
	}

	private JsonReader readConfigFile()
	{
		return new JsonReader(Try.to(() -> new FileReader("config.json")));
	}

}
