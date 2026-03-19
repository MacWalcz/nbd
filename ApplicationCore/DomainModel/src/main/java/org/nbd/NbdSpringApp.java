package org.nbd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(scanBasePackages = "org.nbd")
public class NbdSpringApp {
    NbdSpringApp(){}
    public static void main(String[] args) {SpringApplication.run(NbdSpringApp.class, args);}
}
