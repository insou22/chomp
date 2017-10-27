package co.insou.chomp;

import java.util.DoubleSummaryStatistics;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

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

		while (true)
		{
			stopwatch.reset();

			HealthRequest request = Beans.create(HealthRequest.class);
			stopwatch.start();
			HealthResponse response = client.process(ServiceRequest.emptyRequest(HealthRequest.class), "/health");

			nanos += stopwatch.stop().elapsed(TimeUnit.NANOSECONDS);
			times++;

			System.out.print("\rAverage: " + (nanos / (times * 1000)) + "us ; Executions: " + times);
		}
	}

}
