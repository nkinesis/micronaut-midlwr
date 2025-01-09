package net.ptidej.tools4cities.middleware.core;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.context.ApplicationContextBuilder;
import io.micronaut.context.ApplicationContextConfigurer;
import io.micronaut.context.annotation.ContextConfigurer;
import io.micronaut.runtime.Micronaut;
import net.ptidej.tools4cities.middleware.datastores.InMemoryDataStore;

/**
*
* This is the Micronaut application entrypoint
* 
*/
public class Application {
	
	IDataStore store = InMemoryDataStore.getInstance();

    @ContextConfigurer
    public static class Configurer implements ApplicationContextConfigurer {
        @Override
        public void configure(@NonNull ApplicationContextBuilder builder) {
            builder.defaultEnvironments("dev");
        }
    }
    
    public static void main(String[] args) {
        Micronaut.run(Application.class, args);
    }
}