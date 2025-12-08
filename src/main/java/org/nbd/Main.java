package org.nbd;

import fish.payara.micro.PayaraMicro;

public class Main {
    public static void main(String[] args) {
        try {
            // Деплой WAR
            PayaraMicro.getInstance()
                    .addDeployment("target/nbd.war") // путь к сгенерированному WAR
                    .bootStrap();
            System.out.println("Payara Micro запущен на http://localhost:8080/nbd");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


