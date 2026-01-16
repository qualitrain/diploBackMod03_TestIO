package mx.com.qtx.mod03TestIO.f_regex;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.regex.*;

public class ValidadorFecha {
    
    // Versión 1: Regex básico para validar formato
    private static final Pattern PATRON_FECHA_BASICO = 
        Pattern.compile("^(\\d{4})/(\\d{2})/(\\d{2})$");
    
    // Versión 2: Regex avanzado con validación de rangos
    private static final Pattern PATRON_FECHA_AVANZADO = 
        Pattern.compile("^(\\d{4})/(0[1-9]|1[0-2])/(0[1-9]|[12]\\d|3[01])$");
    
    // Versión 3: Regex con nombres de grupos (más legible)
    private static final Pattern PATRON_FECHA_CON_NOMBRES = 
        Pattern.compile("^(?<anio>\\d{4})/(?<mes>0[1-9]|1[0-2])/(?<dia>0[1-9]|[12]\\d|3[01])$");
    
    // Formatter para LocalDate
    private static final DateTimeFormatter FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy/MM/dd");

    public static void main(String[] args) {
        System.out.println("=== CONVERSIÓN DE FECHA A LocalDate ===");
        
        String[] fechasPrueba = {
            "2023/12/25",
            "2023-12-25",      // Formato incorrecto
            "2023/13/25",      // Mes inválido
            "2023/02/30",      // Día inválido para febrero
            "2023/00/15",      // Mes cero
            "23/12/25",        // Año con 2 dígitos
            "2023/2/5",        // Sin ceros a la izquierda
            "2023/12/25 14:30", // Con hora
            "2024/02/29"       // Año bisiesto válido
        };
        
        for (String fechaStr : fechasPrueba) {

            System.out.println("\nCadena: " + String.format("%-18s", "\"" + fechaStr + "\"")
                                          + " " + "-".repeat(60) + "\n");
            
            // Método 1: Solo validación básica
            try {
                LocalDate fecha1 = convertirFechaBasico(fechaStr);
                System.out.println("  Método 1 (básico): ✓ " + fecha1);
            }
            catch (IllegalArgumentException e) {
                System.out.println("  Método 1 (básico): ✗ " + e.getMessage());
            }
            
            // Método 2: Validación avanzada con regex
            try {
                LocalDate fecha2 = convertirFechaAvanzado(fechaStr);
                System.out.println("  Método 2 (avanzado): ✓ " + fecha2);
            } catch (IllegalArgumentException e) {
                System.out.println("  Método 2 (avanzado): ✗ " + e.getMessage());
            }
            
            // Método 3: Usando Optional (mejor para APIs)
            Optional<LocalDate> fecha3 = convertirFechaOptional(fechaStr);
            fecha3.ifPresentOrElse(
                f -> System.out.println("  Método 3 (Optional): ✓ " + f),
                () -> System.out.println("  Método 3 (Optional): ✗ Fecha inválida")
            );
            
            // Método 4: Usando DateTimeFormatter (alternativa sin regex)
            try {
                LocalDate fecha4 = convertirFechaConFormatter(fechaStr);
                System.out.println("  Método 4 (Formatter): ✓ " + fecha4);
            } catch (DateTimeParseException e) {
                System.out.println("  Método 4 (Formatter): ✗ Formato inválido");
            }
            
            System.out.println();

            explicarRegex(fechaStr);
        }

    }
    
    // ========== MÉTODO 1: Validación Básica con Extracción ==========
    /**
     * Valida el formato "AAAA/MM/DD" y convierte a LocalDate.
     * Solo valida formato, no rangos.
     */
    public static LocalDate convertirFechaBasico(String fechaStr) {
        if (fechaStr == null) {
            throw new IllegalArgumentException("La fecha no puede ser nula");
        }
        
        Matcher matcher = PATRON_FECHA_BASICO.matcher(fechaStr);
        
        if (!matcher.matches()) {
            throw new IllegalArgumentException(
                "Formato inválido. Use AAAA/MM/DD (ej: 2023/12/25)"
            );
        }
        
        // Extraer los grupos
        int anio = Integer.parseInt(matcher.group(1));
        int mes = Integer.parseInt(matcher.group(2));
        int dia = Integer.parseInt(matcher.group(3));
        
        // Validar rangos básicos
        if (mes < 1 || mes > 12) {
            throw new IllegalArgumentException("Mes debe estar entre 01 y 12");
        }
        
        if (dia < 1 || dia > 31) {
            throw new IllegalArgumentException("Día debe estar entre 01 y 31");
        }
        
        try {
            return LocalDate.of(anio, mes, dia);
        } catch (Exception e) {
            throw new IllegalArgumentException("Fecha inválida: " + e.getMessage());
        }
    }
    
    // ========== MÉTODO 2: Validación Avanzada ==========
    /**
     * Valida formato Y rangos con regex avanzado.
     */
    public static LocalDate convertirFechaAvanzado(String fechaStr) {
        Matcher matcher = PATRON_FECHA_AVANZADO.matcher(fechaStr);
        
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Formato inválido. Use AAAA/MM/DD");
        }
        
        // Extraer y convertir
        int anio = Integer.parseInt(matcher.group(1));
        int mes = Integer.parseInt(matcher.group(2));
        int dia = Integer.parseInt(matcher.group(3));
        
