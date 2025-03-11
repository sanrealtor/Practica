import java.util.Scanner;
import java.util.Random;

/**
 * Clase BattleShip
 * Simula un juego de batalla naval entre un jugador humano y una máquina.
 * @author Alejandro Arango
 * @version 2.0
 */


public class BattleShip1D {

    private static final int TAMANO_TABLERO = 10; // Tamaño del tablero
    private static final int AGUA = 0;           // Representa agua
    private static final int BARCO = 1;          // Representa un barco
    private static final int TOCADO = 2;         // Representa un barco tocado
    private static final int HUNDIDO = 3;        // Representa un barco hundido

    private static Scanner input = new Scanner(System.in);
    private static Random rand = new Random();

    public static void main(String[] args) {
        System.out.println("*************************************");
        System.out.println("Bienvenido al juego de Batalla Naval 1D");
        System.out.println("*************************************");

        while (true) {
            int[] tableroJugador = new int[TAMANO_TABLERO];  // Tablero del jugador
            int[] tableroMaquina = new int[TAMANO_TABLERO];  // Tablero de la máquina

            // Colocar barcos en ambos tableros
            colocarBarcos(tableroJugador, true);  // Jugador coloca sus barcos
            colocarBarcos(tableroMaquina, false); // Máquina coloca sus barcos

            System.out.println("\n¡Comienza la partida!");
            jugar(tableroJugador, tableroMaquina);  // Comienza el juego

            // Preguntar si el jugador quiere jugar otra partida
            System.out.print("¿Deseas jugar otra partida? (S/N): ");
            char respuesta = input.next().charAt(0);
            if (respuesta != 'S' && respuesta != 's') {
                System.out.println("Gracias por jugar.");
                break;
            }
        }
    }

    /*
     * colocarBarcos
     * Coloca los barcos en el tablero, ya sea para el jugador o la máquina.
     * @param tablero El tablero donde se colocarán los barcos.
     * @param esJugador Indica si el jugador está colocando los barcos (true) o la máquina (false).
     */
    private static void colocarBarcos(int[] tablero, boolean esJugador) {
        if (esJugador) {
            System.out.println("\nColoca tus barcos en el tablero:");
        } else {
            System.out.println("\nLa máquina está colocando sus barcos...");
        }

        // Colocar un barco de tamaño 1 (lancha)
        colocarBarco(tablero, 1, esJugador);

        // Colocar un barco de tamaño 2 (barco médico)
        colocarBarco(tablero, 2, esJugador);

        // Colocar un barco de tamaño 3 (barco de munición)
        colocarBarco(tablero, 3, esJugador);
    }

    /*
     * colocarBarco
     * Coloca un barco en el tablero en una posición válida.
     * @param tablero El tablero donde se colocará el barco.
     * @param tamanoBarco El tamaño del barco a colocar.
     * @param esJugador Indica si el jugador está colocando el barco (true) o la máquina (false).
     */
    private static void colocarBarco(int[] tablero, int tamanoBarco, boolean esJugador) {
        int posicion;
        boolean posicionValida;

        do {
            if (esJugador) {
                System.out.print("Ingresa la posición inicial (1-10) para el barco de tamaño " + tamanoBarco + ": ");
                posicion = input.nextInt() - 1;  // Ajustar a índice 0-9
            } else {
                posicion = rand.nextInt(TAMANO_TABLERO - tamanoBarco + 1);  // Posición aleatoria
            }

            posicionValida = true;
            for (int i = 0; i < tamanoBarco; i++) {
                if (tablero[posicion + i] != AGUA) {
                    posicionValida = false;
                    break;
                }
            }

            if (posicionValida) {
                for (int i = 0; i < tamanoBarco; i++) {
                    tablero[posicion + i] = BARCO;  // Colocar el barco
                }
                break;
            } else if (esJugador) {
                System.out.println("Posición no válida. Intente de nuevo.");
            }
        } while (esJugador);
    }

    /*
     * jugar
     * Controla el flujo del juego, alternando turnos entre el jugador y la máquina.
     * @param tableroJugador El tablero del jugador.
     * @param tableroMaquina El tablero de la máquina.
     */
    private static void jugar(int[] tableroJugador, int[] tableroMaquina) {
        while (true) {
            turnoJugador(tableroMaquina);  // Turno del jugador
            if (verificarVictoria(tableroMaquina)) {
                System.out.println("¡Has ganado!");
                break;
            }

            turnoMaquina(tableroJugador);  // Turno de la máquina
            if (verificarVictoria(tableroJugador)) {
                System.out.println("¡La máquina ha ganado!");
                break;
            }

            mostrarTableros(tableroJugador, tableroMaquina);  // Mostrar estado de los tableros
        }
    }

