package co.insou.chomp.service;

import co.insou.chomp.bean.Beans;

public interface ServiceRequest<RES extends ServiceResponse<? extends ServiceRequest<RES>>> extends ServiceCall {

	static <REQ extends ServiceRequest<?>> REQ emptyRequest(Class<REQ> requestType) {
		return Beans.create(requestType);
	}

}
