package comdiegocano.magodelaspalabras;


import java.util.HashSet;

public class Jugador {
    private String nombre;
    private HashSet<String> palabrasFormadas;
    private int puntuacion;
    private HashSet<Character> letrasDisponibles;
    
    public Jugador(String nombre) {
        this.nombre = nombre;
        this.palabrasFormadas = new HashSet<>();
        this.puntuacion = 0;
        this.letrasDisponibles = new HashSet<>();
    }

    public String getNombre() {
        return nombre;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public HashSet<String> getPalabrasFormadas() {
        return palabrasFormadas;
    }

    public HashSet<Character> getLetrasDisponibles() {
        return letrasDisponibles;
    }

    public void actualizarLetrasDisponibles(HashSet<Character> nuevasLetras) {
        this.letrasDisponibles = nuevasLetras;
    }

    public void agregarPalabra(String palabra, boolean esValida, 
            HashSet<Character> letrasUsadas) {
        
        if (esValida && !palabrasFormadas.contains(palabra)) {
            palabrasFormadas.add(palabra);
            letrasDisponibles.removeAll(letrasUsadas);
            puntuacion += calcularPuntos(palabra);
        } else if (!esValida) {
            puntuacion -= 5; //penalizacion por no poner una palabra valida
        }
    }
    
    public int calcularPuntos(String palabra) {
        int puntos = 0;
        for (char c : palabra.toCharArray()) {
            if (esVocal(c)) {
                puntos += 5;
            } else {
                puntos += 3;
            }
        }
        return puntos;
    }

    public boolean esVocal(char c) {
        return "aeiouáéíóú".indexOf(c) != -1;
    }

    public void mostrarInfo() {
        System.out.println("Jugador: " + nombre);
        System.out.println("Puntuación: " + puntuacion);
        System.out.println("Palabras formadas: ");
        for (String palabra : palabrasFormadas) {
            System.out.println("  - " + palabra);
        }
    }
    
    
    
}
