package ru.practicum.shareit;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ShareItApp implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ShareItApp.class, args);
    }

    @Override
    public void run(String... args) {
        System.err.println("Hello World!");
    }
}
