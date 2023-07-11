package ra.model;

import ra.services.CategoryService;

import java.io.Serializable;
import java.util.ArrayList;

public class Category implements Serializable {
  private final int id;
  private String name;
  private boolean status = true;


  public Category() {
    int currentId;
    ArrayList<Category> categories = CategoryService.getCategories();
    if (categories != null && !categories.isEmpty()) {
      Category lastCategory = categories.get(categories.size() -1);
      currentId = lastCategory.getId();
    } else
      currentId = 0;
    id = ++currentId;
  }

  public int getId() {
    return id;
  }

  @Override
  public String toString() {
    return "ID: " + id + "\n" +
            "Tên danh mục: " + name + "\n" +
            "Trạng thái: " + (status ? "Đang hoạt động" : "Ngừng hoạt động");
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
