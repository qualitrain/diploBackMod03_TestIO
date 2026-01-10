package mx.com.qtx.mod03TestIO.d_nio;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Nio01_PathEsVirtual {
    public static void main(String[] args) {
        // probarRutaInexistente();
        try {
            probarRutaNormalizada();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void probarRutaNormalizada() throws IOException {
        Path ruta = Paths.get("..","..", "2026", "..","..");
        System.out.println("Files.exists(ruta) = " + Files.exists(ruta));
        System.out.println("ruta.toString() = " + ruta.toString());
        System.out.println("ruta.toAbsolutePath() = " + ruta.toAbsolutePath());

        Path rutaNormalizada = ruta.normalize();
        System.out.println("Files.exists(rutaNormalizada) = " + Files.exists(rutaNormalizada));
        System.out.println("rutaNormalizada.toString() = " + rutaNormalizada.toString());
        System.out.println("rutaNormalizada.toAbsolutePath() = " + rutaNormalizada.toAbsolutePath());

        Path rutaReal = rutaNormalizada.toRealPath(LinkOption.NOFOLLOW_LINKS);
        System.out.println("Files.exists(rutaReal) = " + Files.exists(rutaReal));
        System.out.println("rutaReal.toString() = " + rutaReal.toString());
        System.out.println("rutaReal.toAbsolutePath() = " + rutaReal.toAbsolutePath());
    }

    private static void probarRutaInexistente() {
        Path ruta = Paths.get("c:" + File.separator + "temporal");
        System.out.println("ruta.toAbsolutePath() = " + ruta.toAbsolutePath());
        System.out.println("ruta.toString() = " + ruta.toString());
        System.out.println("Files.exists(ruta) = " + Files.exists(ruta));

        Path ruta2 = Paths.get("ejercicios" + File.separator + "temporal");
        System.out.println("\nruta2.toAbsolutePath() = " + ruta2.toAbsolutePath());
        System.out.println("ruta2.toString() = " + ruta2.toString());
        System.out.println("Files.exists(ruta) = " + Files.exists(ruta2));

        Path ruta3 = Paths.get("src");
        System.out.println("\nruta3.toAbsolutePath() = " + ruta3.toAbsolutePath());
        System.out.println("ruta3.toString() = " + ruta3.toString());
        System.out.println("Files.exists(ruta) = " + Files.exists(ruta3));

        Path ruta4 = Paths.get("src","main","java");
        System.out.println("\nrut4.toAbsolutePath() = " + ruta4.toAbsolutePath());
        System.out.println("ruta4.toString() = " + ruta4.toString());
        System.out.println("Files.exists(ruta) = " + Files.exists(ruta4));

        Path ruta5 = Paths.get("");
        System.out.println("\nruta5.toAbsolutePath() = " + ruta5.toAbsolutePath());
        System.out.println("ruta5.toString() = " + ruta5.toString());
        System.out.println("Files.exists(ruta) = " + Files.exists(ruta5));

        Path rutaResuelta = ruta.resolve("tramo2");
        System.out.println("\nrutaResuelta.toAbsolutePath() = " + rutaResuelta.toAbsolutePath());
        System.out.println("rutaResuelta.toString() = " + rutaResuelta.toString());
        System.out.println("Files.exists(rutaResuelta) = " + Files.exists(rutaResuelta));

        Path rutaResuelta2 = ruta.resolve(Paths.get("tramo1","tramo2","tramo3"));
        System.out.println("\nrutaResuelta2.toAbsolutePath() = " + rutaResuelta2.toAbsolutePath());
        System.out.println("rutaResuelta2.toString() = " + rutaResuelta2.toString());
        System.out.println("Files.exists(rutaResuelta2) = " + Files.exists(rutaResuelta2));
    }
}
