package com.espol.poo.proyecto;

import java.io.File;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Pantalla de configuración inicial del juego.
 * Permite seleccionar modo de juego, símbolo y quién inicia.
 */
public class PantallaConfiguracion {

    // Estilos CSS
    private static final String ESTILO_TITULO =
            "-fx-font-size: 40; -fx-font-weight: bold; -fx-text-fill: white;";
    private static final String ESTILO_SUBTITULO =
            "-fx-font-size: 14; -fx-text-fill: #BDC3C7;";
    private static final String ESTILO_LABEL =
            "-fx-text-fill: white; -fx-font-size: 16; -fx-font-weight: bold;";
    private static final String ESTILO_RADIO =
            "-fx-text-fill: white; -fx-font-size: 14;";
    private static final String ESTILO_BOTON =
            "-fx-background-color: #27AE60; -fx-text-fill: white; " +
            "-fx-font-size: 18; -fx-font-weight: bold; " +
            "-fx-padding: 12 50; -fx-cursor: hand; -fx-background-radius: 8;";
    private static final String ESTILO_BOTON_SEC =
            "-fx-background-color: #3498DB; -fx-text-fill: white; " +
            "-fx-font-size: 14; -fx-padding: 8 30; -fx-cursor: hand; " +
            "-fx-background-radius: 5;";

    /**
     * Crea y retorna la escena de configuración.
     */
    public Scene crearEscena(Stage stage) {

        // --- Título ---
        Label titulo = new Label("TRES EN RAYA");
        titulo.setStyle(ESTILO_TITULO);
        Label subtitulo = new Label("Proyecto Estructuras de Datos");
        subtitulo.setStyle(ESTILO_SUBTITULO);

        // --- Modo de juego ---
        Label lblModo = new Label("Modo de Juego:");
        lblModo.setStyle(ESTILO_LABEL);
        ToggleGroup grupoModo = new ToggleGroup();
        RadioButton rbHvC = new RadioButton("Humano vs Computadora");
        RadioButton rbHvH = new RadioButton("Humano vs Humano");
        RadioButton rbCvC = new RadioButton("Computadora vs Computadora");
        rbHvC.setToggleGroup(grupoModo);
        rbHvH.setToggleGroup(grupoModo);
        rbCvC.setToggleGroup(grupoModo);
        rbHvC.setSelected(true);
        rbHvC.setStyle(ESTILO_RADIO);
        rbHvH.setStyle(ESTILO_RADIO);
        rbCvC.setStyle(ESTILO_RADIO);
        VBox modoBox = new VBox(5, lblModo, rbHvC, rbHvH, rbCvC);

        // --- Selección de símbolo (solo para HvC) ---
        Label lblSimbolo = new Label("Tu símbolo:");
        lblSimbolo.setStyle(ESTILO_LABEL);
        ToggleGroup grupoSimbolo = new ToggleGroup();
        RadioButton rbX = new RadioButton("X");
        RadioButton rbO = new RadioButton("O");
        rbX.setToggleGroup(grupoSimbolo);
        rbO.setToggleGroup(grupoSimbolo);
        rbX.setSelected(true);
        rbX.setStyle(ESTILO_RADIO);
        rbO.setStyle(ESTILO_RADIO);
        HBox simboloHBox = new HBox(20, rbX, rbO);
        VBox simboloSection = new VBox(5, lblSimbolo, simboloHBox);

        // --- Quién inicia (solo para HvC) ---
        Label lblInicia = new Label("¿Quién inicia?");
        lblInicia.setStyle(ESTILO_LABEL);
        ToggleGroup grupoInicia = new ToggleGroup();
        RadioButton rbHumano = new RadioButton("Tú");
        RadioButton rbCompu = new RadioButton("Computadora");
        rbHumano.setToggleGroup(grupoInicia);
        rbCompu.setToggleGroup(grupoInicia);
        rbHumano.setSelected(true);
        rbHumano.setStyle(ESTILO_RADIO);
        rbCompu.setStyle(ESTILO_RADIO);
        HBox iniciaHBox = new HBox(20, rbHumano, rbCompu);
        VBox iniciaSection = new VBox(5, lblInicia, iniciaHBox);

        // Mostrar/ocultar secciones según el modo seleccionado
        grupoModo.selectedToggleProperty().addListener((obs, viejo, nuevo) -> {
            boolean esHvC = (nuevo == rbHvC);
            simboloSection.setVisible(esHvC);
            simboloSection.setManaged(esHvC);
            iniciaSection.setVisible(esHvC);
            iniciaSection.setManaged(esHvC);
        });

        // --- Botones ---
        Button btnJugar = new Button("JUGAR");
        btnJugar.setStyle(ESTILO_BOTON);
        btnJugar.setOnAction(e -> {
            // Determinar modo
            String modo;
            if (rbHvC.isSelected()) modo = "HvC";
            else if (rbHvH.isSelected()) modo = "HvH";
            else modo = "CvC";

            // Determinar configuración
            int simboloHumano = rbX.isSelected() ? 1 : 2;
            int simboloComp;
            int turnoInicial;

            if (modo.equals("HvC")) {
                simboloComp = (simboloHumano == 1) ? 2 : 1;
                turnoInicial = rbHumano.isSelected() ? simboloHumano : simboloComp;
            } else {
                simboloComp = 0;
                turnoInicial = 1; // X siempre inicia en HvH y CvC
            }

            // Crear y mostrar pantalla de juego
            PantallaJuego juego = new PantallaJuego(modo, simboloComp, turnoInicial);
            stage.setScene(juego.crearEscena(stage));
        });

        Button btnCargar = new Button("Cargar Partida");
        btnCargar.setStyle(ESTILO_BOTON_SEC);
        btnCargar.setOnAction(e -> cargarPartida(stage));

        // --- Separador visual ---
        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: #7F8C8D;");

        // --- Layout principal ---
        VBox root = new VBox(18,
                titulo, subtitulo,
                sep,
                modoBox,
                simboloSection,
                iniciaSection,
                btnJugar, btnCargar
        );
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #2C3E50;");

        return new Scene(root, 520, 580);
    }

    /**
     * Abre un diálogo para cargar una partida guardada.
     */
    private void cargarPartida(Stage stage) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Cargar Partida");
        fc.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Archivo de Partida", "*.txt"));
        File file = fc.showOpenDialog(stage);
        if (file != null) {
            Object[] datos = GestorPartida.cargar(file);
            if (datos != null) {
                String modo = (String) datos[0];
                int simboloComp = (int) datos[1];
                int turno = (int) datos[2];
                int[][] casillas = (int[][]) datos[3];

                Tablero tablero = new Tablero();
                tablero.setCasillas(casillas);

                PantallaJuego juego = new PantallaJuego(modo, simboloComp, turno, tablero);
                stage.setScene(juego.crearEscena(stage));
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("No se pudo cargar la partida.");
                alert.showAndWait();
            }
        }
    }
}
