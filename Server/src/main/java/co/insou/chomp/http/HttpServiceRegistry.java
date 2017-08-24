package co.insou.chomp.http;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import co.insou.chomp.service.ControllerRegistryNotFoundException;
import co.insou.chomp.service.ServiceController;
import co.insou.chomp.service.ServiceRegistry;
import co.insou.chomp.service.ServiceRequest;
import co.insou.chomp.service.ServiceResponse;

public final class HttpServiceRegistry implements ServiceRegistry {

	static HttpServiceRegistry create()
	{
		return new HttpServiceRegistry();
	}

	private final Map<String, ServiceController<?, ?>> controllers = new HashMap<>();

	private HttpServiceRegistry()
	{

	}

	@Override
	@SuppressWarnings("unchecked")
	public <REQ extends ServiceRequest<RES>, RES extends ServiceResponse<REQ>>
	ServiceController<REQ, RES> locateController(String endpoint)
	{
		return (ServiceController<REQ, RES>)
				Optional.ofNullable(this.controllers.get(endpoint.toLowerCase()))
						.orElseThrow(() -> new ControllerRegistryNotFoundException(this.controllers, endpoint.toLowerCase()));
	}

	@Override
	public void register(ServiceController<?, ?> controller)
	{
		if (!controller.endpoint().startsWith("/"))
		{
			throw new RuntimeException("Controller with endpoint " + controller.endpoint() + " must start with /");
		}

		this.controllers.put(controller.endpoint().toLowerCase(), controller);
	}


}
