package co.insou.chomp.util.except;

public enum ThrowableUtils {

	;

	public static RuntimeException getPropogated(Throwable throwable)
	{
		return (throwable instanceof RuntimeException) ? (RuntimeException) throwable : new RuntimeException(throwable);
	}

}
