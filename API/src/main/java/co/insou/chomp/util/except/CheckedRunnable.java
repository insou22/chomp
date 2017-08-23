package co.insou.chomp.util.except;

@FunctionalInterface
public interface CheckedRunnable {

	void run() throws Throwable;

}
