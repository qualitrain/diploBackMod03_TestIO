package mx.com.qtx.mod03TestIO.f_regex;

import java.util.*;
import java.util.regex.*;

public class CapitalizadorNombres {
    
    // Lista de palabras que NO deben capitalizarse (excepto al principio)
    private static final Set<String> PALABRAS_MINUSCULAS = new HashSet<>(Arrays.asList(
        "de", "del", "la", "las", "los", "el", "y", "e", "o", "u",
        "a", "ante", "bajo", "con", "contra", "desde", "en", "entre",
        "hacia", "hasta", "para", "por", "según", "sin", "so", "sobre",
        "tras", "vs", "vs.", "san", "santa", "santo", "don", "doña",
        "mac", "mc", "van", "von", "der", "di", "da", "de", "le", "al"
    ));
    
    // Palabras que SIEMPRE deben estar en mayúsculas completas
    private static final Set<String> PALABRAS_MAYUSCULAS = new HashSet<>(Arrays.asList(
        "ii", "iii", "iv", "v", "vi", "vii", "viii", "ix", "x",
        "unu", "une", "onu", "fmi", "bm", "oea", "ue", "eeuu", "usa",
        "cd", "dvd", "tv", "pc", "cpu", "ram", "gb"
    ));
    
    // Apellidos compuestos comunes (mantener mayúscula después del guión)
    private static final Set<String> APELLIDOS_COMPUESTOS = new HashSet<>(Arrays.asList(
        "garcía", "lópez", "martínez", "gonzález", "rodríguez", "fernández",
        "pérez", "gómez", "sánchez", "díaz", "hernández", "muñoz", "álvarez"
    ));
    
