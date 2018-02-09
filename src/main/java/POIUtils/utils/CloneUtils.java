package POIUtils.utils;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedHashMap;
import java.util.Map;

public class CloneUtils {

    private final static Map<Integer, Object> CACHE = new LinkedHashMap<>();

    private final static int MAX_CACHE_COUNT = 10;

    public CloneUtils() {
    }

    public <T> T clone(T beCloned) {
        try {
            return readBytesToObject(writeObjectToBytes(beCloned));
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private <T> byte[] writeObjectToBytes(T beCloned) throws IOException {
        ByteOutputStream byteOutputStream = new ByteOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteOutputStream);
        objectOutputStream.writeObject(beCloned);
        byte[] bytes = byteOutputStream.getBytes();
        CACHE.putIfAbsent(beCloned.hashCode(),bytes);
        return bytes;
    }

    private <T> T readBytesToObject(byte[] bytes) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(bytes));
        return (T) objectInputStream.readObject();
    }

}
