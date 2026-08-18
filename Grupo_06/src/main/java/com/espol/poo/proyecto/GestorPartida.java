package com.espol.poo.proyecto;

import java.io.*;

/**
 * Gestiona el guardado y carga de partidas a medio jugar.
 * Formato del archivo de texto:
 *   Línea 1: modo (HvC, HvH, CvC)
 *   Línea 2: símbolo de la computadora (1=X, 2=O, 0=no aplica)
 *   Línea 3: turno actual (1=X, 2=O)
 *   Líneas 4-6: filas del tablero (valores separados por coma)
 */
public class GestorPartida {

    /**
     * Guarda el estado de la partida en un archivo de texto.
     */
    public static void guardar(File archivo, String modo, int simboloComputadora,
                               int turnoActual, int[][] casillas) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(archivo))) {
            pw.println(modo);
            pw.println(simboloComputadora);
            pw.println(turnoActual);
            for (int i = 0; i < 3; i++) {
                pw.println(casillas[i][0] + "," + casillas[i][1] + "," + casillas[i][2]);
            }
        } catch (IOException e) {
            System.out.println("Error al guardar: " + e.getMessage());
        }
    }

    /**
     * Carga una partida desde un archivo de texto.
     * @return Object[]{modo, simboloComputadora, turnoActual, casillas} o null si falla
     */
    public static Object[] cargar(File archivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String modo = br.readLine().trim();
            int simboloComp = Integer.parseInt(br.readLine().trim());
            int turno = Integer.parseInt(br.readLine().trim());
            int[][] casillas = new int[3][3];
            for (int i = 0; i < 3; i++) {
                String[] partes = br.readLine().trim().split(",");
                for (int j = 0; j < 3; j++) {
                    casillas[i][j] = Integer.parseInt(partes[j]);
                }
            }
            return new Object[]{modo, simboloComp, turno, casillas};
        } catch (Exception e) {
            System.out.println("Error al cargar: " + e.getMessage());
            return null;
        }
    }
}
