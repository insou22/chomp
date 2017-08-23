package co.insou.chomp.service;

import java.util.Map;

public class ControllerRegistryNotFoundException extends RuntimeException {

	public ControllerRegistryNotFoundException(Map<String, ServiceController<?, ?>> controllers, String endpoint)
	{
		super("Service controller registry for endpoint " + endpoint + " was not found! " +
				"Current controllers: " + controllers.toString());
	}

}
