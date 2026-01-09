package mx.com.qtx.mod03TestIO.b_charset;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class TestCharset {
    public static void main(String[] args) {
        /*
        testBasicosDeCharset();
        testCharsetsDisponibles();
         */

        codificarCaracterDesdeString(StandardCharsets.ISO_8859_1,"A");
        System.out.println();
        codificarCaracterDesdeString(StandardCharsets.ISO_8859_1,"Á");
        System.out.println();
        codificarCaracterDesdeString(StandardCharsets.ISO_8859_1,"😁");
        System.out.println();
        System.out.println("-".repeat(50));
        codificarCaracterDesdeString(StandardCharsets.UTF_8,"A");
        System.out.println();
        codificarCaracterDesdeString(StandardCharsets.UTF_8,"Á");
        System.out.println();
        codificarCaracterDesdeString(StandardCharsets.UTF_8,"😁");
        
         /*
        byte n = -1;
        System.out.println("n = " + Integer.toBinaryString(n));
        n = (byte) 128;
        System.out.println("n = " + Integer.toBinaryString(n));
    */
    }

    private static void testCharsetsDisponibles() {
        Charset.availableCharsets().forEach((k, v) -> {
            System.out.println("k + v = " + k + ":" + v);
        });

        System.out.println("\nAliases del " + StandardCharsets.ISO_8859_1);
        StandardCharsets.ISO_8859_1.aliases().forEach(cdI -> {
            System.out.println("cdI = " + cdI);
        });
    }

    private static void testBasicosDeCharset() {
        Charset charsetUtf8 = StandardCharsets.UTF_8;
        Charset charsetISO_8859_1 = StandardCharsets.ISO_8859_1;

        Charset charsetActual = Charset.defaultCharset();
        System.out.println("charsetActual = " + charsetActual);
    }

    private static void codificarCaracterDesdeString(Charset charsetDestino, String caracter) {
        ByteBuffer buffer = charsetDestino.encode(caracter);
        byte[] bytesCodificados = buffer.array();
        System.out.print("Charset:" + charsetDestino
                + ", caracter a codificar con origen String(UTF-16):" + caracter
                + ", cantidad de bytes producidos:" + bytesCodificados.length
                + ", bytes:[");
        for(byte byteI:bytesCodificados) {
            int valCaracter = byteI & 0xff; //Apaga el bit de signo y rellena con ceros a la izquierd
           System.out.print(valCaracter + " ");
    //        System.out.print(byteI + " ");
        }
        System.out.println("]");

    }
}
/*
 1   en binario = 0000 0001
 2   en binario = 0000 0010
 127 en binario = 0111 1111
 128 en binario = 1000 0000

                   0000 0000 0000 0000 1000 0000
 */