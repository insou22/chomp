package co.insou.chomp.bean;

import java.util.IdentityHashMap;
import java.util.Map;

import co.insou.chomp.util.InstanceUtils;

public enum Beans {

	;

	private static final Map<Class<?>, Class<?>> BINDINGS = new IdentityHashMap<>();

	public static <T> Class<? extends T> build(Class<T> clazz)
	{
		if (Bean.class.isAssignableFrom(clazz))
		{
			return clazz;
		}

		if (clazz.isInterface())
		{
			@SuppressWarnings("unchecked")
			Class<? extends T> implementation = (Class<? extends T>) Beans.BINDINGS.computeIfAbsent(clazz, Beans::bindToImplementation);

			return implementation;
		}

		throw new BeanCreationException("Class does not extend bean or is not interface");
	}

	public static <T> T create(Class<T> clazz)
	{
		if (clazz.isAnnotationPresent(DynamicBean.class))
		{
			return InstanceUtils.createOrNull(clazz);
		}

		Class<? extends T> implementation = Beans.build(clazz);

		return InstanceUtils.createOrNull(implementation);
	}

	public static <T> Class<? extends T> attemptToBuild(Class<T> type)
	{
		try
		{
			return Beans.build(type);
		}
		catch (BeanCreationException exception)
		{
			return null;
		}
	}

	private static <T> Class<? extends T> bindToImplementation(Class<T> binding)
	{
		BeanBuilder<T> builder = new BeanBuilder<>(binding);

		return builder.build();
	}

	public static final class BeanCreationException extends RuntimeException {

		private BeanCreationException(String message)
		{
			super(message);
		}
	}

}