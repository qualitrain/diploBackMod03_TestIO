package mx.com.qtx.mod03TestIO.c_codepoints;


import mx.com.qtx.mod03TestIO.c_codepoints.lectores.Lector01A_Texto_UTF8;
import mx.com.qtx.mod03TestIO.c_codepoints.lectores.Lector01B_TextoISO8859_1;
import mx.com.qtx.mod03TestIO.c_codepoints.lectores.Lector02_CodePoints_UTF8;
import mx.com.qtx.mod03TestIO.c_codepoints.lectores.Lector05_NIO2Simple_UTF8;
import mx.com.qtx.mod03TestIO.c_codepoints.persistores.PersistorISO8859_1;
import mx.com.qtx.mod03TestIO.c_codepoints.persistores.PersistorUTF8;
import mx.com.qtx.mod03TestIO.util.Util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Set;

public class DemoCaracteresYCharset {
	public static final int NO_EXISTE_ARCHIVO = -1;
	public static final int ERROR_GENERICO_ES = -99;
	public static final int EOF = -1;

	public static void main(String[] args) {
        // mostrarProblemaAcentosYemojis();
		// explorarCharsetsDisponibles();
		// explorarCharsets_codificacionVsDecodificacion();

        probarEscrituraYlecturaChars();

		// probarCopiaCharSetDistintos();

		// probarCopiaChasetDefault();
		// probarCopiaCharSetDistintos_Java10();

	}
    private static void mostrarProblemaAcentosYemojis() {
        System.out.println("\nDemoCaracteresYCharset.mostrarProblemaAcentosYemojis" + " -".repeat(20) +"\n");
        String cadEjemplo = "Árbol, Ébano, Tribilín, Ratón, Saúl, Ñoño, emojis:🙂, 🍉, 😵‍💫";

        System.out.println("cadEjemplo = " + cadEjemplo);

        // 1. Generar bytes en diferentes charsets. Desde UTF-16 -manejado internamente por la clase String
        byte[] bytesUTF8 = cadEjemplo.getBytes(StandardCharsets.UTF_8);
        byte[] bytesISO = cadEjemplo.getBytes(StandardCharsets.ISO_8859_1);

        System.out.println("Longitud bytes UTF-8: " + bytesUTF8.length);
        System.out.println("Longitud bytes ISO-8859-1: " + bytesISO.length);
        System.out.println("¿Son iguales? " + Arrays.equals(bytesUTF8, bytesISO));

        // 2. Mostrar diferencia en los bytes
        System.out.println("\nPrimeros 20 bytes UTF-8:");
        for (int i = 0; i < Math.min(20, bytesUTF8.length); i++) {
            System.out.printf("%02X ", bytesUTF8[i] & 0xFF);
        }

        System.out.println("\n\nPrimeros 20 bytes ISO-8859-1:");
        for (int i = 0; i < Math.min(20, bytesISO.length); i++) {
            System.out.printf("%02X ", bytesISO[i] & 0xFF);
        }

        // 3. Probar la decodificación incorrecta
        String decodificadoIncorrecto = new String(bytesUTF8, StandardCharsets.ISO_8859_1);
        System.out.println("\n\nDecodificación incorrecta (UTF-8 como ISO-8859-1):");
        System.out.println(decodificadoIncorrecto);

        // 4. Mostrar los caracteres individualmente
        System.out.println("\nCaracteres en la decodificación incorrecta:");
        for (char c : decodificadoIncorrecto.toCharArray()) {
            System.out.printf("Char: %c - Código: %d%n", c, (int)c);
        }
    }

