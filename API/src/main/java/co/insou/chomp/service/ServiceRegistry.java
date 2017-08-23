package co.insou.chomp.service;

public interface ServiceRegistry {

	<REQ extends ServiceRequest<RES>, RES extends ServiceResponse<REQ>>
		ServiceController<REQ, RES> locateController(String endpoint);

	void register(ServiceController<?, ?> controller);

}
