package co.insou.chomp.util.except;

@FunctionalInterface
public interface CheckedConsumer<T> {

	void accept(T value) throws Throwable;

}