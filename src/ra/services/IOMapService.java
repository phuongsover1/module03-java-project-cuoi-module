package ra.services;

import java.io.*;
import java.util.Collection;
import java.util.Map;

public class IOMapService<T,I> {

  public Map<I,T> readFromFile(File file) {
    Map<I,T> map = null;
    try(ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream((file))))) {
      map = (Map<I, T>) ois.readObject();
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
    return map;
  }

  public void writeToFile(File file, Map<I,T> map) {
    try (ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)))) {
      oos.writeObject(map);
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
