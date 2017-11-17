package co.insou.chomp.config;

import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;

import com.google.gson.stream.JsonReader;
import com.google.inject.Provider;

import co.insou.chomp.Chomp;
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
		File file = new File("config.json");

		if (file.exists())
		{
			return new JsonReader(Try.to(() -> new FileReader(file)));
		}

		Chomp.print("WARNING: config.json not found, defaulting to jar packaged configuration");

		return new JsonReader(new InputStreamReader(Chomp.class.getResourceAsStream("/config.json")));
	}

}
