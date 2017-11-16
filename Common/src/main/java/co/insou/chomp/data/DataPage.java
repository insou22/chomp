package co.insou.chomp.data;

public interface DataPage {

	DataPage getPage(String key);

	<T> T get(String key, Class<T> type);

	String getString(String key);

	boolean getBoolean(String key);

	char getChar();

	byte getByte(String key);

	short getShort(String key);

	int getInt(String key);

	long getLong(String key);

	float getFloat(String key);

	double getDouble(String key);


}
