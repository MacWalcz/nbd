package org.nbd;

import fish.payara.micro.PayaraMicro;

public class Main {
    public static void main(String[] args) {
        try {
            PayaraMicro.getInstance()
                    .addDeployment("target/nbd.war")
                    .bootStrap();
            System.out.println("Payara Micro is running on http://localhost:8080/nbd");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


