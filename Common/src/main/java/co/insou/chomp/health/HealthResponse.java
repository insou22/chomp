package co.insou.chomp.health;

import co.insou.chomp.service.ServiceResponse;

public interface HealthResponse extends ServiceResponse<HealthRequest> {

	double getCpuLoad();

	void setCpuLoad(double cpuLoad);

	double getTotalMemory();

	void setTotalMemory(double totalMemory);

	double getUsedMemory();

	void setUsedMemory(double usedMemory);

	HelloBean getHelloBean();

	void setHelloBean(HelloBean bean);

}
