package br.com.ilia.digital.folhadeponto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"br.com.ilia.digital"})
public class FolhaDePontoApplication {

	public static void main(String[] args) {
		SpringApplication.run(FolhaDePontoApplication.class, args);
	}
}
