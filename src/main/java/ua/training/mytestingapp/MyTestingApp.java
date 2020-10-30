package ua.training.mytestingapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
public class MyTestingApp {

    public static void main(String[] args) {
        SpringApplication.run(MyTestingApp.class, args);
    }
}
