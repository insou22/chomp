package co.insou.chomp.http;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

import com.google.common.io.CharStreams;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.inject.Inject;

import co.insou.chomp.bean.Beans;
import co.insou.chomp.bean.DynamicBean;
import co.insou.chomp.service.ServiceCall;
import co.insou.chomp.service.ServiceController;
import co.insou.chomp.service.ServiceRegistry;
import co.insou.chomp.service.ServiceRequest;
import co.insou.chomp.service.ServiceResponse;
import co.insou.chomp.util.except.Try;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

public class HttpRequestHandler implements HttpHandler {

	private final ServiceRegistry registry;
	private final Gson gson;

	@Inject
	public HttpRequestHandler(ServiceRegistry registry, Gson gson)
	{
		this.registry = registry;
		this.gson = gson;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void handleRequest(HttpServerExchange exchange) throws Exception
	{
		ServiceController controller = this.registry.locateController(exchange.getRequestPath());

		ServiceRequest request = this.formatRequest(exchange);

		ServiceResponse response = controller.process(request);

		response.setTimestamp(Instant.now());
		response.setType((Class<? extends ServiceCall>) response.getClass().getAnnotation(DynamicBean.class).value());

		exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
		exchange.getResponseSender().send(this.gson.toJson(response));
	}

	@SuppressWarnings("unchecked")
	private ServiceRequest formatRequest(HttpServerExchange exchange) throws Exception
	{
		String body = CharStreams.toString(new InputStreamReader(exchange.getInputStream(), StandardCharsets.UTF_8));

		JsonObject json = this.gson.fromJson(body, JsonElement.class).getAsJsonObject();

		return this.gson.fromJson(
				json,
				Beans.create(Try.to(() ->
						(Class<ServiceRequest>) Class.forName(json.get("type").getAsString()))
				).getClass()
		);
	}

}
