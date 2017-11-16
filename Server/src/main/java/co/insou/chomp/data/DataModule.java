package co.insou.chomp.data;

import com.google.inject.AbstractModule;

public final class DataModule extends AbstractModule {

	public static DataModule create()
	{
		return new DataModule();
	}

	private DataModule()
	{

	}

	@Override
	protected void configure()
	{
		DataStoreProvider provider = new DataStoreProvider();
		this.requestInjection(provider);

		this.bind(DataProvider.class).toInstance(new DataStoreProvider());
		this.requestStaticInjection(Data.class);
	}

}