    /*
     * turnoJugador
     * Permite al jugador atacar una posición en el tablero de la máquina.
     * @param tableroMaquina El tablero de la máquina.
     */
    private static void turnoJugador(int[] tableroMaquina) {
        int posicion;
        do {
            System.out.print("Ingresa la posición (1-10) para atacar: ");
            posicion = input.nextInt() - 1;  // Ajustar a índice 0-9
        } while (posicion < 0 || posicion >= TAMANO_TABLERO);

        if (tableroMaquina[posicion] == BARCO) {
            System.out.println("¡Impacto!");
            tableroMaquina[posicion] = TOCADO;
            verificarHundido(tableroMaquina, posicion);
        } else if (tableroMaquina[posicion] == TOCADO || tableroMaquina[posicion] == HUNDIDO) {
            System.out.println("Ya habías atacado esta posición.");
        } else {
            System.out.println("¡Agua!");
            tableroMaquina[posicion] = AGUA;
        }
    }

    /*
     * turnoMaquina
     * Permite a la máquina atacar una posición en el tablero del jugador.
     * @param tableroJugador El tablero del jugador.
     */
    private static void turnoMaquina(int[] tableroJugador) {
        int posicion = rand.nextInt(TAMANO_TABLERO);  // Posición aleatoria

        System.out.println("La máquina ataca en la posición " + (posicion + 1));

        if (tableroJugador[posicion] == BARCO) {
            System.out.println("¡La máquina ha impactado!");
            tableroJugador[posicion] = TOCADO;
            verificarHundido(tableroJugador, posicion);
        } else if (tableroJugador[posicion] == TOCADO || tableroJugador[posicion] == HUNDIDO) {
            System.out.println("La máquina ya había atacado esta posición.");
        } else {
            System.out.println("¡La máquina ha fallado!");
            tableroJugador[posicion] = AGUA;
        }
    }

    /*
     * verificarHundido
     * Verifica si un barco ha sido hundido después de un impacto.
     * @param tablero El tablero donde se verifica el hundimiento.
     * @param posicion La posición del impacto.
     */
    private static void verificarHundido(int[] tablero, int posicion) {
        int inicio = posicion;
        while (inicio > 0 && tablero[inicio - 1] == TOCADO) {
            inicio--;
        }

        int fin = posicion;
        while (fin < TAMANO_TABLERO - 1 && tablero[fin + 1] == TOCADO) {
            fin++;
        }

        boolean hundido = true;
        for (int i = inicio; i <= fin; i++) {
            if (tablero[i] != TOCADO) {
                hundido = false;
                break;
            }
        }

        if (hundido) {
            for (int i = inicio; i <= fin; i++) {
                tablero[i] = HUNDIDO;
            }
            System.out.println("¡Barco hundido!");
        }
    }

    /*
     * verificarVictoria
     * Verifica si todos los barcos en un tablero han sido hundidos.
     * @param tablero El tablero a verificar.
     * @return true si todos los barcos han sido hundidos, false en caso contrario.
     */
    private static boolean verificarVictoria(int[] tablero) {
        for (int i = 0; i < TAMANO_TABLERO; i++) {
            if (tablero[i] == BARCO) {
                return false;
            }
        }
        return true;
    }

    /*
     * mostrarTableros
     * Muestra el estado actual de los tableros del jugador y la máquina.
     * @param tableroJugador El tablero del jugador.
     * @param tableroMaquina El tablero de la máquina.
     */
    private static void mostrarTableros(int[] tableroJugador, int[] tableroMaquina) {
        System.out.println("\nTablero del Jugador:");
        mostrarTablero(tableroJugador);

        System.out.println("\nTablero de la Máquina:");
        mostrarTablero(tableroMaquina);
    }

    /*
     * mostrarTablero
     * Muestra el estado de un tablero.
     * @param tablero El tablero a mostrar.
     */
    private static void mostrarTablero(int[] tablero) {
        for (int i = 0; i < TAMANO_TABLERO; i++) {
            System.out.print("[" + tablero[i] + "]");
        }
        System.out.println();
    }
}