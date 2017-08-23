package co.insou.chomp.health;

import java.lang.management.ManagementFactory;
import java.time.Instant;

import com.sun.management.OperatingSystemMXBean;

import co.insou.chomp.bean.Beans;
import co.insou.chomp.service.ServiceController;

public class HealthController implements ServiceController<HealthRequest, HealthResponse> {

	private final OperatingSystemMXBean os = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);

	@Override
	public HealthResponse process(HealthRequest request)
	{
		HealthResponse response = Beans.create(HealthResponse.class);

		response.setCpuLoad(this.os.getSystemCpuLoad());
		response.setTotalMemory(this.os.getTotalPhysicalMemorySize());
		response.setUsedMemory(this.os.getCommittedVirtualMemorySize());
		response.setLastUpdated(Instant.now());

		return response;
	}

	@Override
	public String endpoint()
	{
		return "/health";
	}

}
