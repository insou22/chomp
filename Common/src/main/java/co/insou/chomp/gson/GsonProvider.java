package co.insou.chomp.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public enum GsonProvider {

	;

	private static final Gson GSON;

	static
	{
		GSON = new GsonBuilder()
				.registerTypeAdapterFactory(new ClassTypeAdapterFactory())
				.registerTypeAdapter(Class.class, new ClassTypeAdapter())
				.create();
	}

	public static Gson getGson()
	{
		return GsonProvider.GSON;
	}

}
