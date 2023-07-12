package ra.services;

import java.io.*;
import java.util.Collection;

public class IOCollectionService<T> {
  private final File destination;

  public IOCollectionService(File destination) {
    this.destination = destination;
  }
  public Collection<T> readFromFile() {
    Collection<T> list = null;
    try(ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream((destination))))) {
        list = (Collection<T>) ois.readObject();
    } catch (EOFException ex){

    }
    catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }
    return list;
  }

  public void writeToFile(Collection<T> list) {
    try (ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(destination)))) {
      oos.writeObject(list);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


}

