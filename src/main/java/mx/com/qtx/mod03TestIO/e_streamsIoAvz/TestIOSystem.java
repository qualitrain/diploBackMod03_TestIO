package mx.com.qtx.mod03TestIO.e_streamsIoAvz;

import java.util.Scanner;
import java.util.regex.Pattern;

public class TestIOSystem {
    public static void main(String[] args) {
        System.out.println("Hola");
 //       System.err.println("Oops, me equivoqué"); // Escritura en salida estándar de error
 //       test_lecturaPorPalabra();
        test_lecturaPorLinea();
    }

    private static void test_lecturaPorLinea() {
        try(Scanner lector = new Scanner(System.in)){
            Pattern delimitador = lector.delimiter();
            System.out.println("delimitador.toString() = " + delimitador.toString());

            lector.useDelimiter("\n");
            delimitador = lector.delimiter();
            System.out.println("delimitador.toString() = " + delimitador.toString());

            System.out.println("Alex, escribe algo:");
            while(lector.hasNext()){
                String token = lector.next();
                System.out.println("token = " + token);
            }
        }
    }

    private static void test_lecturaPorPalabra() {
        try(Scanner lector = new Scanner(System.in)){
            System.out.println("Alex, escribe algo:");
            while(lector.hasNext()){
                String token = lector.next();
                System.out.println("token = " + token);
            }
        }
    }
}
