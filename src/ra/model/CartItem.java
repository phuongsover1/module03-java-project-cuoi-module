package ra.model;

import java.io.Serializable;
import java.util.ArrayList;

public class CartItem implements Serializable {
  private final int id;
  private Product product;
  private int quantity;

  public CartItem(Account account) {
    ArrayList<CartItem> cartItems = account.getCartItems();
    if (cartItems.isEmpty()) {
      id = 0;
    } else {
      id = cartItems.get(cartItems.size() - 1).id + 1;
    }
  }

  public int getId() {
    return id;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  @Override
  public String toString() {
            return "ID: " + id + "\n" +
                    "Tên sản phẩm: " + product.getName() + "\n"  +
                    "Số lượng: " + quantity + "\n" +
                    "Giá: " + product.getPrice();

  }
}
