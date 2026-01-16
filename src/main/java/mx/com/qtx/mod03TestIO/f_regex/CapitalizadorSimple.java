package mx.com.qtx.mod03TestIO.f_regex;

import java.util.Set;

public class CapitalizadorSimple {
    
    /**
     * Capitaliza nombres propios de manera simple pero efectiva
     * - Primera letra de cada palabra en mayúscula
     * - Resto en minúscula
     * - Respeta guiones y apóstrofes
     */
    public static String capitalizar(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return texto;
        }
        
        // Normalizar: quitar espacios extras, todo a minúscula
        texto = texto.trim().toLowerCase().replaceAll("\\s+", " ");
        
        StringBuilder textoCapitalizado = new StringBuilder();
        boolean capitalizarSiguiente = true;
        
        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);
            
            if (Character.isWhitespace(c)) {
                // Es un espacio: la siguiente letra debe ser mayúscula
                textoCapitalizado.append(c);
                capitalizarSiguiente = true;
            } else if (c == '-' || c == '\'') {
                // Guión o apóstrofe: mantener y capitalizar la siguiente letra
                textoCapitalizado.append(c);
                capitalizarSiguiente = true;
            } else if (capitalizarSiguiente) {
                // Primera letra después de espacio/guion/apóstrofe: MAYÚSCULA
                textoCapitalizado.append(Character.toUpperCase(c));
                capitalizarSiguiente = false;
            } else {
                // Letra normal: minúscula
                textoCapitalizado.append(c);
            }
        }
        
        return textoCapitalizado.toString();
    }
    
    /**
     * Capitaliza pero mantiene ciertas palabras en minúscula
     */
    public static String capitalizarInteligente(String texto, Set<String> palabrasMinusculas) {
        if (texto == null || texto.trim().isEmpty()) {
            return texto;
        }
        
        texto = texto.trim().toLowerCase().replaceAll("\\s+", " ");
        String[] palabras = texto.split(" ");
        
        for (int i = 0; i < palabras.length; i++) {
            String palabra = palabras[i];
            
            // Si no es la primera palabra y está en la lista, mantener minúscula
            if (i > 0 && palabrasMinusculas.contains(palabra)) {
                palabras[i] = palabra;
            } else {
                // Capitalizar la palabra
                palabras[i] = capitalizarPalabra(palabra);
            }
        }
        
        return String.join(" ", palabras);
    }
    
    private static String capitalizarPalabra(String palabra) {
        if (palabra == null || palabra.isEmpty()) {
            return palabra;
        }
        
        // Manejar palabras con guiones o apóstrofes
        if (palabra.contains("-") || palabra.contains("'")) {
            String[] partes = palabra.contains("-") ? 
                palabra.split("-") : palabra.split("'");
            
            String separador = palabra.contains("-") ? "-" : "'";
            String[] partesCapitalizadas = new String[partes.length];
            
            for (int j = 0; j < partes.length; j++) {
                partesCapitalizadas[j] = capitalizarPalabraSimple(partes[j]);
            }
            
            return String.join(separador, partesCapitalizadas);
        }
        
        return capitalizarPalabraSimple(palabra);
    }
    
    private static String capitalizarPalabraSimple(String palabra) {
        if (palabra == null || palabra.isEmpty()) {
            return palabra;
        }
        return palabra.substring(0, 1).toUpperCase() + 
               palabra.substring(1).toLowerCase();
    }
    
    // Ejemplo de uso
    public static void main(String[] args) {
        Set<String> palabrasMinusculas = Set.of("de", "del", "la", "las", "y", "el");
        
        String nombre1 = "maría de los ángeles";
        System.out.println("Simple: " + capitalizar(nombre1));
        System.out.println("Inteligente: " + capitalizarInteligente(nombre1, palabrasMinusculas));
        
        String nombre2 = "juan pérez y gómez";
        System.out.println("Simple: " + capitalizar(nombre2));
        System.out.println("Inteligente: " + capitalizarInteligente(nombre2, palabrasMinusculas));
    }
}