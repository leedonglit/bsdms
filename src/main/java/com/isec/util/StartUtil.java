package com.isec.util;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StartUtil implements CommandLineRunner{
	
	@Override
	public void run(String... args) throws Exception {
		System.out.println("----------------服务启动完成------------------------");
	}

}
