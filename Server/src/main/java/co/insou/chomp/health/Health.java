package co.insou.chomp.health;

import com.google.inject.Inject;

import co.insou.chomp.service.ChompService;
import co.insou.chomp.service.ServiceRegistry;

@ChompService
public final class Health {

	@Inject
	private Health(ServiceRegistry registry, HealthController controller)
	{
		registry.register(controller);
	}

}
