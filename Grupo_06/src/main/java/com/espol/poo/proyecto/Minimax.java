package com.espol.poo.proyecto;

import java.util.LinkedList;

/**
 * Implementación del algoritmo Minimax usando un Árbol N-ario (Tree).
 *
 * Estrategia: "Elige el mejor movimiento para ti, asumiendo que el
 * oponente escogerá el peor para ti."
 *
 * Pasos (según el PDF):
 * 1. Generar posibles estados a 2 niveles (turno propio + turno oponente)
 * 2. Calcular utilidad de las hojas (nivel 2)
 * 3. Encontrar la utilidad mínima por familia (min de hijos de cada nodo nivel 1)
 * 4. Elegir el nodo nivel 1 con la máxima utilidad mínima
 */
public class Minimax {

    /**
     * Decide la mejor jugada para el jugador indicado.
     * @param actual estado actual del tablero
     * @param jugador el jugador que tiene el turno (1=X, 2=O)
     * @return int[]{fila, col} con la mejor jugada, o null si no hay jugadas
     */
    public static int[] decidirJugada(Tablero actual, int jugador) {
        int oponente = (jugador == 1) ? 2 : 1;

        // Crear la raíz del árbol n-ario con el tablero actual
        Tree<Tablero> arbol = new Tree<>();
        NodeTree<Tablero> raiz = new NodeTree<>(actual);
        arbol.setRoot(raiz);

        // Obtener las casillas libres para generar movimientos
        LinkedList<int[]> movimientos = actual.casillasLibres();
        if (movimientos.isEmpty()) {
            return null;
        }

        int mejorUtilidad = Integer.MIN_VALUE;
        int[] mejorMovimiento = null;

        // PASO 1: Generar nivel 1 (movimientos del jugador actual)
        for (int[] mov : movimientos) {
            // Crear tablero hijo con el movimiento del jugador
            Tablero tableroHijo = actual.copiar();
            tableroHijo.colocar(mov[0], mov[1], jugador);

            // Crear nodo y subárbol para este movimiento
            NodeTree<Tablero> nodoHijo = new NodeTree<>(tableroHijo);
            Tree<Tablero> subArbol = new Tree<>();
            subArbol.setRoot(nodoHijo);
            raiz.getChildren().add(subArbol);

            int utilidadMinima;

            // Si este movimiento gana el juego, es la mejor opción
            if (tableroHijo.verificarGanador() == jugador) {
                utilidadMinima = 1000;
            }
            // Si el tablero está lleno (empate)
            else if (tableroHijo.hayEmpate()) {
                utilidadMinima = 0;
            }
            // PASOS 2 y 3: Generar nivel 2 (respuestas del oponente)
            else {
                utilidadMinima = Integer.MAX_VALUE;
                LinkedList<int[]> movsOponente = tableroHijo.casillasLibres();

                for (int[] movOp : movsOponente) {
                    // Crear tablero nieto con el movimiento del oponente
                    Tablero tableroNieto = tableroHijo.copiar();
                    tableroNieto.colocar(movOp[0], movOp[1], oponente);

                    // Agregar al árbol
                    NodeTree<Tablero> nodoNieto = new NodeTree<>(tableroNieto);
                    Tree<Tablero> subArbolNieto = new Tree<>();
                    subArbolNieto.setRoot(nodoNieto);
                    nodoHijo.getChildren().add(subArbolNieto);

                    // Calcular utilidad de la hoja
                    int utilidad;
                    if (tableroNieto.verificarGanador() == oponente) {
                        utilidad = -1000; // El oponente gana: muy malo
                    } else if (tableroNieto.hayEmpate()) {
                        utilidad = 0;
                    } else {
                        utilidad = tableroNieto.calcularUtilidad(jugador);
                    }

                    // Buscar la utilidad mínima (peor caso para nosotros)
                    if (utilidad < utilidadMinima) {
                        utilidadMinima = utilidad;
                    }
                }
            }

            // PASO 4: Elegir el movimiento con la máxima utilidad mínima
            System.out.println("Evaluando fila " + mov[0] + ", col " + mov[1] + " -> Utilidad Minima: " + utilidadMinima);

            if (utilidadMinima > mejorUtilidad) {
                mejorUtilidad = utilidadMinima;
                mejorMovimiento = mov;
            }
        }

        System.out.println("=> Computadora elige movimiento: (" + mejorMovimiento[0] + ", " + mejorMovimiento[1] + ") con utilidad " + mejorUtilidad);
        System.out.println("---------------------------------------------------");
        return mejorMovimiento;
    }
}
