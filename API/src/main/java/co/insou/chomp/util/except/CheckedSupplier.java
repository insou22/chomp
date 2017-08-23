package co.insou.chomp.util.except;

@FunctionalInterface
public interface CheckedSupplier<T> {

	T get() throws Throwable;

}