    private static void probarEscrituraYlecturaChars() {
        System.out.println("\nDemoCaracteresYCharset.probarEscrituraYlecturaChars" + " -".repeat(20) +"\n");

        String cadEjemplo = "Árbol, Ébano, Tribilín, Ratón, Saúl, Ñoño, emojis:🙂, 🍉, 😵‍💫, 👨‍👩‍👧‍👦";
        System.out.println("cadEjemplo = " + cadEjemplo);

        Charset charsetCodifString = getCharsetAleatorio();
        byte[] bytes = cadEjemplo.getBytes(charsetCodifString); // El charset ISO-8859-1 no soporta emojis
        System.out.println("bytes.length = " + bytes.length + ", codificados con " + charsetCodifString.name());

        IPersistorBytesToChars persistor = getPersistorBytesToChras();
        ILectorArcTexto lector = getLectorArcTexto();

        persistor.guardarBytes(bytes,"octetos.txt");
        lector.leerYmostrarCaracteres("octetos.txt");
    }

    private static Charset getCharsetAleatorio() {
//        final int numAleatorio = getNumAleatorio();
//        if(numAleatorio % 2 == 0)
//             return StandardCharsets.ISO_8859_1;
//        else
             return StandardCharsets.UTF_8;
    }

    private static int getNumAleatorio() {
        int numAleatorio = (int)(Math.random() * 1000);
        return numAleatorio;
    }

    private static ILectorArcTexto getLectorArcTexto() {
//        final int numAleatorio = getNumAleatorio();
//        if(numAleatorio % 2 == 0)
//            return new Lector01B_TextoISO8859_1();
//        else
//            return new Lector01A_Texto_UTF8();                // Manejo "afortunado" de emoticones y codepoints

//          return new Lector02_CodePoints_UTF8();  // No une con emoticones compuestos
//          return new Lector03_CodePointsDidactico_UTF8(); // No une emoticones compuestos. Muestra ZWJ (Zero Width Joiner, U+200D)
//          return new Lector04_CodePointsConZWJ_UTF8();    // No une emoticones compuestos. Muestra ZWJ (Zero Width Joiner, U+200D). Rebuscado
          return new Lector05_NIO2Simple_UTF8();          // Resuelve bien los emoticones simples y compuestos
//          return new Lector06_NIO2xLinea_UTF8();          // Resuelve bien los emoticones simples y compuestos
//          return new Lector07_CodePointsBuffered_UTF8();    // Lee con mayor eficiencia anidando un buffer. Usa decorador
    }

    private static IPersistorBytesToChars getPersistorBytesToChras() {
//        final int numAleatorio = getNumAleatorio();
//        if(numAleatorio % 2 == 0)
//            return new PersistorISO8859_1();
//        else
            return new PersistorUTF8();
    }

    public static void explorarCharsets_codificacionVsDecodificacion() {
        System.out.println("\n------------- Demo codificación y decodificación de caracteres -------------");
		Charset charset_Iso8859_1 = StandardCharsets.ISO_8859_1;
		Charset charset_Utf8 = StandardCharsets.UTF_8;
		
		String letras="ABCDEFGHIXYZÁÉÍÓÚÑ";
        System.out.println("letras = " + letras+ "\n");
		
		for(int i=0; i<letras.length(); i++) {
			String caracterI = letras.charAt(i) + "";

			System.out.printf("Charset para codificación:%11s, caracter desde String(UTF-16):%s, ", charset_Iso8859_1, caracterI);
			byte[] bytesCodificados = codificarCaracterDesdeString(charset_Iso8859_1, caracterI);
            mostrarBytes(bytesCodificados);
            System.out.printf("bytes decodificados con %11s:[%s]\n", charset_Iso8859_1, decodificarBytes(charset_Iso8859_1, bytesCodificados));
            System.out.printf("bytes decodificados con %11s:[%s]\n", charset_Utf8, decodificarBytes(charset_Utf8, bytesCodificados));
            System.out.println();
            
            System.out.printf("Charset para codificación:%11s, caracter desde String(UTF-16):%s, ", charset_Utf8, caracterI);
			bytesCodificados = codificarCaracterDesdeString(charset_Utf8, caracterI);		 	
            mostrarBytes(bytesCodificados);
            System.out.printf("bytes decodificados con %11s:[%s]\n", charset_Utf8, decodificarBytes(charset_Utf8, bytesCodificados));
            System.out.printf("bytes decodificados con %11s:[%s]\n", charset_Iso8859_1, decodificarBytes(charset_Iso8859_1, bytesCodificados));
            
            System.out.println("------------------------------------------------------------------------");
		}
	}

