package com.acme.qa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.awt.Desktop;
import java.net.URI;

@SpringBootApplication
public class AiQaApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiQaApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void openBrowser() {
        try {
            if (Desktop.isDesktopSupported()
                    && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {

                Desktop.getDesktop().browse(
                        new URI("http://localhost:8080")
                );

            } else {
                System.out.println("No fue posible abrir el navegador automáticamente.");
                System.out.println("Abre manualmente: http://localhost:8080");
            }

        } catch (Exception e) {
            System.out.println("No fue posible abrir el navegador automáticamente.");
            System.out.println("Abre manualmente: http://localhost:8080");
        }
    }
}