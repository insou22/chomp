package co.insou.chomp;

import java.util.DoubleSummaryStatistics;
import java.util.Scanner;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.google.common.base.Stopwatch;

import co.insou.chomp.bean.Beans;
import co.insou.chomp.health.HealthRequest;
import co.insou.chomp.health.HealthResponse;
import co.insou.chomp.service.ServiceRequest;

public class TestClient {

	private static final DoubleSummaryStatistics statistics = new DoubleSummaryStatistics();

	public static void main(String[] args)
	{
		Scanner scanner = new Scanner(System.in);

		ChompClient client = ChompClient.fromDefaults();

		Stopwatch stopwatch = Stopwatch.createUnstarted();

		long nanos = 0;
		long times = 0;

		System.out.println("=== RUNNING ===");

		HealthRequest request = Beans.create(HealthRequest.class);
		HealthResponse response = client.process(ServiceRequest.emptyRequest(HealthRequest.class), "/health");

		System.out.println("RESPONSE:");
		System.out.println(ReflectionToStringBuilder.reflectionToString(response));

		if (true) return;/*

		while (true)
		{
			stopwatch.reset();

			HealthRequest request = Beans.create(HealthRequest.class);
			stopwatch.start();
			HealthResponse response = client.process(ServiceRequest.emptyRequest(HealthRequest.class), "/health");

			nanos += stopwatch.stop().elapsed(TimeUnit.NANOSECONDS);
			times++;

			System.out.print("\rAverage: " + (nanos / (times * 1000)) + "us ; Executions: " + times);
		}*/
	}

}
