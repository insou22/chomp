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

		double cpuLoad = this.os.getSystemCpuLoad();
		response.setCpuLoad(Double.isNaN(cpuLoad) ? -1D : cpuLoad);

		response.setTotalMemory(this.os.getTotalPhysicalMemorySize());
		response.setUsedMemory(this.os.getCommittedVirtualMemorySize());
		response.setLastUpdated(Instant.now());

		UselessBean useless = Beans.create(UselessBean.class);
		useless.setImGay("I'm motherfucking gay");
		useless.setInts(new int[]{6, 9, 4, 2 ,0});
		response.setUselessBean(useless);

		return response;
	}

	@Override
	public String endpoint()
	{
		return "/health";
	}

}
