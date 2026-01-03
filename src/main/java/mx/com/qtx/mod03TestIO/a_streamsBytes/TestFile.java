package mx.com.qtx.mod03TestIO.a_streamsBytes;

import java.io.File;

public class TestFile {
    public static void main(String[] args) {
        File arc = new File("imagen.jpg");
        System.out.println("arc.getAbsolutePath() = " + arc.getAbsolutePath());
        System.out.println("arc.isFile() = " + arc.isFile());
        System.out.println("arc.length() = " + arc.length());
        System.out.println("arc.getParent() = " + arc.getParent());
        System.out.println("arc.canRead() = " + arc.canRead());
    }
}
