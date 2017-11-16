package co.insou.chomp.data;

import java.util.function.Function;

@FunctionalInterface
public interface DataProvider extends Function<String, DataStore> {

}
