package mx.com.qtx.mod03TestIO.c_codepoints.persistores;

import mx.com.qtx.mod03TestIO.c_codepoints.IPersistorBytesToChars;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class PersistorUTF8 implements IPersistorBytesToChars {
    @Override
    public void guardarBytes(byte[] bytes, String arcSal) {
        System.out.println("\nPersistorUTF8.guardarBytes");

        Charset charsetUtf8 = StandardCharsets.UTF_8;
        int nEscrituras = 0;

        try(FileWriter fw = new FileWriter(arcSal,charsetUtf8 )) {
            CharBuffer buffer = charsetUtf8.decode(ByteBuffer.wrap(bytes));
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
