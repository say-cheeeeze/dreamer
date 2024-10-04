package com.cheeeeze.bootjpa1.web.remnant.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping( "/api/remnant/*")
public class RemnantRestController {
	
	@GetMapping( "/test")
	public void test() {
		System.out.println( "Rest Controller Test" );
	}
}
