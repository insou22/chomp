package co.insou.chomp;

import java.util.Scanner;

import com.google.common.base.Stopwatch;

import co.insou.chomp.bean.Beans;
import co.insou.chomp.health.HealthRequest;
import co.insou.chomp.health.HealthResponse;

public class TestClient {

	public static void main(String[] args)
	{
		Scanner scanner = new Scanner(System.in);

		ChompClient client = ChompClient.fromDefaults();

		Stopwatch stopwatch = Stopwatch.createUnstarted();

		while (true)
		{
			stopwatch.reset();
			scanner.next();

			HealthRequest request = Beans.create(HealthRequest.class);
			stopwatch.start();
			HealthResponse response = client.process(request, "/health");
			System.out.println("Took " + stopwatch.stop() + " seconds to process.");

			System.out.println("Response: " + response);
		}
	}

}