        // Intentar crear la fecha (LocalDate valida días específicos del mes)
        try {
            return LocalDate.of(anio, mes, dia);
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Fecha inválida: " + e.getMessage());
        }
    }
    
    // ========== MÉTODO 3: Con Grupos Nombrados ==========
    /**
     * Versión más legible usando grupos nombrados.
     * Retorna Optional para mejor manejo en streams y APIs.
     */
    public static Optional<LocalDate> convertirFechaOptional(String fechaStr) {
        if (fechaStr == null) {
            return Optional.empty();
        }
        
        Matcher matcher = PATRON_FECHA_CON_NOMBRES.matcher(fechaStr);
        
        if (!matcher.matches()) {
            return Optional.empty();
        }
        
        try {
            // Extraer usando nombres (más legible)
            int anio = Integer.parseInt(matcher.group("anio"));
            int mes = Integer.parseInt(matcher.group("mes"));
            int dia = Integer.parseInt(matcher.group("dia"));
            
            return Optional.of(LocalDate.of(anio, mes, dia));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    
    // ========== MÉTODO 4: Usando DateTimeFormatter (Alternativa) ==========
    /**
     * Alternativa sin regex, usando el formatter de Java.
     * Más simple pero menos control sobre mensajes de error específicos.
     */
    public static LocalDate convertirFechaConFormatter(String fechaStr) {
        return LocalDate.parse(fechaStr, FORMATTER);
    }
    
    // ========== MÉTODO 5: Con Validación de Negocio Adicional ==========
    /**
     * Ejemplo realista con validaciones de negocio.
     */
    public static LocalDate convertirFechaConValidaciones(
            String fechaStr, 
            LocalDate fechaMinima, 
            LocalDate fechaMaxima) {
        
        // Validar formato con regex
        Matcher matcher = PATRON_FECHA_AVANZADO.matcher(fechaStr);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Formato inválido. Use AAAA/MM/DD");
        }
        
        // Extraer partes
        int anio = Integer.parseInt(matcher.group(1));
        int mes = Integer.parseInt(matcher.group(2));
        int dia = Integer.parseInt(matcher.group(3));
        
        // Crear LocalDate
        LocalDate fecha = LocalDate.of(anio, mes, dia);
        
        // Validaciones de negocio
        if (fecha.isBefore(LocalDate.of(1900, 1, 1))) {
            throw new IllegalArgumentException("La fecha no puede ser anterior a 1900/01/01");
        }
        
        if (fecha.isAfter(LocalDate.now().plusYears(1))) {
            throw new IllegalArgumentException("La fecha no puede ser más de un año en el futuro");
        }
        
        // Validar rangos específicos si se proporcionan
        if (fechaMinima != null && fecha.isBefore(fechaMinima)) {
            throw new IllegalArgumentException(
                "La fecha debe ser posterior a " + fechaMinima.format(FORMATTER)
            );
        }
        
        if (fechaMaxima != null && fecha.isAfter(fechaMaxima)) {
            throw new IllegalArgumentException(
                "La fecha debe ser anterior a " + fechaMaxima.format(FORMATTER)
            );
        }
        
        return fecha;
    }
    
    // ========== UTILIDAD: Explicar qué hizo el regex ==========
    public static void explicarRegex(String fechaStr) {
        System.out.println("    ANÁLISIS DETALLADO PARA: " + fechaStr);
        
        Matcher matcher = PATRON_FECHA_CON_NOMBRES.matcher(fechaStr);
        
        if (matcher.matches()) {
            System.out.println("    ✓ COINCIDENCIA ENCONTRADA");
            System.out.println("      Grupo 'anio': " + matcher.group("anio"));
            System.out.println("      Grupo 'mes': " + matcher.group("mes") +
                             " (validó: 01-12 con ceros iniciales)");
            System.out.println("      Grupo 'dia': " + matcher.group("dia") +
                             " (validó: 01-31 con ceros iniciales)");
            
            // Mostrar el poder de los grupos de captura
            System.out.println("\n      Extracción automática:");
            System.out.println("        Año: " + matcher.group(1) +
                             " (equivalente a grupo 'anio')");
            System.out.println("        Mes: " + matcher.group(2) +
                             " (equivalente a grupo 'mes')");
            System.out.println("        Día: " + matcher.group(3) +
                             " (equivalente a grupo 'dia')");
            
            System.out.println("      Total de grupos (incluyendo el 0): " + (matcher.groupCount() + 1));
            System.out.println("      Grupo 0 (coincidencia completa): " + matcher.group(0));
            
        } else {
            System.out.println("    ✗ NO COINCIDE CON EL PATRÓN");
            System.out.println("    Patrón esperado: ^(?<anio>\\d{4})/(?<mes>0[1-9]|1[0-2])/(?<dia>0[1-9]|[12]\\d|3[01])$");
            
            // Probar con el patrón básico para ver dónde falla
            Matcher basico = PATRON_FECHA_BASICO.matcher(fechaStr);
            if (basico.matches()) {
                System.out.println("    ⚠  Coincide con patrón básico, pero falla en validación de rangos");
                System.out.println("       Año extraído: " + basico.group(1));
                System.out.println("       Mes extraído: " + basico.group(2) + " (¿está entre 01 y 12?)");
                System.out.println("       Día extraído: " + basico.group(3) + " (¿está entre 01 y 31?)");
            }
        }
    }
}