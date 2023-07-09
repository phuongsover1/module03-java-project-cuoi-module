package ra.services;

import java.io.*;
import java.util.Collection;

public class IOCollectionService<T> {
  public Collection<T> readFromFile(File file) {
    Collection<T> list = null;
    try(ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream((file))))) {
        list = (Collection<T>) ois.readObject();
    } catch (EOFException ex){

    }
    catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }
    return list;
  }

  public void writeToFile(File file, Collection<T> list) {
    try (ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)))) {
      oos.writeObject(list);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


}

