package com.themis.blockinfo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class BlockinfoApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlockinfoApplication.class, args);
	}
}
