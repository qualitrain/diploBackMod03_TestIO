package mx.com.qtx.mod03TestIO.ejercicios;

import mx.com.qtx.mod03TestIO.f_regex.CapitalizadorSimple;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CapturadorClientes {
    private static final Pattern PATRON_FECHA_BASICO =
            Pattern.compile("^" +
                    "(\\d{4})" +
                    "/" +
                    "(\\d{2})" +
                    "/" +
                    "(\\d{2})" +
                    "$");

    // Regex con nombres de grupos )
    private static final String CAD_REGEX_FECHA_CON_GPOS_NOMBRADOS =
                    "^" +                                      // Inicio
                    "(?<anio>20\\d{2}|19\\d{2})" +             // Grupo denominado "anio", formado por 2 alternativas: a) el veinte y 2 dígitos cualquiera, b) el diecinueve y 2 dígitos alternativos
                    "/" +                                      // Diagonal literal usada en las fechas
                    "(?<mes>0[1-9]|1[0-2])" +                  // Grupo denominado "mes", formado por 2 alternativas: a) el cero seguido por un dígito del 1 al 9, b) el uno seguido por un dígito del 0 al 2
                    "/" +                                      // Diagonal literal usada en las fechas
                    "(?<dia>0[1-9]|[12]\\d|3[01])" +           // Grupo denominado "dia", formado por 3 alternativas: a) el cero seguido por un dígito del 1 al 9, b) el uno o el dos seguido por un dígito cualquiera, c) el tres seguido  por un dígito cero o uno
                    "$";                                       // Final

    private static final Pattern PATRON_FECHA_CON_GPOS_NOMBRADOS = Pattern.compile(CAD_REGEX_FECHA_CON_GPOS_NOMBRADOS);

    // Regex nombres de personas. El texto debe tener al menos 2 caracteres, empezar con una palabra (que puede tener partes con guión o apóstrofe), luego tener al menos un espacio y otra palabra similar, y nada más después.
    private static final String CAD_REGEX_NOMBRE_PERSONA =
            "^" +
            "(?=.{2,})" +             // Lookahead: al menos 2 caracteres en total. Significa "Primero verifica que haya al menos 2 caracteres en total, pero no cuentes esta verificación como parte del nombre"
                                         // (    Abre un "grupo de condiciones" (solo para verificar algo)
                                         // ?=   Significa "verifica que lo que sigue sea verdad, pero no lo cuentes como parte del nombre"
                                         // .    Significa "Cualquier caracter" (letra, número, símbolo, espacio)
                                         // {2,} Significa "Dos o más veces"
                                         // )    Cierra el grupo de condiciones

            "(?:\\p{L}+" +            // Primera palabra (puede tener guiones/apóstrofes) Significa "Puede haber (o no) partes que empiecen con guión o apóstrofe seguido de letras, y esto puede pasar varias veces o ninguna".
                                         //  (?:  Significa "Empieza un grupo que NO voy a recordar después". En regex normal, los () guardan lo que encuentran para usarlo después. Con ?: adentro, dice: "agrúpalo pero no lo guardes".
                                         // \p{L} Significa "Cualquier letra de cualquier idioma". Incluye: A, B, C, á, é, í, ñ, ç, etc. La L significa "Letter" (letra)
                                         // +     Significa "Una o más veces". Entonces \p{L}+ significa "Una o más letras"
                "(?:[-']\\p{L}+)" +      // (?:   Significa Otro grupo que no se guarda.
                                         // [-'] = "Un guión - O un apóstrofe '".
                                         // \p{L}+ = "Una o más letras" (igual que antes).

            "*)" +                    // )*  Significa "Este grupo completo puede repetirse CERO o más veces". (?:\p{L}+(?:[-']\p{L}+)*) significa "Una o más letras, que pueden estar seguidas (o no) de partes con guión o apóstrofe y más letras"

            "(?:\\s+" +               // Una o más palabras adicionales. (?:\s+(?:\p{L}+(?:[-']\p{L}+)*))+ significa:Debe haber: espacio(s) seguido de una palabra (con posibles guiones/apóstrofes), y esto de 'espacio + palabra' debe repetirse UNA o MÁS veces
                                         // \s Significa "Cualquier espacio en blanco" (espacio normal, tabulación, salto de línea)
                "(?:\\p{L}+" +
                    "(?:[-']\\p{L}+)" +
                "*)" +
            ")+" +
            "$";

    private static final Pattern PATRON_NOMBRE_PERSONA = Pattern.compile(CAD_REGEX_NOMBRE_PERSONA);

    public static void main(String[] args) {
        try(Scanner lectorConsola = new Scanner(System.in)) {
            lectorConsola.useDelimiter("\\n");
            List<Cliente> lstCtes = capturarClientes(lectorConsola, 5);
            lstCtes.forEach(System.out::println);
        }
    }

    private static List<Cliente> capturarClientes(Scanner lectorConsola, int nCtes) {
        List<Cliente> lstCtes = new ArrayList<>();
        for(int i=0; i<nCtes; i++){
            Cliente cteI = capturarCliente(lectorConsola);
            if(cteI == null)
                break;
            lstCtes.add(cteI);
        }
        return lstCtes;
    }

    private static Cliente capturarCliente(Scanner lectorConsola) {
        int id = getIdCteValido(lectorConsola);
        if(id == 0){
            return null;
        }
        String nombre = getNombreCteValido(lectorConsola);
        LocalDate fecNacCte = getFecNacCteValida(lectorConsola);
        return new Cliente(id,nombre,fecNacCte);
    }

    private static LocalDate getFecNacCteValida(Scanner lectorConsola) {
        System.out.println("Teclee la fecha de nacimiento del cliente en formato: AAAA/MM/DD");
        while(true) {
            System.out.print("Fecha de necimiento:");
            String cadFecNac = lectorConsola.next();
            List<String> lstErrs = validarFecNac(cadFecNac);
            if (lstErrs.isEmpty()) {
                return obtenerLocalDateDe(cadFecNac);
            }
            lstErrs.forEach(System.out::println);
        }
    }

    private static LocalDate obtenerLocalDateDe(String cadFecNac) {
        Matcher matcher = PATRON_FECHA_CON_GPOS_NOMBRADOS.matcher(cadFecNac);
        matcher.matches();
        int anio = Integer.parseInt(matcher.group("anio"));
        int mes = Integer.parseInt(matcher.group("mes"));
        int dia = Integer.parseInt(matcher.group("dia"));
        return LocalDate.of(anio, mes, dia);
    }

    private static List<String> validarFecNac(String cadFecNac) {
        List<String> errores = new ArrayList<>();
        if(cadFecNac.trim().isEmpty()){
            errores.add("✗ La Fecha está vacía");
            return errores;
        }
        return validarFechaConExpresionesRegulares(cadFecNac, errores);
    }

    private static List<String> validarFechaConExpresionesRegulares(String cadFecNac, List<String> errores) {
        Matcher matcher = PATRON_FECHA_CON_GPOS_NOMBRADOS.matcher(cadFecNac);
        if (matcher.matches()) {
            validarQueFechaSeaLogica(matcher, errores);
        }
        else{
             identificarErroresEnPartesDeLaFecha(cadFecNac, errores);
        }

        return errores;
    }

    private static void identificarErroresEnPartesDeLaFecha(String cadFecNac, List<String> errores) {
        // No coincide con el patrón detallado
        // Probar con el patrón básico para ver dónde falla
        Matcher basico = PATRON_FECHA_BASICO.matcher(cadFecNac);
        if (basico.matches()) {
            int anio = Integer.parseInt(basico.group(1));
            if(anio > 2099 || anio < 1900){
                errores.add("✗ La Fecha contiene un año fuera de un rango lógico:" + anio);
            }
            int mes = Integer.parseInt(basico.group(2));
            if(mes > 12 || mes < 1){
                errores.add("✗ La Fecha contiene un mes fuera de un rango lógico:" + mes);
            }
            int dia = Integer.parseInt(basico.group(3));
            if(dia > 31 || mes < 1){
                errores.add("✗ La Fecha contiene un dia fuera de un rango lógico:" + mes);
            }
        }
        else{
             errores.add("✗ La Fecha ¨[" + cadFecNac +
                    "] NO COINCIDE CON EL PATRÓN esperado AAAA/MM/DD [" + CAD_REGEX_FECHA_CON_GPOS_NOMBRADOS + "]");
        }
    }

    private static void validarQueFechaSeaLogica(Matcher matcher, List<String> errores) {
        int anio = Integer.parseInt(matcher.group("anio"));
        int mes = Integer.parseInt(matcher.group("mes"));
        int dia = Integer.parseInt(matcher.group("dia"));
        try {
            LocalDate.of(anio, mes, dia);
        }
        catch(DateTimeException dtex){
            errores.add("✗ La Fecha contiene datos ilógicos (como 30 de febrero o parecido)");
        }
    }

    private static String getNombreCteValido(Scanner lectorConsola) {
        System.out.println("Teclee el nombre del cliente");
        while(true) {
            System.out.print("Nombre:");
            String nombre = lectorConsola.next();
            nombre = nombre.trim().replaceAll("\\s+", " ");
            List<String> lstErrs = validarNombre(nombre);
            if(lstErrs.isEmpty()){
                return CapitalizadorSimple.capitalizar(nombre);
            }
            lstErrs.forEach(System.out::println);
        }
    }

    private static List<String> validarNombre(String nombre) {
        List<String> errores = new ArrayList<>();
        if(nombre.trim().isEmpty()){
            errores.add("Nombre inválido. Está en blanco.");
            return errores;
        }
        return validarNombreConExpresionesRegulares(nombre, errores);
    }

    private static List<String> validarNombreConExpresionesRegulares(String nombre, List<String> errores) {
        Matcher matcher = PATRON_NOMBRE_PERSONA.matcher(nombre);
        if(matcher.matches() == false){
            errores.add("El texto debe tener al menos 2 caracteres, empezar con una palabra (que puede tener " +
                        "partes con guión o apóstrofe), luego tener al menos un espacio y otra palabra " +
                        "similar, y nada más después.");
        }
        return errores;
    }

    private static Integer getIdCteValido(Scanner lectorConsola) {
        System.out.println("Teclee un Id igual a cero para terminar anticipadamente");
        while(true) {
            System.out.print("Id:");
            String cadId = lectorConsola.next();
//            System.err.println("[" + cadId + "] length=" +  cadId.length());
            List<String> lstErrs = validarId(cadId);
            if(lstErrs.isEmpty()){
                return Integer.parseInt(cadId);
            }
            lstErrs.forEach(System.out::println);
        }
    }

    private static List<String> validarId(String cadId) {
        List<String> errores = new ArrayList<>();
        if(cadId.trim().isEmpty()){
            errores.add("Id inválida. Está en blanco.");
            return errores;
        }
        try{
            Integer.parseInt(cadId);
            return errores;
        }
        catch (Exception ex){
            errores.add("Id con formato inválido. " +ex.getMessage());
            return errores;
        }
    }
}
