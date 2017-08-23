package co.insou.chomp.service;

public interface ServiceRequest<RES extends ServiceResponse<? extends ServiceRequest<RES>>> extends ServiceCall {

}
