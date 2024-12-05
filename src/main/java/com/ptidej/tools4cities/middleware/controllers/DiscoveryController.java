package com.ptidej.tools4cities.middleware.controllers;

import java.util.stream.Stream;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import io.micronaut.context.ApplicationContext;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.web.router.Router;
import io.micronaut.web.router.UriRouteInfo;

@Controller("/discover") 
public class DiscoveryController {
	
	private String getRouteMetadata(String prefix) {
		 JsonArray routes = new JsonArray();
    	 try (ApplicationContext context = ApplicationContext.run()) {
             Router router = context.getBean(Router.class);
             @NonNull Stream<UriRouteInfo<?, ?>> routeStream = router.uriRoutes();
             routeStream.forEach(route -> {
            	 JsonObject routeObj = new JsonObject();
            	 String routeMethod = route.getHttpMethodName();
            	 String routeName = route.getUriMatchTemplate().toString();
            	 Boolean isGETorPOST = routeMethod.contains("GET") || routeMethod.contains("POST");
            	
            	 if (isGETorPOST && routeName.startsWith(prefix)) {
            		 System.out.println(routeName);
            		 routeObj.addProperty("name", routeName);
            		 routeObj.addProperty("method", routeMethod);
                	 routeObj.addProperty("description", "A very very nice route");
                	 routes.add(routeObj);
            	 }
           	 
             });
             
            return routes.toString();
         }
	}
	
    @Get("/consumers")
    public String consumers() {
    	return getRouteMetadata("/consumers");
    }
    
    @Get("/producers")
    public String producers() {
    	return getRouteMetadata("/producers");
    }
}


