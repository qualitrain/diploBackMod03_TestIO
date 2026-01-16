package mx.com.qtx.mod03TestIO.e_streamsIoAvz;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class DemoSalidaFormateada {
	// ejemplo de escritura formateada usando el m�todo format de PrintWriter, que tambien existe
	// en PrintStream. Este m�todo es intercambiable por printf -inspirado en el de lenguaje C-
	public static void main(String[] args)throws IOException {
		String nombreArchivoSalida = "salidaFormat.txt";

        try (PrintWriter flujoSalida = new PrintWriter(new FileWriter(nombreArchivoSalida)) ){

            String nombre = "Federico Garcia Lorca";
            String direccion = "Av. Insurgentes Sur 1580, col. Guadalupe Inn";
            for(int i=1;i<10;i++){
            	double doble1 = i * 3.141592654;
            	long numerote = i * 123456789;
            	flujoSalida.format("%06d - %-30s - %55s - %7.2f - %-12f - %14d\n",
            			           i,nombre,direccion,doble1,doble1,numerote); // imprime  lineas formateadaS
            }
    		System.out.println("Fin exitoso de la generacion de " + nombreArchivoSalida);           
        } 
        catch(Exception ex) {
        	System.out.println("Error:" + ex.getClass().getName() + " [" + ex.getMessage() + "]");
        } 

	}

}
