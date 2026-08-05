package com.espol.poo.proyecto;

import java.util.LinkedList;

/**
 * Modelo del tablero de Tres en Raya (3x3).
 * Valores: 0 = vacío, 1 = X, 2 = O
 */
public class Tablero {

    private int[][] casillas;

    public Tablero() {
        this.casillas = new int[3][3];
    }

    // Coloca una ficha en la posición indicada
    public void colocar(int fila, int col, int jugador) {
        casillas[fila][col] = jugador;
    }

    // Verifica si una casilla está libre
    public boolean estaLibre(int fila, int col) {
        return casillas[fila][col] == 0;
    }

    // Obtiene el valor de una casilla
    public int getCasilla(int fila, int col) {
        return casillas[fila][col];
    }

    // Obtiene la matriz completa (para guardar partida)
    public int[][] getCasillas() {
        return casillas;
    }

    // Establece la matriz completa (para cargar partida)
    public void setCasillas(int[][] casillas) {
        this.casillas = casillas;
    }

    /**
     * Verifica si hay un ganador.
     * @return 0 = nadie, 1 = gana X, 2 = gana O
     */
    public int verificarGanador() {
        // Revisar filas
        for (int i = 0; i < 3; i++) {
            if (casillas[i][0] != 0
                    && casillas[i][0] == casillas[i][1]
                    && casillas[i][1] == casillas[i][2]) {
                return casillas[i][0];
            }
        }
        // Revisar columnas
        for (int j = 0; j < 3; j++) {
            if (casillas[0][j] != 0
                    && casillas[0][j] == casillas[1][j]
                    && casillas[1][j] == casillas[2][j]) {
                return casillas[0][j];
            }
        }
        // Revisar diagonal principal
        if (casillas[0][0] != 0
                && casillas[0][0] == casillas[1][1]
                && casillas[1][1] == casillas[2][2]) {
            return casillas[0][0];
        }
        // Revisar diagonal secundaria
        if (casillas[0][2] != 0
                && casillas[0][2] == casillas[1][1]
                && casillas[1][1] == casillas[2][0]) {
            return casillas[0][2];
        }
        return 0;
    }

    /**
     * Verifica si el tablero está lleno sin ganador (empate).
     */
    public boolean hayEmpate() {
        if (verificarGanador() != 0) {
            return false;
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (casillas[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Calcula la utilidad del tablero para un jugador.
     * u(jugador) = P(jugador) - P(oponente)
     * donde P(j) = filas + columnas + diagonales disponibles para j.
     * Una línea está "disponible" si NO contiene fichas del oponente.
     */
    public int calcularUtilidad(int jugador) {
        int oponente = (jugador == 1) ? 2 : 1;
        int pJugador = contarLineasDisponibles(oponente);
        int pOponente = contarLineasDisponibles(jugador);
        return pJugador - pOponente;
    }

    /**
     * Cuenta cuántas líneas (filas, columnas, diagonales) NO contienen
     * fichas del oponente indicado. Esas son las líneas disponibles
     * para el otro jugador.
     */
    private int contarLineasDisponibles(int oponente) {
        int count = 0;

        // Revisar filas
        for (int i = 0; i < 3; i++) {
            boolean disponible = true;
            for (int j = 0; j < 3; j++) {
                if (casillas[i][j] == oponente) {
                    disponible = false;
                    break;
                }
            }
            if (disponible) count++;
        }

        // Revisar columnas
        for (int j = 0; j < 3; j++) {
            boolean disponible = true;
            for (int i = 0; i < 3; i++) {
                if (casillas[i][j] == oponente) {
                    disponible = false;
                    break;
                }
            }
            if (disponible) count++;
        }

        // Revisar diagonal principal
        boolean disponible = true;
        for (int i = 0; i < 3; i++) {
            if (casillas[i][i] == oponente) {
                disponible = false;
                break;
            }
        }
        if (disponible) count++;

        // Revisar diagonal secundaria
        disponible = true;
        for (int i = 0; i < 3; i++) {
            if (casillas[i][2 - i] == oponente) {
                disponible = false;
                break;
            }
        }
        if (disponible) count++;

        return count;
    }

    /**
     * Crea una copia profunda del tablero.
     */
    public Tablero copiar() {
        Tablero copia = new Tablero();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                copia.casillas[i][j] = this.casillas[i][j];
            }
        }
        return copia;
    }

    /**
     * Retorna la lista de posiciones libres como pares {fila, col}.
     */
    public LinkedList<int[]> casillasLibres() {
        LinkedList<int[]> libres = new LinkedList<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (casillas[i][j] == 0) {
                    libres.add(new int[]{i, j});
                }
            }
        }
        return libres;
    }
}