    /**
     * Capitaliza un nombre propio de manera inteligente
     * @param nombre El nombre a capitalizar
     * @return El nombre capitalizado correctamente
     */
    public static String capitalizarNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return nombre;
        }
        
        // Paso 1: Normalizar espacios y trim
        nombre = nombre.trim().replaceAll("\\s+", " ");
        
        // Paso 2: Convertir todo a minúsculas primero (para normalizar)
        nombre = nombre.toLowerCase();
        
        // Paso 3: Dividir en palabras
        String[] palabras = nombre.split(" ");
        List<String> lstPalabrasCapitalizadas = new ArrayList<>();
        
        for (int i = 0; i < palabras.length; i++) {
            String palabraOriginalI = palabras[i];
            String palabraCapitalizadaI;
            
            // Caso especial: palabraOriginalI vacía
            if (palabraOriginalI.isEmpty()) {
                continue;
            }
            
            // Verificar si es una palabra con guión, apóstrofe o punto
            if (palabraOriginalI.contains("-") || palabraOriginalI.contains("'") || palabraOriginalI.contains(".")) {
                palabraCapitalizadaI = capitalizarPalabraCompuesta(palabraOriginalI);
            }
            // Verificar si debe estar en MAYÚSCULAS completas
            else if (PALABRAS_MAYUSCULAS.contains(palabraOriginalI.toLowerCase())) {
                palabraCapitalizadaI = palabraOriginalI.toUpperCase();
            }
            // Verificar si es una palabra que normalmente va en minúsculas
            else if (i > 0 && i < palabras.length - 1 && 
                    PALABRAS_MINUSCULAS.contains(palabraOriginalI.toLowerCase())) {
                palabraCapitalizadaI = palabraOriginalI.toLowerCase();
            }
            // Verificar si es un apellido compuesto (como García-Márquez)
            else if (i > 0 && APELLIDOS_COMPUESTOS.contains(palabraOriginalI.toLowerCase())) {
                palabraCapitalizadaI = capitalizarSimple(palabraOriginalI);
            }
            // Palabra normal - capitalizar primera letra
            else {
                palabraCapitalizadaI = capitalizarSimple(palabraOriginalI);
            }
            
            // Caso especial: primera palabraOriginalI SIEMPRE se capitaliza
            if (i == 0 && !palabraCapitalizadaI.isEmpty()) {
                palabraCapitalizadaI = capitalizarSimple(palabraCapitalizadaI);
            }
            
            // Caso especial: última palabraOriginalI SIEMPRE se capitaliza (para apellidos)
            if (i == palabras.length - 1 && !PALABRAS_MINUSCULAS.contains(palabraOriginalI.toLowerCase())) {
                palabraCapitalizadaI = capitalizarSimple(palabraCapitalizadaI);
            }
            
            lstPalabrasCapitalizadas.add(palabraCapitalizadaI);
        }
        
        // Paso 4: Unir las palabras
        return String.join(" ", lstPalabrasCapitalizadas);
    }
    
    /**
     * Capitaliza una palabra simple (primera letra mayúscula, resto minúscula)
     */
    private static String capitalizarSimple(String palabra) {
        if (palabra == null || palabra.isEmpty()) {
            return palabra;
        }
        
        // Para palabras de una sola letra
        if (palabra.length() == 1) {
            return palabra.toUpperCase();
        }
        
        // Capitalizar primera letra, resto en minúscula
        return palabra.substring(0, 1).toUpperCase() + 
               palabra.substring(1).toLowerCase();
    }
    
    /**
     * Capitaliza palabras compuestas con guiones, apóstrofes o puntos
     */
    private static String capitalizarPalabraCompuesta(String palabra) {
        if (palabra.contains("-")) {
            return capitalizarConSeparador(palabra, "-");
        } else if (palabra.contains("'")) {
            return capitalizarConSeparador(palabra, "'");
        } else if (palabra.contains(".")) {
            return capitalizarConAbreviaciones(palabra);
        }
        
        return capitalizarSimple(palabra);
    }
    
    /**
     * Capitaliza palabras con separadores como guiones o apóstrofes
     */
    private static String capitalizarConSeparador(String palabra, String separador) {
        String[] partes = palabra.split(Pattern.quote(separador));
        List<String> partesCapitalizadas = new ArrayList<>();
        
        for (String parte : partes) {
            if (parte.isEmpty()) {
                partesCapitalizadas.add(parte);
            } else if (PALABRAS_MAYUSCULAS.contains(parte.toLowerCase())) {
                partesCapitalizadas.add(parte.toUpperCase());
            } else {
                partesCapitalizadas.add(capitalizarSimple(parte));
            }
        }
        
        return String.join(separador, partesCapitalizadas);
    }
    
    /**
     * Maneja abreviaciones como "J.R.R." o "S.A."
     */
    private static String capitalizarConAbreviaciones(String palabra) {
        String[] partes = palabra.split("\\.");
        List<String> partesCapitalizadas = new ArrayList<>();
        
        for (String parte : partes) {
            if (parte.isEmpty()) {
                partesCapitalizadas.add(parte);
            } else if (parte.length() == 1) {
                // Para iniciales como "J.R.R."
                partesCapitalizadas.add(parte.toUpperCase());
            } else {
                partesCapitalizadas.add(capitalizarSimple(parte));
            }
        }
        
        return String.join(".", partesCapitalizadas);
    }
    
    /**
     * Método alternativo: más simple para casos básicos
     */
    public static String capitalizarBasico(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return texto;
        }
        
        texto = texto.trim().toLowerCase();
        StringBuilder resultado = new StringBuilder();
        boolean nuevaPalabra = true;
        
        for (char c : texto.toCharArray()) {
            if (Character.isWhitespace(c)) {
                resultado.append(c);
                nuevaPalabra = true;
            } else if (nuevaPalabra) {
                resultado.append(Character.toUpperCase(c));
                nuevaPalabra = false;
            } else {
                resultado.append(c);
            }
        }
        
        return resultado.toString();
    }
    
    /**
     * Método para capitalizar nombres con reglas específicas por región
     */
    public static String capitalizarConReglas(String nombre, Locale locale) {
        // Primero capitalizamos de manera básica
        nombre = capitalizarNombre(nombre);
        
        // Ajustes específicos por idioma/región
        switch (locale.getLanguage()) {
            case "es": // Español
                return ajustarCapitalizacionEspanol(nombre);
            case "en": // Inglés
                return ajustarCapitalizacionIngles(nombre);
            case "fr": // Francés
                return ajustarCapitalizacionFrances(nombre);
            default:
                return nombre;
        }
    }
    
    private static String ajustarCapitalizacionEspanol(String nombre) {
        // En español, tratamos especialmente los apellidos compuestos
        String[] palabras = nombre.split(" ");
        
        for (int i = 1; i < palabras.length; i++) { // Empezamos desde 1 para no afectar el nombre
            String palabra = palabras[i].toLowerCase();
            
            // Si parece un apellido compuesto (empieza con "de ", "del ", etc.)
            if (palabra.startsWith("de ") || palabra.startsWith("del ") || 
                palabra.startsWith("la ") || palabra.startsWith("las ")) {
                // Mantener en minúscula
                palabras[i] = palabras[i].toLowerCase();
            }
        }
        
        return String.join(" ", palabras);
    }
    
    private static String ajustarCapitalizacionIngles(String nombre) {
        // En inglés, tratamos especialmente los prefijos como "Mc", "Mac"
        return nombre.replaceAll("\\b(Mc)([a-z])", "Mc$2")
                     .replaceAll("\\b(Mac)([a-z])", "Mac$2")
                     .replaceAll("\\b(O')([a-z])", "O'$2");
    }
    
    private static String ajustarCapitalizacionFrances(String nombre) {
        // En francés, tratamos especialmente los artículos
        return nombre.replaceAll("\\b(D')([a-z])", "d'$2")
                     .replaceAll("\\b(De )(La )", "de la ")
                     .replaceAll("\\b(Des )", "des ");
    }
    
    /**
     * Método principal para probar la capitalización
     */
    public static void main(String[] args) {
        System.out.println("=== PRUEBAS DE CAPITALIZACIÓN ===\n");
        
        // Lista de nombres para probar
        String[] nombresPrueba = {
            // Casos básicos
            "juan pérez",
            "maría josé garcía lópez",
            "ana maría rodríguez",
            
            // Con artículos/preposiciones
            "juan de dios martínez",
            "maría de los ángeles fernández",
            "josé de la cruz gómez",
            
            // Con guiones
            "jean-claude van damme",
            "ana-maría pérez-gómez",
            "luis alberto sánchez-hernández",
            
            // Con apóstrofes
            "o'connor smith",
            "d'artagnan de la roche",
            
            // Con abreviaciones
            "j.r.r. tolkien",
            "j.k. rowling",
            "a.b. garcía",
            
            // Con números romanos
            "juan pablo ii",
            "luis xiv de francia",
            
            // Con prefijos
            "mc donald",
            "mac arthur",
            "van gogh",
            "von braun",
            
            // Mayúsculas completas
            "cd román",
            "tv azteca",
            "ram ón pérez",
            
            // Casos especiales
            "EL CORONEL NO TIENE QUIEN LE ESCRIBA", // Todo mayúsculas
            "de la calle y gonzález",               // "y" en minúscula
            "a e i o u",                           // Vocales solas
            "  maría   del   carmen  ",            // Múltiples espacios
        };
        
        // Probar cada nombre
        for (String nombre : nombresPrueba) {
            String capitalizado = capitalizarNombre(nombre);
            String basico = capitalizarBasico(nombre);
            
            System.out.println("Original:    \"" + nombre + "\"");
            System.out.println("Capitalizado: \"" + capitalizado + "\"");
            System.out.println("Básico:       \"" + basico + "\"");
            
            // Verificar diferencias
            if (!capitalizado.equals(basico)) {
                System.out.println("⚠  Los métodos dan resultados diferentes!");
            }
            
            System.out.println();
        }
        
        // Probar con diferentes regiones
        System.out.println("\n=== CAPITALIZACIÓN POR REGIÓN ===");
        
        String nombre = "jean de la fontaine";
        System.out.println("Original: " + nombre);
        System.out.println("Español:  " + capitalizarConReglas(nombre, new Locale("es")));
        System.out.println("Inglés:   " + capitalizarConReglas(nombre, new Locale("en")));
        System.out.println("Francés:  " + capitalizarConReglas(nombre, Locale.FRENCH));
    }
}