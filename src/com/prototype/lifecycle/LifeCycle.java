package com.prototype.lifecycle;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.prototype.service.Service;


public class LifeCycle implements Runnable, Closeable, AutoCloseable {

	private boolean running;
	private List<Service> services;

	public LifeCycle() {

		services = new ArrayList<>();
	}

	@Override
	public void run() {

		running = true;

		while(running) {

			//
		}
	}

	@Override
	public void close() throws IOException {

		running = false;

		for(Service service : services) {

			service.close();
		}
	}

	public void registerService(Service service) {

		if(!services.contains(service)) {

			service.setUuid(createUUID());
			services.add(service);
		}
	}

	private UUID createUUID() {

		UUID uuid = UUID.randomUUID();
		boolean unique = true;

		for(Service service : services) {

			if(uuid.compareTo(service.getUuid()) == 0) {

				unique = false;
			}
		}

		if(!unique) {

			uuid = createUUID();
		}

		return uuid;
	}

	public List<Service> getServices() {

		return services;
	}

	public void setServices(List<Service> services) {

		this.services = services;
	}
}
