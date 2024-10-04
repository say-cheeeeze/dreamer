package com.cheeeeze.bootjpa1.web.remnant.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping( "/remnant/*" )
public class RemnantController {
	
	@GetMapping( "regist" )
	public String registRemnant() {
		return "/remnant/regist";
	}
}
