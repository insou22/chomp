package co.insou.chomp.health;

import java.time.Instant;

import co.insou.chomp.service.ServiceResponse;

public interface HealthResponse extends ServiceResponse<HealthRequest> {

	double getCpuLoad();

	void setCpuLoad(double cpuLoad);

	double getTotalMemory();

	void setTotalMemory(double totalMemory);

	double getUsedMemory();

	void setUsedMemory(double usedMemory);

	Instant getLastUpdated();

	void setLastUpdated(Instant lastUpdated);

}
