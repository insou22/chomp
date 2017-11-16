package co.insou.chomp;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import com.google.common.io.CharStreams;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import co.insou.chomp.bean.Beans;
import co.insou.chomp.bean.DynamicBean;
import co.insou.chomp.gson.GsonProvider;
import co.insou.chomp.health.HealthRequest;
import co.insou.chomp.health.HealthResponse;
import co.insou.chomp.service.ServiceCall;
import co.insou.chomp.service.ServiceRequest;
import co.insou.chomp.service.ServiceResponse;
import co.insou.chomp.util.except.CheckedSupplier;
import co.insou.chomp.util.except.Try;
import io.undertow.util.Methods;

public final class ChompClient {

	private static final Gson GSON = GsonProvider.getGson();

	public static ChompClient fromDefaults()
	{
		return new ChompClient("127.0.0.1", 8080);
	}

	public static ChompClient fromPort(int port)
	{
		return new ChompClient("127.0.0.1", port);
	}

	public static ChompClient fromAddress(String address)
	{
		return new ChompClient(address, 8080);
	}

	public static ChompClient fromAddressAndPort(String address, int port)
	{
		return new ChompClient(address, port);
	}

	private final String address;
	private final int port;

	private HealthResponse cachedHealth;

	private ChompClient(String address, int port)
	{
		this.address = address;
		this.port = port;
		this.checkStatus();
	}

	public HealthResponse checkStatus()
	{
		if (this.cachedHealth == null || this.cachedHealth.getLastUpdated().until(Instant.now(), ChronoUnit.SECONDS) > 10)
		{
			this.updateStatus();
		}

		return this.cachedHealth;
	}

	public HealthResponse updateStatus()
	{
		HealthRequest request = Beans.create(HealthRequest.class);

		this.cachedHealth = this.process(request, "/health");

		return this.cachedHealth;
	}

	@SuppressWarnings("unchecked")
	public <REQ extends ServiceRequest<RES>, RES extends ServiceResponse<REQ>> RES process(REQ request, String endpoint)
	{
		if (!endpoint.startsWith("/"))
		{
			throw new RuntimeException("Endpoint (" + endpoint + ") must begin with /");
		}

		String body = this.buildJsonFromRequest(request);
		HttpURLConnection connection = this.connectToEndpoint(endpoint, body.getBytes().length);

		this.writeRequest(connection, body);
		JsonObject json = this.readResponse(connection);

		Class<RES> responseType = this.getResponseType(json);

		return ChompClient.GSON.fromJson(json, responseType);
	}

	private void writeRequest(HttpURLConnection connection, String body)
	{
		DataOutputStream out = new DataOutputStream(Try.to(connection::getOutputStream));
		Try.to(() ->
		{
			out.writeBytes(body);
			out.close();
		});
	}

	private JsonObject readResponse(HttpURLConnection connection)
	{
		InputStream in = Try.to(connection::getInputStream);
		String response = Try.to(() -> CharStreams.toString(new InputStreamReader(in, StandardCharsets.UTF_8)));

		return ChompClient.GSON.fromJson(response, JsonElement.class).getAsJsonObject();
	}

	@SuppressWarnings("unchecked")
	private <T> Class<T> getResponseType(JsonObject json)
	{
		return (Class<T>) Beans.build(Try.to(() -> Class.forName(json.get("type").getAsString().replace("\"", ""))));
	}

	@SuppressWarnings("unchecked")
	private String buildJsonFromRequest(ServiceRequest<?> request)
	{
		Class<?> requestClass = request.getClass().getAnnotation(DynamicBean.class).value();

		request.setType((Class<? extends ServiceCall>) requestClass);
		request.setTimestamp(Instant.now());

		return ChompClient.GSON.toJson(request);
	}

	private URL getUrl(String endpoint)
	{
		return Try.to(() -> new URL(this.getUrlFormat(endpoint)));
	}

	private String getUrlFormat(String endpoint)
	{
		return "http://" + this.address + ":" + this.port + endpoint;
	}

	private HttpURLConnection connectToEndpoint(String endpoint, int bodyLength)
	{
		URL url = this.getUrl(endpoint);

		HttpURLConnection connection = (HttpURLConnection) Try.to((CheckedSupplier<URLConnection>) url::openConnection);
		this.setConnectionOptions(connection, bodyLength);

		return connection;
	}

	private void setConnectionOptions(HttpURLConnection connection, int bodyLength)
	{
		Try.to(() -> connection.setRequestMethod(Methods.POST_STRING));
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setRequestProperty("Content-Length", String.valueOf(bodyLength));

		connection.setUseCaches(false);
		connection.setDoOutput(true);
	}

}
