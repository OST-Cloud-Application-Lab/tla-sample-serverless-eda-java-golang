package org.contextmapper.sample.tlas.infrastructure.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("org.contextmapper.sample.tlas")
public class TlaApplication {

    public static void main(String[] args) {
        SpringApplication.run(TlaApplication.class, args);
    }

}
