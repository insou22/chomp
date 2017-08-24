package co.insou.chomp.service;

import java.time.Instant;

public interface ServiceCall {

	Instant getTimestamp();

	void setTimestamp(Instant timestamp);

	Class<? extends ServiceCall> getType();

	void setType(Class<? extends ServiceCall> type);

}