	public static void explorarCharsetsDisponibles() {
		 System.out.println("\n------------- Charset por defecto y otros Charsets disponibles -------------");
		 Charset charsetDefault = Charset.defaultCharset();
		 System.out.println("El Charset por defecto es " + charsetDefault);

		 Set<String> grupoAliases = charsetDefault.aliases();
		 System.out.println("Los aliases o sinónimos de " 
		         + charsetDefault.displayName() + " son:");
		 for(String aliasI : grupoAliases){
			 System.out.println(aliasI);
		 }
		 
		 System.out.println("\n Hay " + Charset.availableCharsets().size()
		 		+ " Charsets disponibles y son:");		 
		 for(String charsetI : Charset.availableCharsets().keySet()){
			 System.out.println("----" + charsetI + "----");
			 System.out.println("  Sus aliases o sinónimos son:");
			 for(String aliasI : Charset.availableCharsets().get(charsetI).aliases()){
				 System.out.println("\t"+ aliasI);
			 }
		 }
	}

	private static byte[] codificarCaracterDesdeString(Charset charsetDestino, String caracter) {
        ByteBuffer byteBuffer = charsetDestino.encode(caracter);
        // Crear un array del tamaño exacto (los bytes entre la posición y el límite) para evita que se cuelen nulos al final
        byte[] bytesCodificados = new byte[byteBuffer.remaining()];
        byteBuffer.get(bytesCodificados);
        return bytesCodificados;
	}

	private static String decodificarBytes(Charset charsetOrigen, byte[] bytesCodificados) {
//		ByteBuffer byteBuffer = ByteBuffer.wrap(bytesCodificados);
//		CharBuffer charBuffer = charsetOrigen.decode(byteBuffer);
//		String cad = charBuffer.toString();  // Conversión a UTF-16
        String cad = new String(bytesCodificados,charsetOrigen);
        System.out.println("cad.length() = " + cad.length());
        System.out.println("cad.codePoints().count() = " + cad.codePoints().count());
		return cad;
	}

	private static void mostrarBytes(byte[] bytesCodificados) {
		System.out.print("bytes:[");
		for(byte byteI:bytesCodificados) {
			int valCaracter = byteI & 0xff; //Apaga el bit de signo los de complemento-2
			System.out.print(valCaracter + " ");
		}
		System.out.println("]");
	}

	public static void probarCopiaCharSetDistintos() {
		System.out.println("\n------------- Probar copia de un Charset a otro -------------");
		String nombreArcEntrada = "patitoUtf8.txt";
		String nombreSalida = "patitoIso8859_1_b.txt";
		
		
		String nombreCharsetActivo = Charset.defaultCharset().displayName();
		System.out.println("Charset activo:" + nombreCharsetActivo);
		
		Charset charsetIso8859_1 = StandardCharsets.ISO_8859_1;
		Charset charsetUtf8 = StandardCharsets.UTF_8;
		
		long nCharsCopiados = copiarArchivoCharXChar(nombreArcEntrada,charsetUtf8, nombreSalida, charsetIso8859_1);
		if(nCharsCopiados > 0) {
			System.out.println("Fin exitoso. Se copiaron " + nCharsCopiados + " caracteres de " 
						+ nombreArcEntrada + " a " + nombreSalida);
		}
	}
	
