package co.insou.chomp;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import co.insou.chomp.bean.Beans;
import co.insou.chomp.health.HealthRequest;
import co.insou.chomp.health.HealthResponse;

public class TestClient {

	public static void main(String[] args)
	{
		ChompClient client = ChompClient.fromDefaults();

		System.out.println("=== RUNNING ===");

		HealthRequest request = Beans.create(HealthRequest.class);
		HealthResponse response = client.process(request, "/health");

		System.out.println("RESPONSE:");
		System.out.println(ReflectionToStringBuilder.reflectionToString(response));
	}

}
