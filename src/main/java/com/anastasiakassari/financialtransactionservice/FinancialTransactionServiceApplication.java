package com.anastasiakassari.financialtransactionservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class FinancialTransactionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinancialTransactionServiceApplication.class, args);
    }


}
