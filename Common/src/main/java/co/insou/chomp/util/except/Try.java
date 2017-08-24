package co.insou.chomp.util.except;

import java.util.concurrent.Future;

public enum Try {

	;

	public static void to(CheckedRunnable runnable)
	{
		try
		{
			runnable.run();
		}
		catch (Throwable throwable)
		{
			throw ThrowableUtils.getPropogated(throwable);
		}
	}

	public static <T> T to(Future<T> future)
	{
		try
		{
			return future.get();
		}
		catch (Throwable throwable)
		{
			throw ThrowableUtils.getPropogated(throwable);
		}
	}

	public static <T> T to(CheckedSupplier<T> supplier)
	{
		try
		{
			return supplier.get();
		}
		catch (Throwable throwable)
		{
			throw ThrowableUtils.getPropogated(throwable);
		}
	}

	public static <T, R> R to(CheckedFunction<T, R> function, CheckedSupplier<T> supplier)
	{
		try
		{
			return function.apply(supplier.get());
		}
		catch (Throwable throwable)
		{
			throw ThrowableUtils.getPropogated(throwable);
		}
	}

	public static <T extends AutoCloseable, R> R toWithResources(CheckedSupplier<T> resources,
	                                                             CheckedFunction<T, R> tryTo)
	{
		T resource = Try.to(resources);
		try
		{
			return tryTo.apply(resource);
		}
		catch (Throwable throwable)
		{
			throw ThrowableUtils.getPropogated(throwable);
		}
		finally
		{
			Try.to(resource::close);
		}
	}

}