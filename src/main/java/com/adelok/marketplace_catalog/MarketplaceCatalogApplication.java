package com.adelok.marketplace_catalog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"com.adelok.marketplace_catalog", "com.adelok.common"})
@SpringBootApplication
public class MarketplaceCatalogApplication {

	public static void main(String[] args) {
		SpringApplication.run(MarketplaceCatalogApplication.class, args);
	}

}
