package co.insou.chomp.data;

import com.google.inject.Inject;

public enum Data {

	;

	@Inject private static DataProvider DATA_PROVIDER;

	static DataStore namedStore(String name) {
		return Data.DATA_PROVIDER.apply(name);
	}

}
