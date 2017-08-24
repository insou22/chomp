package co.insou.chomp.preload;

import java.util.HashSet;
import java.util.Set;

import com.google.inject.Inject;
import com.google.inject.Injector;

import co.insou.chomp.bean.Beans;
import co.insou.chomp.service.ChompService;
import co.insou.chomp.service.ServiceRequest;
import co.insou.chomp.service.ServiceResponse;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;

public final class Preloader {

	private final Injector injector;
	private final Set<Class<?>> preloadedServices = new HashSet<>();
	private final Set<Class<?>> preloadedBeans = new HashSet<>();

	@Inject
	private Preloader(Injector injector)
	{
		this.injector = injector;
	}

	public void preload()
	{
		System.out.println("Preloading Chomp Services...");

		FastClasspathScanner scanner = new FastClasspathScanner();

		scanner.matchClassesWithAnnotation(ChompService.class, this::loadService)
				.scan();

		scanner.matchSubinterfacesOf(ServiceRequest.class, this::preloadBean)
				.scan();

		scanner.matchSubinterfacesOf(ServiceResponse.class, this::preloadBean)
				.scan();

		System.out.println("Finished preloading");
	}

	private void loadService(Class<?> service)
	{
		if (this.preloadedServices.add(service))
		{
			System.out.println("Preloading Service " + service);
			this.injector.getInstance(service);
		}
	}

	private void preloadBean(Class<?> beanClass)
	{
		if (this.preloadedBeans.add(beanClass))
		{
			System.out.println("Preloading Bean " + beanClass);
			Beans.create(beanClass);
		}
	}

}
