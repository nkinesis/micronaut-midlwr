package com.ptidej.tools4citites.middleware.controllers;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ptidej.tools4cities.middleware.middleware.IProducer;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

@Controller("/consumers") 
public class ConsumersController {
	
	private static String getBaseDir() throws URISyntaxException {
        // Get the directory of the current class
        File file = new File(ConsumersController.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        return file.isDirectory() ? file.getPath() : file.getParent();
    }
	
	private static List<Class<?>> findClasses(String baseDir, String packageName, String suffix) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        File directory = new File(baseDir);

        if (!directory.exists() || !directory.isDirectory()) {
            return classes;
        }

        for (File file : directory.listFiles()) {
            if (file.isDirectory()) {
                // Recursively search subdirectories
                classes.addAll(findClasses(file.getAbsolutePath(), packageName + file.getName() + ".", suffix));
            } else if (file.getName().endsWith(".class")) {
                // Extract class name and load it
                String className = packageName + file.getName().replace(".class", "");
                Class<?> clazz = Class.forName(className);

                // Check if the class name ends with the desired suffix
                if (clazz.getSimpleName().endsWith(suffix)) {
                    classes.add(clazz);
                }
            }
        }
        return classes;
    }
	
	private JsonArray getClassMetadata(String clazzName) {
		JsonArray outputArray = new JsonArray();
		JsonObject metadataObj = new JsonObject();
		try {
			// Step 1: Load the class by its fully qualified name (FQN)
	        Class<?> clazz = Class.forName(clazzName);
			
	        // Step 2: Create an instance of the class (assuming a no-argument constructor)
	        Set<IProducer<JsonObject>> setOfProducers = new HashSet<IProducer<JsonObject>>();
	        Object instance = clazz.getDeclaredConstructor(Set.class).newInstance(setOfProducers);
	
	        // Step 3: Access the public field and print its value
	        Field descriptionField = clazz.getField("description"); 
	        Class<?> resultField = clazz.getMethod("getResults").getReturnType();
	        
	        metadataObj.addProperty("description", (String) descriptionField.get(instance));
	        metadataObj.addProperty("route", "/consumers/" + clazz.getSimpleName());
	        metadataObj.addProperty("returnType", (String) resultField.toString());

		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchFieldException | SecurityException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException e ) {
			e.printStackTrace();
		} 
		outputArray.add(metadataObj);
		return outputArray;
	}
   
    @Get("/")
    public String index() {
    	
    	String baseDir = "";
    	JsonArray outputArray = new JsonArray();
    	ArrayList<String> outputClazzes = new ArrayList<String>();
		try {
			baseDir = getBaseDir();
            List<Class<?>> consumerClasses = findClasses(baseDir, "", "Consumer");
            consumerClasses.forEach(clazz -> {
            	if (!clazz.getName().contains("IConsumer") && !clazz.getName().contains("AbstractConsumer")) {
            		outputClazzes.add(clazz.getName());            		
            	}
            });
            
        } catch (ClassNotFoundException | URISyntaxException e) {
            return e.getMessage();
        }
		
		for (int i = 0; i < outputClazzes.size(); i++) {
			JsonObject clazzObj = new JsonObject();
			String clazzName = outputClazzes.get(i);
			clazzObj.addProperty("name", clazzName);
			clazzObj.add("metadata", getClassMetadata(clazzName));
	        outputArray.add(clazzObj);
		}
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(outputArray);
    }

}




