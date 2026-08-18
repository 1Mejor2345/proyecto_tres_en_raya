# Tres en Raya contra el computador

Proyecto desarrollado en JavaFX para la materia **Estructuras de Datos**. La aplicación permite jugar Tres en Raya en un tablero de 3x3 y utiliza el algoritmo **Minimax**, junto con un **árbol n-ario**, para que la computadora seleccione sus movimientos.

## Descripción

En cada turno, los jugadores colocan una ficha **X** u **O** en una casilla libre. Gana quien complete primero una fila, columna o diagonal. Si se ocupan todas las casillas sin que exista un ganador, la partida termina en empate.

Antes de empezar, el usuario puede seleccionar:

- El modo de juego.
- El símbolo que desea utilizar: X u O.
- Quién realiza el primer movimiento: el humano o la computadora.

## Modos de juego

- **Humano vs Computadora:** el usuario juega contra la computadora.
- **Humano vs Humano:** dos personas juegan desde el mismo equipo.
- **Computadora vs Computadora:** ambas fichas son controladas automáticamente.

## Funcionalidades

- Interfaz gráfica creada con JavaFX.
- Selección del símbolo y del jugador que inicia.
- Validación de casillas ocupadas y control de turnos.
- Detección de victorias y empates.
- Decisión de jugadas mediante Minimax.
- Implementación propia de un árbol n-ario genérico.
- Reinicio de la partida.
- Guardado de partidas en archivos de texto.
- Carga de partidas para continuarlas posteriormente.

## Minimax y función de utilidad

La computadora analiza el estado actual del tablero generando un árbol de dos niveles:

1. El primer nivel contiene todos los movimientos que puede realizar el jugador que tiene el turno.
2. El segundo nivel contiene las posibles respuestas del oponente para cada movimiento anterior.

Cada nodo del árbol contiene un objeto `Tablero`. Para escoger una jugada, el algoritmo calcula la utilidad de los estados del segundo nivel, conserva la utilidad mínima de cada familia y finalmente selecciona el movimiento que tenga la mayor de esas utilidades mínimas.

La función utilizada es:

```text
u(jugador) = P(jugador) - P(oponente)
```

Donde `P` representa la cantidad de filas, columnas y diagonales que aún se encuentran disponibles para cada jugador.

## Estructuras utilizadas

- `int[][]` para representar las nueve casillas del tablero.
- `LinkedList<int[]>` para almacenar las posiciones libres.
- `LinkedList<Tree<E>>` para almacenar los hijos de cada nodo.
- `Tree<E>` y `NodeTree<E>` como implementación genérica del árbol n-ario.

## Estructura del Repositorio

- `Grupo_06/`: Directorio que contiene el proyecto de NetBeans con el código fuente en Java.
- `Grupo_06.docx`: Documento de reporte final con capturas de pantalla, explicaciones y la rúbrica de co-evaluación del grupo.
- `README.md`: Este archivo.

### Clases principales

- **`Proyecto`**: inicia la aplicación JavaFX.
- **`PantallaConfiguracion`**: permite seleccionar el modo, símbolo y turno inicial.
- **`PantallaJuego`**: controla la interfaz del tablero, los turnos y el final de la partida.
- **`Tablero`**: representa el estado del juego, comprueba victorias y calcula la utilidad.
- **`Minimax`**: genera los posibles estados y escoge la mejor jugada.
- **`Tree` y `NodeTree`**: forman el árbol n-ario utilizado por Minimax.
- **`GestorPartida`**: guarda y carga partidas mediante archivos de texto.

## Guardado de partidas

Las partidas se guardan en un archivo `.txt` con la siguiente información:

1. Modo de juego.
2. Símbolo de la computadora.
3. Turno actual.
4. Las tres filas del tablero.

Al cargar el archivo, la aplicación reconstruye el tablero y permite continuar desde el turno guardado.

## Contexto académico

Este proyecto fue realizado como parte de la segunda evaluación de la materia Estructuras de Datos, PAO 1 - 2026, ESPOL.
