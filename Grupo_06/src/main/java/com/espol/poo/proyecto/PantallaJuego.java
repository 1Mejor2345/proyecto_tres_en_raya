package com.espol.poo.proyecto;

import java.io.File;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Pantalla principal del juego con el tablero 3x3.
 * Soporta tres modos: HvC (Humano vs Computadora),
 * HvH (Humano vs Humano), CvC (Computadora vs Computadora).
 */
public class PantallaJuego {

    // Estado del juego
    private Tablero tablero;
    private int turnoActual;          // 1=X, 2=O (quién juega ahora)
    private final int turnoInicial;   // Para reiniciar partida
    private boolean juegoTerminado;

    // Configuración
    private final String modo;        // "HvC", "HvH", "CvC"
    private final int simboloComputadora; // 1=X, 2=O (0 si no aplica)

    // Componentes de la UI
    private final Button[][] botones = new Button[3][3];
    private Label lblEstado;
    private Timeline timelineCvC;

    // Estilos CSS
    private static final String ESTILO_CELDA =
            "-fx-min-width: 120; -fx-min-height: 120; " +
            "-fx-max-width: 120; -fx-max-height: 120; " +
            "-fx-font-size: 48; -fx-font-weight: bold; " +
            "-fx-background-color: #ECF0F1; " +
            "-fx-border-color: #BDC3C7; -fx-border-width: 1; " +
            "-fx-background-radius: 8; -fx-border-radius: 8; -fx-cursor: hand;";

    private static final String ESTILO_CELDA_X = ESTILO_CELDA + " -fx-text-fill: #E74C3C;";
    private static final String ESTILO_CELDA_O = ESTILO_CELDA + " -fx-text-fill: #2980B9;";

    private static final String ESTILO_BOTON =
            "-fx-background-color: #3498DB; -fx-text-fill: white; " +
            "-fx-font-size: 14; -fx-padding: 8 20; -fx-cursor: hand; " +
            "-fx-background-radius: 5;";

    /**
     * Constructor para nueva partida.
     */
    public PantallaJuego(String modo, int simboloComputadora, int turnoInicial) {
        this.tablero = new Tablero();
        this.modo = modo;
        this.simboloComputadora = simboloComputadora;
        this.turnoActual = turnoInicial;
        this.turnoInicial = turnoInicial;
        this.juegoTerminado = false;
    }

    /**
     * Constructor para cargar partida guardada.
     */
    public PantallaJuego(String modo, int simboloComputadora, int turnoActual, Tablero tablero) {
        this.tablero = tablero;
        this.modo = modo;
        this.simboloComputadora = simboloComputadora;
        this.turnoActual = turnoActual;
        this.turnoInicial = turnoActual;
        this.juegoTerminado = false;
    }