	public static void probarCopiaCharSetDistintos_Java10() {
		System.out.println("\n------------- Probar copia de un Charset a otro, con transfer-------------");
		String nombreArcEntrada = "patitoUtf8.txt";
		String nombreSalida = "patitoIso8859_1_b.txt";

		String nombreCharsetActivo = Charset.defaultCharset().displayName();
		System.out.println("Charset activo:" + nombreCharsetActivo);
		
//		Charset charsetIso8859_1 = Charset.forName("ISO-8859-1");
//		Charset charsetUtf8 = Charset.forName("UTF-8");

        Charset charsetIso8859_1 = StandardCharsets.ISO_8859_1;
        Charset charsetUtf8 = StandardCharsets.UTF_8;
		
		long nCharsCopiados = copiarArchivoCharXCharJava10(nombreArcEntrada,charsetUtf8, nombreSalida, charsetIso8859_1);
		if(nCharsCopiados > 0) {
			System.out.println("Fin exitoso. Se copiaron " + nCharsCopiados + " caracteres de " 
						+ nombreArcEntrada + " a " + nombreSalida);
		}
	}

	private static long copiarArchivoCharXCharJava10(String nomArcFte, Charset charsetFte, String nomArcDest,
			                                         Charset charsetDest) {
		long nCaracteresCopiados = 0;
		
		try( FileReader arcEntrada = new FileReader(nomArcFte, charsetFte); 
		     FileWriter arcSalida = new FileWriter(nomArcDest, charsetDest)	){	
			nCaracteresCopiados = arcEntrada.transferTo(arcSalida);
		} 
		catch (FileNotFoundException e) {
			System.out.println("No existe archivo [" + e.getMessage() + "]");
			return NO_EXISTE_ARCHIVO;
		} 
		catch (Exception e) {
			System.out.println("Error de E/S [" + e.getClass().getName() + ":"
			        + e.getMessage() 
					+ Util.getCausa(e) + "]");
				return ERROR_GENERICO_ES;
		}	
		return nCaracteresCopiados;
	}	
	
	public static void probarCopiaChasetDefault() {
		System.out.println("\n------------- Probar copia de un archivo a otro con el mismo Charset -------------");
		String nombreArcEntrada = "patitoUtf8.txt";
		String nombreSalida = "textoSalidaUtf8.txt";
		
		String nombreCharsetActivo = Charset.defaultCharset().displayName();
		System.out.println("Charset activo:" + nombreCharsetActivo);
		
		long nCharsCopiados = copiarArchivoCharXChar(nombreArcEntrada, nombreSalida);
		if(nCharsCopiados > 0) {
			System.out.println("Fin exitoso. Se copiaron " + nCharsCopiados + " caracteres de " 
						+ nombreArcEntrada + " a " + nombreSalida);
		}
	}
		
	private static long copiarArchivoCharXChar(String nombreArcEntrada, String nombreSalida) {
		Charset charsetLocal = Charset.defaultCharset();
		return copiarArchivoCharXChar(nombreArcEntrada,charsetLocal,nombreSalida,charsetLocal);
	}

	public static long copiarArchivoCharXChar(String nomArcFte, Charset charsetFte, 
			                                  String nomArcDest, Charset charsetDest) {
		long nCaracteresCopiados = 0;
		
		try( FileReader arcEntrada = new FileReader(nomArcFte, charsetFte); 
		     FileWriter arcSalida = new FileWriter(nomArcDest, charsetDest)	){	
			int c;			
        	while(true){
        		c = arcEntrada.read();
        		if (c == EOF)
        			break;
        		arcSalida.write(c);
        		nCaracteresCopiados++;
        	}			
		} 
		catch (FileNotFoundException e) {
			System.out.println("No existe archivo [" + e.getMessage() + "]");
			return NO_EXISTE_ARCHIVO;
		} 
		catch (IOException e) {
			System.out.println("Error de E/S [" + e.getClass().getName() + ":"
			        + e.getMessage() 
					+ Util.getCausa(e) + "]");
				return ERROR_GENERICO_ES;
		}	
		return nCaracteresCopiados;
	}
	
	
}
