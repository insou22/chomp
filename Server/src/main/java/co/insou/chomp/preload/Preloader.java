package co.insou.chomp.preload;

import java.util.HashSet;
import java.util.Set;

import com.google.inject.Inject;
import com.google.inject.Injector;

import co.insou.chomp.Chomp;
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
		Chomp.print("Preloading Chomp Services...");

		new FastClasspathScanner()
				.matchClassesWithAnnotation(ChompService.class, this::loadService)
				.matchSubinterfacesOf(ServiceRequest.class, this::preloadBean)
				.matchSubinterfacesOf(ServiceResponse.class, this::preloadBean)
				.scan();

		Chomp.print("Finished preloading");
	}

	private void loadService(Class<?> service)
	{
		if (this.preloadedServices.add(service))
		{
			Chomp.print("Preloading Service " + service);
			this.injector.getInstance(service);
		}
	}

	private void preloadBean(Class<?> beanClass)
	{
		if (this.preloadedBeans.add(beanClass))
		{
			Chomp.print("Preloading Bean " + beanClass);
			Beans.create(beanClass);
		}
	}

}
