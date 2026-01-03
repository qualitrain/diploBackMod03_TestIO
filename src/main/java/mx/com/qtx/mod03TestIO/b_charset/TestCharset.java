package mx.com.qtx.mod03TestIO.b_charset;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class TestCharset {
    public static void main(String[] args) {
        testBasicosDeCharset();
        testCharsetsDisponibles();
    }

    private static void testCharsetsDisponibles() {
        Charset.availableCharsets().forEach((k,v)->{
            System.out.println("k + v = " + k + ":" + v);
        });

        System.out.println("\nAliases del " +  StandardCharsets.ISO_8859_1);
        StandardCharsets.ISO_8859_1.aliases().forEach(cdI->{
            System.out.println("cdI = " + cdI);
        });
    }

    private static void testBasicosDeCharset() {
        Charset charsetUtf8 = StandardCharsets.UTF_8;
        Charset charsetISO_8859_1 = StandardCharsets.ISO_8859_1;

        Charset charsetActual = Charset.defaultCharset();
        System.out.println("charsetActual = " + charsetActual);


    }
}
