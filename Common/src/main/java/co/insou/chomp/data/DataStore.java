package co.insou.chomp.data;

public interface DataStore {

	static DataStore named(String storeName) {
		return Data.namedStore(storeName);
	}

	DataPage getDefaultPage();

	DataPage getPageOf(String key);

}
