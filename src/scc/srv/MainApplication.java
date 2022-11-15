package scc.srv;

import java.util.HashSet;
import java.util.Set;

import jakarta.ws.rs.core.Application;
import utils.GenericExceptionMapper;

public class MainApplication extends Application
{	

	// URL -> https://scc23app-westeurope-58569.azurewebsites.net/
	
	private Set<Object> singletons = new HashSet<Object>();
	private Set<Class<?>> resources = new HashSet<Class<?>>();

	public MainApplication() {
		resources.add(ControlResource.class);

		resources.add(MediaResource.class);
		resources.add(UserResource.class);
		resources.add(AuctionResource.class);
		resources.add(GenericExceptionMapper.class);

		singletons.add(new MediaResource());
		//singletons.add(new UserResource());
		singletons.add(new AuctionResource());
	}

	@Override
	public Set<Class<?>> getClasses() {
		return resources;
	}

	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}
}
