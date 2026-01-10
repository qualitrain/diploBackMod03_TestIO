package mx.com.qtx.mod03TestIO.c_codepoints.lectores;

import mx.com.qtx.mod03TestIO.c_codepoints.ILectorArcTexto;

import java.io.*;
import java.nio.file.*;
import java.nio.charset.*;

public class Lector05_NIO2Simple_UTF8 implements ILectorArcTexto {
    
    @Override
    public void leerYmostrarCaracteres(String file) {
        System.out.println("\n" +
                this.getClass().getSimpleName() +
                ".leerYmostrarCaracteres");
        
        try {
            Path path = Paths.get(file);
            
            // 1. Verificar existencia
            if (!Files.exists(path)) {
                System.out.println("Archivo no encontrado: " + file);
                return;
            }
            
            // 2. Leer todo el contenido de una vez (para archivos pequeños)
            String contenido = Files.readString(path, StandardCharsets.UTF_8);
            
            // 3. Procesar code points correctamente
            procesarContenidoConCodePoints(contenido);
            
        }
        catch (MalformedInputException e) {
            System.out.println("Error: El archivo no está en UTF-8 válido");
        }
        catch (IOException e) {
            System.err.println("Error de E/S: " + e.getMessage());
        }
    }
    
    private void procesarContenidoConCodePoints(String contenido) {
        System.out.println("\nCONTENIDO (procesado por code points):");
        System.out.println("=".repeat(40));
        
        // Convertir a array de code points
        int[] codePoints = contenido.codePoints().toArray();
        
        for (int i = 0; i < codePoints.length; i++) {
            int cp = codePoints[i];
            
            // Crear String del code point
            String caracter = new String(Character.toChars(cp));
            System.out.print(caracter);
            
            // Cada 50 caracteres, mostrar información
            if ((i + 1) % 50 == 0) {
                System.out.printf("\n[Carácter %d: U+%05X] ", i + 1, cp);
            }
        }
        
        // Mostrar estadísticas
        System.out.println("\n\n" + "=".repeat(40));
        System.out.println("ESTADÍSTICAS:");
        System.out.printf("Total Code Points: %d%n", codePoints.length);
        System.out.printf("Total chars (UTF-16): %d%n", contenido.length());
        
        // Contar emojis (caracteres suplementarios)
        long emojis = contenido.codePoints().filter(cp -> cp > 0xFFFF).count();
        if (emojis > 0) {
            System.out.printf("Emojis/Caracteres suplementarios: %d%n", emojis);
        }
    }

    /**
     * Método alternativo: lectura con BufferedReader de NIO.2
     */
    public void leerConBufferedReader(String file) throws IOException {
        Path path = Paths.get(file);
        
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            
            System.out.println("\nLECTURA LINEA POR LINEA:");
            System.out.println("=".repeat(40));
            
            String linea;
            int numeroLinea = 0;
            
            while ((linea = reader.readLine()) != null) {
                numeroLinea++;
                
                // Mostrar información de la línea
                System.out.printf("\nLínea %d (chars: %d, code points: %d):%n",
                    numeroLinea, linea.length(), linea.codePoints().count());
                
                // Mostrar contenido con análisis
                System.out.print("  ");
                int[] codePoints = linea.codePoints().toArray();
                for (int cp : codePoints) {
                    String caracter = new String(Character.toChars(cp));
                    System.out.print(caracter);
                }
            }
            
            System.out.printf("\n\nTotal líneas: %d%n", numeroLinea);
            
        } catch (MalformedInputException e) {
            System.out.println("Error: El archivo contiene UTF-8 inválido");
        }
    }
}