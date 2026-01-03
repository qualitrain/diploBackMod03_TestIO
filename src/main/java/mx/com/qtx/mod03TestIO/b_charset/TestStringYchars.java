package mx.com.qtx.mod03TestIO.b_charset;

public class TestStringYchars {
    public static void main(String[] args) {
        String saludo = "Feliz año 🙂👍. Serás un campeón";

        System.out.println("saludo = " + saludo);
        
        System.out.println("saludo.length() = " + saludo.length());
        System.out.println("saludo.getBytes().length = " + saludo.getBytes().length);
        System.out.println("saludo.codePoints().count() = " + saludo.codePoints().count());
    }
}
