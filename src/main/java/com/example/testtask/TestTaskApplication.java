package com.example.testtask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Главный класс приложения, запускающий Spring Boot приложение.
 */
@SpringBootApplication
public class TestTaskApplication {

    /**
     * Точка входа в приложение.
     *
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        SpringApplication.run(TestTaskApplication.class, args);
    }
}
