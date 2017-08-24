package co.insou.chomp.service;

public interface ServiceController<REQ extends ServiceRequest<RES>, RES extends ServiceResponse<REQ>> {

	RES process(REQ request);

	String endpoint();

}
