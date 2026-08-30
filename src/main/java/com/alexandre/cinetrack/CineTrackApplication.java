package com.alexandre.cinetrack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/*
 * Esta é a classe principal do CineTrack.
 * @SpringBootApplication ativa as configurações automáticas do Spring Boot.
 */
@EnableScheduling
@SpringBootApplication
public class CineTrackApplication {

    /*
     * Ponto de entrada da aplicação Java.
     */
    public static void main(String[] args) {
        SpringApplication.run(CineTrackApplication.class, args);
    }
}