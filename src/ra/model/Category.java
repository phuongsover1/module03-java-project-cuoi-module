package ra.model;

import java.io.Serializable;

public class Category implements Serializable {
  private static int currentId = 0;
  private final int id;
  private String name;
  private boolean status = true;

  public Category() {
    id = currentId++;
  }

  public int getId() {
    return id;
  }

  @Override
  public String toString() {
    return "Danh mục{" +
            "id=" + id +
            ", tên danh mục='" + name + '\'' +
            ", trạng thái=" + (status ? " Đang hoạt động " : " Không hoạt động") +
            '}';
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isStatus() {
    return status;
  }

  public void setStatus(boolean status) {
    this.status = status;
  }
}
