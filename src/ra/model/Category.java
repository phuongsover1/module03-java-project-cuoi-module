package ra.model;

import ra.services.CategoryService;

import java.io.Serializable;
import java.util.ArrayList;

public class Category implements Serializable {
  private static int currentId;
  private final int id;
  private String name;
  private boolean status = true;

  static {
    ArrayList<Category> categories = CategoryService.getCategories();
    if (!categories.isEmpty()) {
      Category lastCategory = categories.get(categories.size());
      currentId = lastCategory.getId();
    } else
      currentId = 0;
  }

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
