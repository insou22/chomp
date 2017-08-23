package co.insou.chomp.time;

import java.util.function.Supplier;

import com.google.common.base.Stopwatch;

@FunctionalInterface
public interface Audit<T> {

    static Audit<?> of(Runnable runnable) {
        return new AuditRunnable(runnable);
    }

    static <T> Audit<T> of(Supplier<T> supplier) {
        return new AuditSupplier<>(supplier);
    }

    T printTime(String name);

    final class AuditRunnable implements Audit<Object> {

        private final Runnable runnable;

        private AuditRunnable(Runnable runnable) {
            this.runnable = runnable;
        }

        @Override
        public Object printTime(String name) {
            Stopwatch stopwatch = Stopwatch.createStarted();
            this.runnable.run();
            stopwatch.stop();
            System.out.println(name + " took " + stopwatch.toString());

            return null;
        }

    }

    final class AuditSupplier<T> implements Audit<T> {

        private final Supplier<T> supplier;

        private AuditSupplier(Supplier<T> supplier) {
            this.supplier = supplier;
        }

        @Override
        public T printTime(String name) {
            Stopwatch stopwatch = Stopwatch.createStarted();
            T value = this.supplier.get();
            stopwatch.stop();
            System.out.println(name + " took " + stopwatch.toString());

            return value;
        }

    }

}