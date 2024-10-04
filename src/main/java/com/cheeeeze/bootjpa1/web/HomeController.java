package com.cheeeeze.bootjpa1.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
	
	@GetMapping( "/" )
	public String home() {
		System.out.println( "#########" );
//		return "/static/index";
		return "index";
	}
}
