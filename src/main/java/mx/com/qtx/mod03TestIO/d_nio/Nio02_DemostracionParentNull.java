package mx.com.qtx.mod03TestIO.d_nio;

import java.nio.file.*;

public class Nio02_DemostracionParentNull {
    public static void main(String[] args) {
        System.out.println("=== DEMOSTRACIÓN DE getParent() ===\n");
        
        // Ruta relativa simple
        Path[] rutas = {
            Paths.get("archivo.txt"),          // null
            Paths.get("carpeta/archivo.txt"),  // "carpeta"
            Paths.get("a/b/c/archivo.txt"),    // "a/b/c"
            Paths.get("."),                    // null
            Paths.get(".."),                   // null
            Paths.get(""),                     // null
            Paths.get("/"),                    // null (raíz)
            Paths.get("/usr"),                 // "/"
            Paths.get("/usr/bin"),             // "/usr"
            Paths.get("C:\\"),                 // null (raíz Windows)
            Paths.get("C:\\Windows"),          // "C:\"
            Paths.get("C:\\Windows\\System32"), // "C:\Windows"
            Paths.get("../otro"),
            Paths.get("../otro").toAbsolutePath()
        };
        
        for (Path rutaI : rutas) {
            Path parent = rutaI.getParent();
            System.out.printf("%-25s -> %s\n",
                    rutaI.toString(),
                    parent != null ? "'" + parent + "'" : "null"
            );
        }
    }
}