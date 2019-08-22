package com.hxd.test1.util;

import java.io.*;


public class ByteArrayUtils {

    public static<T> byte[] objectToBytes(T obj){
        byte[] bytes = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream sOut;
        try {
            sOut = new ObjectOutputStream(out);
            sOut.writeObject(obj);
            sOut.flush();
            bytes= out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    public static<T> T bytesToObject(byte[] bytes) {
        T t = null;
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        ObjectInputStream sIn;
        try {
            sIn = new ObjectInputStream(in);
            t = (T)sIn.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;

    }
}
