package com.ptidej.tools4cities.middleware.controllers;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

@Controller("/helloWorld") 
@Secured(SecurityRule.IS_ANONYMOUS)
public class HelloWorldController {
	
    @Get("/")
    public String index() {
        return "Hello requester :)";
    }
	
    @Get("/{name}")
    public String name(@PathVariable String name) {
        return "Hello " + name;
    }
    
}