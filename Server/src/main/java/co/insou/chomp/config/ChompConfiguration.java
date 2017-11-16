package co.insou.chomp.config;

import com.google.inject.ProvidedBy;

@ProvidedBy(ConfigurationProvider.class)
public interface ChompConfiguration {

	UndertowConfiguration getHttp();

	void setHttp(UndertowConfiguration http);

}
