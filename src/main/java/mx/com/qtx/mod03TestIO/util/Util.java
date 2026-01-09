package mx.com.qtx.mod03TestIO.util;

public class Util {
	public static String getCausa(Exception e) {
		if (e.getCause() == null)
			return "";
		return " causado por " + e.getCause().getClass().getName() 
				               + ":" + e.getCause().getMessage() ;
	}


}
