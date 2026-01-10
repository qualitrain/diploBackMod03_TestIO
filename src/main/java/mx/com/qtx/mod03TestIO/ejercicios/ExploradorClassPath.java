package mx.com.qtx.mod03TestIO.ejercicios;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Stream;

public class ExploradorClassPath {
    public static final String KEY_PROP_RUTA_CLASES = "java.class.path";
    public static final String NOM_ARC_LISTA_CLASES = "clases.txt";
    public static final String CARPETA_DESTINO = "C:\\qtx\\temporal\\borrame";

    public static void main(String[] args) {
        generarReporteDeClases();
    }

    private static void generarReporteDeClases() {
        final Path rutaClases = Paths.get(System.getProperty(KEY_PROP_RUTA_CLASES));
        final Path rutaCarpetaDestino = getRutaCarpetaDestinoValida();
        final Path rutaArchivo = getRutaArchivoValido(rutaCarpetaDestino);

        List<String> lineasArc = leerLineasArchivoClases(rutaClases);
        lineasArc = agregarTitulosAReporte(lineasArc, rutaClases);

        salvarReporteClases(rutaArchivo, lineasArc);

        System.out.println("Archivo " + rutaArchivo + " Creado");

    }

    private static Path getRutaArchivoValido(Path rutaCarpetaDestino) {
        Path rutaArchivo = rutaCarpetaDestino.resolve(NOM_ARC_LISTA_CLASES);
        if(Files.exists(rutaArchivo)){
            rutaArchivo = getNuevaRutaArchivo(rutaCarpetaDestino);
        }
        return rutaArchivo;
    }

    private static Path getRutaCarpetaDestinoValida() {
        Path rutaCarpetaDestino = Paths.get(CARPETA_DESTINO);
        if(Files.exists(rutaCarpetaDestino) == false){
            throw new RuntimeException("No existe la carpeta destino " + rutaCarpetaDestino.toString());
        }
        if(Files.isWritable(rutaCarpetaDestino) == false){
            throw new RuntimeException("No se tienen permisos para escribir en la carpeta destino " + rutaCarpetaDestino.toString());
        }
        return rutaCarpetaDestino;
    }

    private static void salvarReporteClases(Path rutaArchivo, List<String> lineasArc) {
        try {
            Files.write(rutaArchivo, lineasArc);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Path getNuevaRutaArchivo(Path rutaCarpetaDestino) {
        Path rutaArchivo;
        String nomSinExt = getNombreArcSinExtension(NOM_ARC_LISTA_CLASES);
        String ext = getExtensionArchivo(NOM_ARC_LISTA_CLASES);

        rutaArchivo = rutaCarpetaDestino.resolve(nomSinExt
                + LocalDateTime.now().toString().replaceAll(":","_")
                                                .replaceAll("\\.","_")
                + "." + ext);
        return rutaArchivo;
    }

    private static List<String> agregarTitulosAReporte(List<String> lineasArc, Path rutaClases) {
        lineasArc = new ArrayList<>(lineasArc);

        lineasArc.add(0,"ruta general para todas las clases: " + rutaClases);
        lineasArc.add(1,"");
        lineasArc.add(2,String.format("%-80s    %s","Ruta relativa clase","Ult. Modificación"));
        lineasArc.add(3,"=".repeat(80) + "    " + "=".repeat(28));
        return lineasArc;
    }

    private static List<String> leerLineasArchivoClases(Path rutaClases) {
        List<String> lineasArc;
        try (Stream<Path> streamDirs = Files.walk(rutaClases, FileVisitOption.FOLLOW_LINKS)){
            lineasArc = streamDirs
                          .filter(path -> path.toString().endsWith(".class"))
                          .map(path -> {
                            try {
                                return List.of(
                                        path.toString().replace(rutaClases.toString(), ""),
                                        Files.getLastModifiedTime(path, LinkOption.NOFOLLOW_LINKS).toString());
                            }
                            catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                          })
                          .map(lstI->String.format("%-80s -> %s", lstI.getFirst(), lstI.get(1)))
                          .toList();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        return lineasArc;
    }

    private static String getNombreArcSinExtension(String nomArchivo){
        int posExt = nomArchivo.indexOf(".");
        return nomArchivo.substring(0,posExt);
    }

    private static String getExtensionArchivo(String nomArchivo){
        int posExt = nomArchivo.indexOf(".");
        return nomArchivo.substring(posExt + 1);

    }

    private static void mostrarPropsSystem() {
        Properties props = System.getProperties();
        props.stringPropertyNames().stream()
                .sorted()
                .map(k->String.format("%-25s : %-30s", k, props.getProperty(k)))
                .forEach(System.out::println);
    }

    private static void mostrarVariablesAmbiente() {
        System.getenv().entrySet().stream()
                .sorted((o1, o2) -> o1.getKey().compareTo(o2.getKey()))
                .map(esI-> String.format("%-15s : %-30s", esI.getKey(), esI.getValue()))
                .forEach(System.out::println);
    }
}