    /**
     * Crea y retorna la escena del juego.
     */
    public Scene crearEscena(Stage stage) {
        // Etiqueta de estado (turno actual)
        lblEstado = new Label();
        lblEstado.setStyle("-fx-text-fill: white; -fx-font-size: 22; -fx-font-weight: bold;");
        actualizarEstado();

        // Tablero 3x3
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(6);
        grid.setVgap(6);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Button btn = new Button("");
                btn.setStyle(ESTILO_CELDA);
                final int fila = i, col = j;
                btn.setOnAction(e -> manejarClick(fila, col));
                botones[i][j] = btn;
                grid.add(btn, j, i);
            }
        }

        // Botones de acción
        Button btnGuardar = new Button("Guardar Partida");
        btnGuardar.setStyle(ESTILO_BOTON);
        btnGuardar.setOnAction(e -> guardarPartida(stage));

        Button btnNueva = new Button("Nueva Partida");
        btnNueva.setStyle(ESTILO_BOTON);
        btnNueva.setOnAction(e -> {
            if (timelineCvC != null) timelineCvC.stop();
            PantallaJuego nueva = new PantallaJuego(modo, simboloComputadora, turnoInicial);
            stage.setScene(nueva.crearEscena(stage));
        });

        Button btnVolver = new Button("Volver");
        btnVolver.setStyle(ESTILO_BOTON);
        btnVolver.setOnAction(e -> {
            if (timelineCvC != null) timelineCvC.stop();
            PantallaConfiguracion config = new PantallaConfiguracion();
            stage.setScene(config.crearEscena(stage));
        });

        HBox acciones = new HBox(10, btnGuardar, btnNueva, btnVolver);
        acciones.setAlignment(Pos.CENTER);

        // Layout principal
        VBox root = new VBox(25, lblEstado, grid, acciones);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #2C3E50;");

        // Actualizar botones con el estado del tablero (importante para partidas cargadas)
        actualizarTablero();

        Scene scene = new Scene(root, 520, 580);

        // Si es turno de la computadora en modo HvC, jugar automáticamente
        if (modo.equals("HvC") && turnoActual == simboloComputadora && !juegoTerminado) {
            Platform.runLater(this::jugadaComputadora);
        }

        // Si es modo CvC, iniciar juego automático
        if (modo.equals("CvC")) {
            iniciarCvC();
        }

        return scene;
    }

    /**
     * Maneja el clic del usuario en una casilla.
     */
    private void manejarClick(int fila, int col) {
        if (juegoTerminado) return;
        if (!tablero.estaLibre(fila, col)) return;

        // En modo CvC no se permiten clics
        if (modo.equals("CvC")) return;

        // En modo HvC, solo permitir clic cuando es turno del humano
        if (modo.equals("HvC") && turnoActual == simboloComputadora) return;

        // Colocar la ficha
        tablero.colocar(fila, col, turnoActual);
        actualizarTablero();

        if (verificarFinJuego()) return;

        // Cambiar turno
        turnoActual = (turnoActual == 1) ? 2 : 1;
        actualizarEstado();

        // Si es modo HvC y ahora toca la computadora
        if (modo.equals("HvC") && turnoActual == simboloComputadora) {
            Platform.runLater(this::jugadaComputadora);
        }
    }

    /**
     * La computadora realiza su jugada usando Minimax.
     */
    private void jugadaComputadora() {
        if (juegoTerminado) return;

        int[] jugada = Minimax.decidirJugada(tablero, turnoActual);
        if (jugada != null) {
            tablero.colocar(jugada[0], jugada[1], turnoActual);
            actualizarTablero();

            if (verificarFinJuego()) return;

            turnoActual = (turnoActual == 1) ? 2 : 1;
            actualizarEstado();
        }
    }

    /**
     * Actualiza los textos y estilos de los botones según el tablero.
     */
    private void actualizarTablero() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int val = tablero.getCasilla(i, j);
                if (val == 1) {
                    botones[i][j].setText("X");
                    botones[i][j].setStyle(ESTILO_CELDA_X);
                } else if (val == 2) {
                    botones[i][j].setText("O");
                    botones[i][j].setStyle(ESTILO_CELDA_O);
                } else {
                    botones[i][j].setText("");
                    botones[i][j].setStyle(ESTILO_CELDA);
                }
            }
        }
    }

    /**
     * Actualiza la etiqueta de estado con el turno actual.
     */
    private void actualizarEstado() {
        String simbolo = (turnoActual == 1) ? "X" : "O";
        String quien;
        switch (modo) {
            case "HvC":
                quien = (turnoActual == simboloComputadora) ? " (Computadora)" : " (Humano)";
                break;
            case "HvH":
                quien = " (Jugador " + ((turnoActual == 1) ? "1" : "2") + ")";
                break;
            default: // CvC
                quien = " (Computadora)";
                break;
        }
        lblEstado.setText("Turno de: " + simbolo + quien);
    }

    /**
     * Verifica si el juego ha terminado (victoria o empate).
     * @return true si el juego terminó
     */
    private boolean verificarFinJuego() {
        int ganador = tablero.verificarGanador();
        if (ganador != 0) {
            juegoTerminado = true;
            String simbolo = (ganador == 1) ? "X" : "O";
            lblEstado.setText("¡Ganador: " + simbolo + "!");
            if (timelineCvC != null) timelineCvC.stop();
            mostrarAlerta("Fin del Juego", "¡El jugador " + simbolo + " ha ganado!");
            return true;
        }
        if (tablero.hayEmpate()) {
            juegoTerminado = true;
            lblEstado.setText("¡Empate!");
            if (timelineCvC != null) timelineCvC.stop();
            mostrarAlerta("Fin del Juego", "¡La partida terminó en empate!");
            return true;
        }
        return false;
    }

    /**
     * Inicia el modo Computadora vs Computadora con animación por turnos.
     */
    private void iniciarCvC() {
        timelineCvC = new Timeline(new KeyFrame(Duration.millis(800), e -> {
            if (!juegoTerminado) {
                jugadaComputadora();
            } else {
                timelineCvC.stop();
            }
        }));
        timelineCvC.setCycleCount(Timeline.INDEFINITE);
        timelineCvC.play();
    }

    /**
     * Guarda la partida actual en un archivo.
     */
    private void guardarPartida(Stage stage) {
        if (juegoTerminado) {
            mostrarAlerta("Aviso", "No se puede guardar una partida terminada.");
            return;
        }
        FileChooser fc = new FileChooser();
        fc.setTitle("Guardar Partida");
        fc.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Archivo de Partida", "*.txt"));
        fc.setInitialFileName("partida.txt");
        File file = fc.showSaveDialog(stage);
        if (file != null) {
            GestorPartida.guardar(file, modo, simboloComputadora, turnoActual,
                    tablero.getCasillas());
            mostrarAlerta("Éxito", "Partida guardada correctamente.");
        }
    }

    /**
     * Muestra un diálogo de información.
     */
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
