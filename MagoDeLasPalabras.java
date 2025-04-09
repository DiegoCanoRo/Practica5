package comdiegocano.magodelaspalabras;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class MagoDeLasPalabras {

    private ArrayList<Jugador> jugadores;
    private Diccionario diccionario;
    private int rondaActual;
    private int totalRondas = 3;
    private HashSet<String> palabrasUsadasEnRonda;
    private int turno;
    private boolean modoRegular;
    private boolean modoExperto;

    public MagoDeLasPalabras(ArrayList<Jugador> jugadores, Diccionario diccionario) {
        this.jugadores = jugadores;
        this.diccionario = diccionario;
        this.rondaActual = 1;
        this.palabrasUsadasEnRonda = new HashSet<>();
        this.turno = 0;
        modoRegular = true;
        modoExperto = false;
    }

    public void iniciarJuego() {
        while (rondaActual <= totalRondas) {
            System.out.println("\n----- INICIO RONDA " + rondaActual + " -----");
            iniciarRonda();
            rondaActual++;
        }
        mostrarResultadosFinales();
        determinarGanador();
    }

    private void iniciarRonda() {
        repartirLetras();
        palabrasUsadasEnRonda.clear();
        boolean rondaActiva = true;
        Scanner sc = new Scanner(System.in);

        while (rondaActiva) {
            Jugador jugador = jugadores.get(turno);
            System.out.println("\nTurno de " + jugador.getNombre());
            System.out.println("Letras disponibles: " + jugador.getLetrasDisponibles());

            System.out.print("Escribe una palabra o escribe " + -1 + " para saltar: ");
            String palabra = sc.nextLine().toLowerCase().trim();

            //si el  jugador pone -1 salta turno
            if (palabra.equals("-1")) {
                cambiarTurno();
                repartirLetras();
                continue;
            }

            boolean esValida = validarPalabra(palabra, jugador);
            jugador.agregarPalabra(palabra, esValida, letrasUsadas(palabra));

            //verifica si la palabra es valida
            if (esValida) {
                palabrasUsadasEnRonda.add(palabra);
                System.out.println("Palabra aceptada, Puntos actuales: " + jugador.getPuntuacion());
            } else {
                System.out.println("Palabra invalida. -5 puntos.");

            }

            //pregunta al jugador si desea seguir en la ronda
            //esto por si ya no sabe que palabra poner
            System.out.print("¿Desean seguir esta ronda? (s/n): ");
            String seguir = sc.nextLine().toLowerCase();
            if (seguir.equals("n")) {
                rondaActiva = false;
            } else {
                cambiarTurno();
            }
        }

        mostrarResultadosRonda();
        reiniciarPalabrasJugadores();
    }

    public void repartirLetras() {
        Random random = new Random();
        String vocales = "aeiouáéíú";
        String consonantes = "bcdfghjklmnñpqrstvwxz";

        int cantVocales = 0;
        int cantConsonantes = 0;
        int a = 0;
        int n = 0;
        String abecedario = "abcdefghijklmnopqrstuvwxyzáéíóú";

        if (modoRegular) {
            cantVocales = 4;
            cantConsonantes = 6;

            for (Jugador jugador : jugadores) {
                HashSet<Character> letras = new HashSet<>();
                while (letras.size() < 10) {
                    if (a <= 4) {
                        letras.add(vocales.charAt(random.nextInt(vocales.length())));
                        a++;
                    } else {
                        letras.add(consonantes.charAt(random.nextInt(consonantes.length())));
                        a = 0;
                    }

                }

                jugador.actualizarLetrasDisponibles(letras);
            }
        }

    }

    public boolean validarPalabra(String palabra, Jugador jugador) {
        if (!diccionario.obtenerPalabras().contains(palabra)) {
            return false;
        }

        if (palabrasUsadasEnRonda.contains(palabra)) {
            return false;
        }
        HashSet<Character> copiaLetras = new HashSet<>(jugador.getLetrasDisponibles());
        for (char c : palabra.toCharArray()) {
            if (!copiaLetras.remove(c)) {
                return false;
            }
        }
        return true;
    }

    //hasshset que almacena las letras usadas en la ronda
    public HashSet<Character> letrasUsadas(String palabra) {
        HashSet<Character> letras = new HashSet<>();
        for (char c : palabra.toCharArray()) {
            letras.add(c);
        }
        return letras;
    }

    public void cambiarTurno() {
        turno = (turno + 1) % jugadores.size();
    }

    public void mostrarResultadosRonda() {
        System.out.println("\n--- Resultados de la ronda ---");
        for (Jugador jugador : jugadores) {
            jugador.mostrarInfo();
        }
    }

    public void reiniciarPalabrasJugadores() {
        for (Jugador jugador : jugadores) {
            jugador.getPalabrasFormadas().clear();
        }
    }

    public void mostrarResultadosFinales() {
        System.out.println("\n----- Resultados Finales -----");
        for (Jugador jugador : jugadores) {
            System.out.println(jugador.getNombre() + " - Puntos: " + jugador.getPuntuacion());
        }
    }

    public void determinarGanador() {
        Jugador ganador = jugadores.get(0);
        for (Jugador jugador : jugadores) {
            if (jugador.getPuntuacion() > ganador.getPuntuacion()) {
                ganador = jugador;
            }
        }
        System.out.println("\n¡El ganador es " + ganador.getNombre() + " con " + ganador.getPuntuacion() + " puntos!");
    }

    public static void main(String[] args) {
        Diccionario diccionario = new Diccionario();
        diccionario.cargarPalabras();

        //btener y mostrar las palabras cargadas
        List<String> palabras = diccionario.obtenerPalabras();
        System.out.println("\nPalabras:");
        for (String palabra : palabras) {
            System.out.println(palabra);
        }

        String palabraAConsultar = "hola";
        int puntos = diccionario.obtenerPuntos(palabraAConsultar);
        System.out.println("\nPuntos de '" + palabraAConsultar + "': " + puntos);

        Scanner scanner = new Scanner(System.in);

        int numJugadores;
        ArrayList<Jugador> jugadores = new ArrayList<>();

        do {
            System.out.print("Ingrese el número de jugadores (2, 3 o 4): ");
            while (!scanner.hasNextInt()) {
                System.out.println("Entrada invalida. Debe ser un número.");
                scanner.next();
            }
            numJugadores = scanner.nextInt();
        } while (numJugadores < 2 || numJugadores > 4);
        scanner.nextLine();

        for (int i = 0; i < numJugadores; i++) {
            String nombre;
            do {
                System.out.print("Ingrese el nombre del jugador " + (i + 1) + ": ");
                nombre = scanner.nextLine().trim();
            } while (nombre.isEmpty());

            Jugador jugador = new Jugador(nombre);
            jugadores.add(jugador);
        }
        MagoDeLasPalabras juego = new MagoDeLasPalabras(jugadores, diccionario);
        juego.iniciarJuego();
    }

}
