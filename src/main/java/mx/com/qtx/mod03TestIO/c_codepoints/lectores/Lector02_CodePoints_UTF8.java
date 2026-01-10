package mx.com.qtx.mod03TestIO.c_codepoints.lectores;

import mx.com.qtx.mod03TestIO.c_codepoints.ILectorArcTexto;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Lector02_CodePoints_UTF8 implements ILectorArcTexto {
    private static final int EOF = -1;
    
    @Override
    public void leerYmostrarCaracteres(String file) {
        System.out.println("\n" +
                this.getClass().getSimpleName() +
                ".leerYmostrarCaracteres");
        
        try (InputStreamReader reader = new InputStreamReader(
                new FileInputStream(file), StandardCharsets.UTF_8)) {
            
            int totalCodePoints = 0;
            
            while (true) {
                // Leer la primera unidad de código
                int unidadUtf8 = reader.read(); // Lee caracter UTF-8, un byte
                if (unidadUtf8 == EOF) {
                    System.out.println("\n: " + totalCodePoints + " code points decodificados con UTF-8");
                    break;
                }
                
                char primerChar = (char) unidadUtf8;
                
                if (Character.isHighSurrogate(primerChar)) {
                    // Es la primera parte de un par suplente (carácter suplementario)
                    // Necesitamos leer la segunda parte
                    int segundaUnidadUtf8 = reader.read();
                    if (segundaUnidadUtf8 == EOF) {
                        // Error: archivo truncado (high surrogate sin low)
                        System.out.print(primerChar);
                        totalCodePoints++;
                        break;
                    }
                    char segundoChar = (char) segundaUnidadUtf8;
                    if (Character.isLowSurrogate(segundoChar)) {
                        totalCodePoints = armarYmostrarCodePointCompuesto(primerChar, segundoChar, totalCodePoints);
                    }
                    else {
                        totalCodePoints = procesarErrorDeCaracterMalFormado(primerChar, segundoChar, totalCodePoints);
                    }
                    
                }
                else
                if (Character.isLowSurrogate(primerChar) == false) {
                    totalCodePoints = mostrarCaracterBmpNormal(primerChar, totalCodePoints, unidadUtf8);
                }
                else {
                    // Low surrogate sin high surrogate previo (error)
                    System.out.print(primerChar);
                    totalCodePoints++;
                }
            }
            
        }
        catch (FileNotFoundException e) {
            System.out.println("Oops no existe el archivo [" + file + "]");
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static int mostrarCaracterBmpNormal(char primerChar, int totalCodePoints, int unidadUtf8) {
        // Es un carácter BMP normal (no es parte de un par suplente y no requiere dos chars para obtener su codepoint)
        System.out.print(primerChar);
        totalCodePoints++;

        // Para fines didácticos, mostrar info de caracteres no ASCII
        if (unidadUtf8 > 127) {
            System.out.printf(" [U+%04X]", unidadUtf8);
        }
        return totalCodePoints;
    }

    private static int armarYmostrarCodePointCompuesto(char primerChar, char segundoChar, int totalCodePoints) {
        // ¡Tenemos un par suplente válido!
        // Combinar para formar el code point completo
        int codePoint = Character.toCodePoint(primerChar, segundoChar);
        String caracterCompleto = new String(Character.toChars(codePoint)); // Genera un string conteniendo 2 chars UTF-16
        System.out.print(caracterCompleto);
        totalCodePoints++;

        // Para fines didácticos, mostrar información del emoji
        System.out.printf(" [U+%05X]", codePoint);
        return totalCodePoints;
    }

    private static int procesarErrorDeCaracterMalFormado(char primerChar, char segundoChar, int totalCodePoints) {
        // Segundo char no es low surrogate (error en el stream)
        // Mostrar ambos chars por separado
        System.out.print(primerChar);
        System.out.print(segundoChar);
        totalCodePoints += 2;
        return totalCodePoints;
    }
}