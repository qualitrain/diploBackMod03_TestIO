package mx.com.qtx.mod03TestIO.c_codepoints.lectores;

import mx.com.qtx.mod03TestIO.c_codepoints.ILectorArcTexto;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Lector01A_Texto_UTF8 implements ILectorArcTexto {
    private static final int EOF = -1;

    @Override
    public void leerYmostrarCaracteres(String file) {
        System.out.println("\n" +
                this.getClass().getSimpleName() +
                ".leerYmostrarCaracteres");
        try(FileReader fr = new FileReader(file, StandardCharsets.UTF_8)){

            while(true) {
                int carac = fr.read();
                if (carac == EOF){
                    System.out.println(": decodificados con UTF-8");
                    break;
                }

                System.out.print(Character.toString(carac));
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("Oops no existe el archivo [" + e.getMessage() + "]");
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
