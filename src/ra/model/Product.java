package ra.model;

import ra.services.CategoryService;
import ra.services.ProductService;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

public class Product implements Serializable {
  private int id;
  private String name;
  private BigDecimal price;
  private Category category;
  private int stock;
  private boolean status = true;

  public Product() {
    int currentId;
    ArrayList<Product> products = ProductService.getProducts();
    if (products != null && !products.isEmpty()) {
      Product lastProduct = products.get(products.size() -1);
      currentId = lastProduct.getId();
      id = ++currentId;
    } else{
      id = 0;
    }
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

  public int getStock() {
    return stock;
  }

  public void setStock(int stock) {
    this.stock = stock;
  }

  public boolean isStatus() {
    return status;
  }

  public void setStatus(boolean status) {
    this.status = status;
  }

  @Override
  public String toString() {
            return "ID: " + id + "\n"+
                    "Tên mật hàng: " + name + "\n"+
                    "Giá: " + price + "\n" +
                    "Danh mục: "  + category.getName() + "\n" +
                    "Số lượng tồn: " + stock + "\n" +
                    "Trạng thái: " + (status ? "Đang được bán" : "Đã ngừng bán" + "\n");
  }
}
