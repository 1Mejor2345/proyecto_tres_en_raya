package com.espol.poo.proyecto;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Clase principal de la aplicación Tres en Raya.
 * Proyecto de Estructuras de Datos — Segunda Evaluación PAO 1 2026.
 */
public class Proyecto extends Application {

    @Override
    public void start(Stage stage) {
        PantallaConfiguracion config = new PantallaConfiguracion();
        stage.setScene(config.crearEscena(stage));
        stage.setTitle("Tres en Raya — Proyecto EDD");
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
