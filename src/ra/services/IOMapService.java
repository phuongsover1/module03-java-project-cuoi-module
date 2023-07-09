package ra.services;

import java.io.*;
import java.util.Collection;
import java.util.Map;

public class IOMapService<T,I> {

  public Map<I,T> readFromFile(File file) {
    Map<I,T> map = null;
    try(ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream((file))))) {
        map = (Map<I, T>) ois.readObject();
    } catch (EOFException ex) {

    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }
    return map;
  }

  public void writeToFile(File file, Map<I,T> map) {
    try (ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)))) {
      oos.writeObject(map);
    } catch (FileNotFoundException e) {
      System.err.println("Không tìm thấy file");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
