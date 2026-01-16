package mx.com.qtx.mod03TestIO.f_regex;

import java.util.regex.Pattern;

public class ValidadorCampos {
	public static final String REGEX_CAD_INICIA_C_BLANCOS = "\\s+.*";
	// \\s+       Uno o mas caracteres blancos (\n,\r,espacio, \t)
	// .*         cero o mas caracteres del tipo que sea
	
	
	public static final String REGEX_USUARIO = "^[a-z0-9_-]{6,15}$";
	// ^          Inicio de la linea
	// [a-z0-9_-] Debe coinicidir con estos caracteres: digitos, a-z, 0-9, guion bajo y el guion
	// {6,15}     Longitud de minimo 6 y hasta 15 caracteres
	// $          Fin de linea
	
	public static final String REGEX_EMAIL_A = 
			"^[A-Za-z][_A-Za-z0-9-]{3,7}" +
                    "(\\.[_A-Za-z0-9-]{3,10})*" +
                    "@[A-Za-z0-9]+" +
                    "(\\.[A-Za-z0-9]+)*" +
                    "(\\.[A-Za-z]{2,})$";
	/*
	 * 		^                  Inicio de la linea 
	 * 		[A-Za-z]           Sigue una letra mayuscula o minuscula. 
	 * 		[_A-Za-z0-9-]{3,7} Sigue una palabra (formada por letras,digitos, guiones o guiones bajos con
	 *                         una longitud de 3 a 7 caracteres
	 *                         
	 *      (                  Inicio del primer grupo
	 *         \\.			          Debe seguir un punto (literal)
	 *         [_A-Za-z0-9-]{3,10}    Sigue una palabra de 3 a 10 caracteres de longitud formada por letras,
	 *         						  digitos o guiones (bajos y normales)
	 *      )*				   Fin del primer grupo. Pueden haber cero o mas de este grupo(es opcional)
	 *      
	 *      @				   Debe seguir una arroba
	 *      [A-Za-z0-9]+ 	   Sigue el nombre del dominio de correo formado por una palabra de al menos un 
	 *                         caracter
	 *      (				   Inicio del segundo grupo
	 *      	\\.			          Debe seguir un punto (literal)
	 *       	[A-Za-z0-9]+          Sigue una palabra de al menos un caracter de longitud
	 *      )*				   Fin del segundo grupo. Pueden haber cero o mas de este grupo(es opcional)
	 *      
	 *      (				   Inicio del tercer grupo (grupo final y obligatorio)
	 *      	\\.					  Debe seguir un punto (literal)
	 *      	[A-Za-z]{2,}		  Deben seguir al menos dos letras (para el sufijo de pais o tipo de 
	 *      						  dominio: mx, cl, us, com, edu, etc.)
	 *      )				   Fin del segundo grupo. Notese que es obligatorio
	 *      $          Fin de linea
	 *      
	 */
	
	public static boolean cadenaIniciandoConBlancos(String cadenaValidable){
		return Pattern.matches(REGEX_CAD_INICIA_C_BLANCOS, cadenaValidable);
	}
	public static boolean cadenaFormatoEmail(String cadenaValidable){
		return Pattern.matches(REGEX_EMAIL_A, cadenaValidable);
	}
	public static boolean cadenaFormatoUsuarioOk(String cadenaValidable){
		return Pattern.matches(REGEX_USUARIO, cadenaValidable);
	}

}
