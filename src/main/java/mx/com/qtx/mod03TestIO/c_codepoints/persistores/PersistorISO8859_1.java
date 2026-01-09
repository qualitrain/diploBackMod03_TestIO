package mx.com.qtx.mod03TestIO.c_codepoints.persistores;

import mx.com.qtx.mod03TestIO.c_codepoints.IPersistorBytesToChars;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class PersistorISO8859_1 implements IPersistorBytesToChars {
    @Override
    public void guardarBytes(byte[] bytes, String arcSal) {
        System.out.println("\nPersistorISO8859_1.guardarBytes");
        Charset charset = StandardCharsets.ISO_8859_1;
        int nEscrituras = 0;

        try(FileWriter fw = new FileWriter(arcSal,charset )) {
            CharBuffer buffer = charset.decode(ByteBuffer.wrap(bytes));
            for(char charI:buffer.array()){
//                System.out.println("(int)charI = " + (int)charI);
                if(charI > 0){ // No escribe nulos
                    fw.write(charI);
                    nEscrituras++;
                }
            }
            System.out.println("nEscrituras = " + nEscrituras);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
