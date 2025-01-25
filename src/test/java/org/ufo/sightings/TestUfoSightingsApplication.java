package org.ufo.sightings;

import org.springframework.boot.SpringApplication;

public class TestUfoSightingsApplication {

	public static void main(String[] args) {
		SpringApplication.from(UfoSightingsApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
