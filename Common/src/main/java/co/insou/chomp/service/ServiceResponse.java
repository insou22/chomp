package co.insou.chomp.service;

public interface ServiceResponse<REQ extends ServiceRequest<? extends ServiceResponse<REQ>>> extends ServiceCall {

}
