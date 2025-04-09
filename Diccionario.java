package comdiegocano.magodelaspalabras;


import java.io.*;
import java.util.*;

public class Diccionario {    
    
    private HashMap<String, Integer> palabrasValidas;
    
    
    public Diccionario() {
        this.palabrasValidas = new HashMap<>();
    }
    
    // mtodo que carga las palabras del archivo y las almacena en un HashMap con puntuacio n
    public void cargarPalabras() {
        try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\diego\\Documents"
            + "\\NetBeansProjects\\MagoDeLasPalabras\\src\\main"
            + "\\java\\comdiegocano\\magodelaspalabras\\palabras.txt"))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                // Se parte cada palabra del archivo, se eliminan los caracteres no alfabeticos
                // y se maneja tambien la separación por comas
                String[] palabrasEnArchivo = linea.toLowerCase().split("[^\\p{L},]+");

                for (String palabra : palabrasEnArchivo) {
                    palabra = palabra.trim();
                    if (!palabra.isEmpty()) {
                        // Si la palabra no está en el HashMap, la agregamos con su puntuación
                        if (!palabrasValidas.containsKey(palabra)) {
                            int puntos = calcularPuntos(palabra);
                            palabrasValidas.put(palabra, puntos);
                            // Imprime la palabra y su puntuación mientras se cargan
                            System.out.println("Palabra cargada: " + palabra + " | Puntos: " + puntos);
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }
    }

    // metodo que obtiene las palabras vaidas del diccionario
    public List<String> obtenerPalabras() {
        return new ArrayList<>(palabrasValidas.keySet());
    }

    // metodo que obtiene la puntuacion de una palabra
    public int obtenerPuntos(String palabra) {
        return palabrasValidas.getOrDefault(palabra, 0); // devuelve 0 si la palabra no existe
    }

    // metodo para calcular los puntos de una palabra
    public int calcularPuntos(String palabra) {
        int puntos = 0;
        for (char c : palabra.toCharArray()) {
            if (esVocal(c)) {
                puntos += 5;  // 5 puntos por cada vocal
            } else {
                puntos += 3;  // 3 puntos por cada consonante
            }
        }
        return puntos;
    }

    //verifica si un caracter es una vocal
    public boolean esVocal(char c) {
        return "aeiouáéíóú".indexOf(c) != -1;
    }
    
    public boolean esConsonante(char c) {
        return "bcdfghjklmnpqrstvwxyz".indexOf(c) >= 0;
    }

}
