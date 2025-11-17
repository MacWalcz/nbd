package org.nbd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication(scanBasePackages = "org.nbd")
@EnableMongoRepositories(basePackages = "org.nbd.repositories")
public class NbdSpringApp {
    NbdSpringApp(){}
    public static void main(String[] args) {SpringApplication.run(NbdSpringApp.class, args);}
}
