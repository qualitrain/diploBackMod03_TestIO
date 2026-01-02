package mx.com.qtx.mod03TestIO;

import java.io.*;

public class EjemploStreamsIO {
    public static void main(String[] args) {
        String nomArcImagen = "imagen.jpg";
        String nomMediaImagen = "mediaImagen.jpg";

        copiarMediaImagen(nomArcImagen,nomMediaImagen);
    }

    private static void copiarMediaImagen(String nomArcImagen, String nomMediaImagen) {
        File fileImg = new File(nomArcImagen);
        long tamanioImg = fileImg.length();
        long tamanioMediaImg = tamanioImg / 2;
        System.out.println("tamaño Imagen = " + tamanioImg);

        try(FileInputStream img = new FileInputStream(nomArcImagen);
            FileOutputStream mediaImg = new FileOutputStream(nomMediaImagen)){
            byte[] contenidoImagen = new byte[Math.toIntExact(tamanioMediaImg + 2)];
            img.read(contenidoImagen,0, (int) tamanioMediaImg);
            mediaImg.write(contenidoImagen);
        }
        catch (FileNotFoundException e) {
            System.out.println(e.getMessage());;
        }
        catch (IOException e) {
            System.out.println(e.getClass().getName() + ":" + e.getMessage());
        }
    }
}
