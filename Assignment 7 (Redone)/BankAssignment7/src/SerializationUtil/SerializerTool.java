package SerializationUtil;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.io.NotSerializableException;

/**
 * INFO ON SERIALIZATION
 * Deserializing a serialized object creates a new object;
 * serialVersionUID field is necessary for Serializable as well as Externalize to provide version control;
 * Compiler will provide this field if we do not provide it which might change if we modify the class structure
 * Of our class, and we will get InvalidClassException;
 * If we provide value to this field and do not change it, serialization-deserialization will not fail if
 * we change our class structure;
 * ex: private static final long serialVersionUID = 2L;
 *
 * Serialization process do not invoke the constructor, but it can assign values to final fields;
 * transient variables will not be serialized, serialized object holds null;
 * static variables will not be serialized, serialized object holds null;
 * In Java, we create several objects that live and die accordingly, and
 * every object will certainly die when the JVM dies or we might want to transfer an object
 * to another machine over the network;
 *
 * serialization allows us to convert the state of an object into a byte stream,
 * which then can be saved into a file on the local disk or sent over the network to any other machine.
 *
 * deserialization allows us to reverse the process, which means reconverting
 * the serialized byte stream to an object again;
 *
 * In simple words, object serialization is the process of saving an object's state to a sequence of bytes and
 * deserialization is the process of reconstructing an object from those bytes.
 *
 *  Any class that implements Serializable interface directly or through its parent can be serialized,
 *  and classes that do not implement Serializable can not be serialized.
 *
 *  When a class implements the Serializable interface, all its subclasses are serializable as well.
 *  But when an object has a reference to another object, these objects must implement the Serializable
 *  interface separately.If our class is having even a single reference to a non-Serializable class then
 *  JVM will throw NotSerializableException.
 *
 *  If we want to serialize one object but do not want to serialize specific fields,
 *  then we can mark those fields as transient.
 *
 *  All the static fields belong to the class instead of the object, and the serialization process serializes
 *  the object so static fields can not be serialized.It does not care about access modifiers such as private
 *
 *  All non-transient and non-static fields are considered part of an object's persistent state and
 *  are eligible for serialization.
 *
 *  If a serializable class doesn't declare a serialVersionUID, the JVM will generate one automatically at run-time.
 */

public class SerializerTool {

    //Deserialize the file to read the contents of the serialized object
    public static Object deserialize(String fileName) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(fileName);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Object obj = ois.readObject();
        ois.close();
        return obj;
    }

    //Serialize the object and write/store it to a file
    public static void serialize(Object obj, String fileName) throws IOException {
        FileOutputStream fos = new FileOutputStream(fileName);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(obj);

        oos.close();
    }

    //ADDED writeObject and readObject to stop serialization adn deserialization
    private static void writeObject(ObjectOutputStream oos) throws IOException {
        throw new NotSerializableException("Serialization is not supported on this object!");
    }

    private static void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        throw new NotSerializableException("DeSerialization is not supported on this object!");
    }
}
