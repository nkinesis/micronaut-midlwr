package net.ptidej.tools4cities.middleware.controllers;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import io.micronaut.context.ApplicationContext;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.web.router.Router;
import io.micronaut.web.router.UriRouteInfo;
import jakarta.annotation.security.PermitAll;

@Controller("/discover") 
@Secured(SecurityRule.IS_ANONYMOUS)
public class DiscoveryController {
	
	private String getRouteMetadata(String prefix) {
		 JsonArray routes = new JsonArray();
		 Set<JsonObject> resultSet = new HashSet<>();
    	 try (ApplicationContext context = ApplicationContext.run()) {
             Router router = context.getBean(Router.class);
             @NonNull Stream<UriRouteInfo<?, ?>> routeStream = router.uriRoutes();
             routeStream.forEach(route -> {
            	 JsonObject routeObj = new JsonObject();
            	 String routeMethod = route.getHttpMethodName();
            	 String routeName = route.getUriMatchTemplate().toString();
            	 String producedType = route.getProduces().toString();
            	 Boolean isGETorPOST = routeMethod.contains("GET") || routeMethod.contains("POST");
            	
            	 if (isGETorPOST && routeName.startsWith(prefix) && producedType.contains("application/json")) {
            		 System.out.println(route);
            		 routeObj.addProperty("name", routeName);
            		 routeObj.addProperty("method", routeMethod);
                	 routeObj.addProperty("description", "This route produces " + producedType);
                	 resultSet.add(routeObj);
            	 }
           	 
             });
             
             for (JsonObject xyz : resultSet) {
            	 routes.add(xyz);
             }
             
            return routes.toString();
         }
	}
	
    @Get("/")
    @PermitAll 
    public String index() {
    	return getRouteMetadata("");
    }
	
    @Get("/consumers")
    @PermitAll 
    public String consumers() {
    	return getRouteMetadata("/consumers");
    }
    
}


