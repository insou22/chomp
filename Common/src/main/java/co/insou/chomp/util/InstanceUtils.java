package co.insou.chomp.util;

import java.lang.reflect.Constructor;

public enum InstanceUtils {

	;

	public static <T> T createOrNull(Class<T> clazz)
	{
		try
		{
			Constructor<T> constructor = clazz.getDeclaredConstructor();

			return constructor.newInstance();
		}
		catch (ReflectiveOperationException exception)
		{
			return null;
		}
	}

}
