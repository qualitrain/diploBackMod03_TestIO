package mx.com.qtx.mod03TestIO.d_nio;

import java.io.File;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.PosixFileAttributes;
import java.text.SimpleDateFormat;

public class Nio04_DemoNio {
    public static void main(String[] args) {
    //    String nomArchivo = "patitoUtf8.txt";
        String nomArchivo = "pom.xml";


        File file = new File(nomArchivo);

        Path ruta = file.toPath();

        explorarPath(ruta);

    }
    private static void explorarPath(Path ruta) {
        System.out.println("\n **** Explorando Path de " + ruta.toString() + "****");

        String dirPadre = ruta.toAbsolutePath()
                              .getParent()
                              .toString();
        System.out.println("directorio Padre = " + dirPadre);

        // Verificar si la ruta existe
        if (!Files.exists(ruta)) {
            System.out.println("La ruta no existe.");
            return;
        }

        try {
            // Obtener los atributos básicos del archivo
            BasicFileAttributes atributos = Files.readAttributes(ruta, BasicFileAttributes.class);

            // Mostrar información básica
            System.out.println("Ruta absoluta: " + ruta.toAbsolutePath());
            System.out.println("Directorio padre: " + ruta.toAbsolutePath().getParent());
            System.out.println("Tipo: " + (atributos.isDirectory() ? "Directorio" : "Archivo"));
            System.out.println("Tamaño: " + (atributos.isDirectory() ? "N/A" : atributos.size() + " bytes"));
            System.out.println("Oculto: " + Files.isHidden(ruta));
            System.out.println("Última modificación: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(atributos.lastModifiedTime().toMillis()));


            // Mostrar los permisos detallados
            System.out.println("Permisos:");
            System.out.println("  Lectura: " + Files.isReadable(ruta));
            System.out.println("  Escritura: " + Files.isWritable(ruta));
            System.out.println("  Ejecución: " + Files.isExecutable(ruta));

            // Contar subdirectorios y archivos en caso de directorios
            if (atributos.isDirectory()) {
                int subdirectorios = 0;
                int archivos = 0;
                try (DirectoryStream<Path> stream = Files.newDirectoryStream(ruta)) {
                    for (Path subruta : stream) {
                        if (Files.isDirectory(subruta)) {
                            subdirectorios++;
                        } else if (Files.isRegularFile(subruta)) {
                            archivos++;
                        }
                    }
                }
                System.out.println("Subdirectorios: " + subdirectorios);
                System.out.println("Archivos: " + archivos);
            }

            FileSystem fileSystem = ruta.getFileSystem();
            System.out.println("FileSystem provider:" + fileSystem.provider().getClass().getName());
            System.out.println("FileSystem separator:" + fileSystem.getSeparator());

            System.out.println("FileStores:");
            for( FileStore fs:fileSystem.getFileStores()) {
                System.out.println("\tnombre:" + fs.name());
                System.out.println("\ttipo:" + fs.type());    		}


            try {
                // Obtener los atributos de seguridad
                PosixFileAttributes atributosSeguridad = Files.readAttributes(ruta, PosixFileAttributes.class);

                // Mostrar propietario del archivo
                System.out.println("Propietario: " + atributosSeguridad.owner().getName());
            }
            catch(UnsupportedOperationException uoEx) {
                System.out.println("Operación Files.readAttributes(ruta, PosixFileAttributes.class) no soportada " + uoEx.getClass().getName() );
            }
        }
        catch (Exception e) {
            System.out.println("Error inesperado: " + e.getMessage());
        }
    }

}
