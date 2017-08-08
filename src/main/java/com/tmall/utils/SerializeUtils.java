package com.tmall.utils;

import java.io.*;

/**
 * 序列化及反序列化工具
 */
public class SerializeUtils {

    public static byte[] obj2Byte(Object object) throws IOException {
        if (object == null) return null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(bos);
        os.writeObject(object);
        os.close();

        byte[] result = bos.toByteArray();
        bos.close();
        return result;
    }

    public static Object byte2Obj(byte[] object) throws IOException, ClassNotFoundException {
        if (object == null) return null;
        ByteArrayInputStream bin = new ByteArrayInputStream(object);
        ObjectInputStream os = new ObjectInputStream(bin);
        Object result = os.readObject();

        bin.close();
        os.close();
        return result;
    }

}
