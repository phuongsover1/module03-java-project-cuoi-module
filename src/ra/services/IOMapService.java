package ra.services;

import java.io.*;
import java.util.Collection;
import java.util.Map;

public class IOMapService<T,I> {
  private final File destination;

  public IOMapService(File destination) {
    this.destination = destination;
  }
  public Map<I,T> readFromFile() {
    Map<I,T> map = null;
    try(ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream((destination))))) {
        map = (Map<I, T>) ois.readObject();
    } catch (EOFException ex) {

    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }
    return map;
  }

  public void writeToFile(Map<I,T> map) {
    try (ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(destination)))) {
      oos.writeObject(map);
    } catch (FileNotFoundException e) {
      System.err.println("Không tìm thấy file");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
