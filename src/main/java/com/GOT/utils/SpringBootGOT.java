package com.GOT.utils;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class SpringBootGOT {

	@RequestMapping(value="/", produces=MediaType.TEXT_HTML_VALUE)
	public String goHome(){
		return "Start game <a href='/init'>here</a>";
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SpringApplication.run(SpringBootGOT.class, args);
	}

}
