package co.insou.chomp.health;

import java.lang.management.ManagementFactory;

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

		HelloBean bean = Beans.create(HelloBean.class);
		bean.setHello("Hello, World!");

		response.setHelloBean(bean);

		return response;
	}

	@Override
	public String endpoint()
	{
		return "/health";
	}

}
