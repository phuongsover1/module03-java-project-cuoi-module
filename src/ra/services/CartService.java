package ra.services;

import ra.controllers.AuthController;
import ra.model.Account;
import ra.model.CartItem;
import ra.model.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.Scanner;

public class CartService implements IServiceCollectionGenerics<CartItem, Integer> {
  private static final ProductService productService = new ProductService();
  private static final AccountService accountService = new AccountService();

  public void addToCart(Scanner sc) {
    int idProduct;
    while (true) {
      System.out.print("Nhập id sản phầm muốn thêm vào giỏ hàng.(Nhập '-1' nếu muốn hủy): ");
      try {
        idProduct = Integer.parseInt(sc.nextLine());
        if (idProduct == -1) {
          return;
        }
        if (idProduct < 0) {
          System.err.println("id sản phẩm không hợp lệ");
          continue;
        }
        ArrayList<Product> products = (ArrayList<Product>) productService.findAll();
        Optional<Product> productOptional = productService.findById(idProduct);
        if (productOptional.isPresent()) {
          Product foundProduct = productOptional.get();

          // TODO: nhưng mình phải kiểm tra thêm là đã có sản phẩm này trong giỏ hàng này chưa. Nếu có thì cập nhật số lượng chứ không thêm mới
          ArrayList<CartItem> cartItems = AuthController.currentAccount.getCartItems();
          CartItem cartItem = null;
          for (int i = 0; i < cartItems.size(); i++) {
            if (cartItems.get(i).getProduct().getId() == idProduct) {
              cartItem = cartItems.get(i);
              break;
            }
          }
          // Nhập số lượng cần thêm
          int maxStock = foundProduct.getStock();
          int quantity;
          while (true) {
            if (cartItem != null) {
              System.out.printf("sản phẩm này đã có trong giỏ hàng. Nhập số lượng mới mà bạn muốn chỉnh sửa (current = %d), (max = %d):", cartItem.getQuantity(), maxStock);
            } else {
              System.out.printf("Nhập số lượng mà bạn muốn thêm vào giỏ hàng (max = %d):  ", maxStock);
            }
            try {
              quantity = Integer.parseInt(sc.nextLine());
              if (quantity <= 0) {
                System.err.println("Số lượng đã nhập không hợp lệ. Hãy nhập lại");
                continue;
              }
              if (quantity > maxStock) {
                System.err.printf("Hiện tại trong kho không còn đủ hàng với số lượng mà bạn vừa nhập (max = %d). Hây nhập lại\n", maxStock);
                continue;
              }
              if (cartItem != null){
                cartItem.setQuantity(quantity);
                accountService.writeToFile();
              } else {
                cartItem = new CartItem(AuthController.currentAccount);
                cartItem.setProduct(foundProduct);
                cartItem.setQuantity(quantity);
                save(cartItem);
              }
              break;
            } catch (NumberFormatException ex) {
              System.err.println("Số lượng đã nhập không hợp lệ. Hãy nhập lại");
            }
          }
        } else {
          System.err.printf("Sản phẩm với id = %d không tồn tại trong danh sách sản phẩm\n", idProduct);
        }
      } catch (NumberFormatException ex) {
        System.err.println("Id sản phẩm không hợp lệ");
      }
    }
  }

  public void displayCartItem() {
    Account account = AuthController.currentAccount;
    if (account != null) {
      ArrayList<CartItem> cartItems = account.getCartItems();
      if (cartItems.isEmpty()) {
        System.err.println("Giỏ hàng đang trống. Xin hãy thêm sản phẩm vào giỏ hàng");
        return;
      }
      BigDecimal totalMoney = new BigDecimal(0);
      for (int i = 0; i < cartItems.size(); i++) {
        System.out.printf("===== Sản phẩm thứ %d =====\n", i + 1);
        CartItem cartItem = cartItems.get(i);
        System.out.println(cartItem);
        BigDecimal total = new BigDecimal(cartItem.getQuantity());
        BigDecimal price = new BigDecimal(String.valueOf(cartItem.getProduct().getPrice()));
        BigDecimal result = total.multiply(price);
        totalMoney = totalMoney.add(result);
      }
      System.out.println("Tổng tiền phải thanh toán: " + totalMoney);
    }
  }

  @Override
  public void save(CartItem entity) {
// TODO: Save vào  cart của currentAccount trong authController
    Account currentAccount = AuthController.currentAccount;
    if (currentAccount != null) {
      currentAccount.getCartItems().add(entity);
      accountService.writeToFile();
    }
  }

  @Override
  public void delete(Integer id) {
    Account currentAccount = AuthController.currentAccount;
    if (currentAccount != null) {
      currentAccount.getCartItems().remove(id.intValue());
      accountService.writeToFile();
    }
  }

  @Override
  public Optional<CartItem> findById(Integer id) {
    Optional<CartItem> cartItemOptional = Optional.empty();
    Account currentAccount = AuthController.currentAccount;
    if (currentAccount != null) {
      ArrayList<CartItem> cartItems = currentAccount.getCartItems();
      if (!cartItems.isEmpty()) {
        try {
          cartItemOptional = Optional.of(cartItems.get(id.intValue()));
        } catch (IndexOutOfBoundsException ex) {
          cartItemOptional = Optional.empty();
        }
      }
    }

    return cartItemOptional;
  }

  @Override
  public Collection<CartItem> findAll() {
    return null;
  }

  public void changeQuantity(Scanner sc) {
    ArrayList<CartItem> cartItems = AuthController.currentAccount.getCartItems();
    if (cartItems.isEmpty()) {
      System.err.println("Giỏ hàng đang trống. Hãy thêm sản phẩm vào giỏ hàng trước");
      return;
    }
    int cartItemId;
    while (true) {
      displayCartItem();
      System.out.print("Nhập id của mặt hàng mà bạn muốn thay đổi số lượng (Nhập '-1' nếu muốn thoát) : ");
      try {
        cartItemId = Integer.parseInt(sc.nextLine());
        if (cartItemId == -1) {
          return;
        }
        if (cartItemId < 0) {
          System.err.println("Id mặt hàng không hợp lệ. Hãy nhập lại.");
          continue;
        }
        // lấy số lượng hiện tại trong product
        Optional<CartItem> cartItemOptional = findById(cartItemId);
        if (cartItemOptional.isPresent()) {
          CartItem cartItem = cartItemOptional.get();
          Product product = productService.findById(cartItem.getProduct().getId()).orElse(null);
          if (product != null) {
            int maxStock = product.getStock();
            int changeQuantity;
            while (true) {
              System.out.printf("Nhập số lượng mà bạn muốn thay đổi (max = %d). Nếu muốn thoát ấn '-1': ", maxStock);
              try {
                changeQuantity = Integer.parseInt(sc.nextLine());
                if (changeQuantity == -1) {
                  return;
                }
                if (changeQuantity <= 0) {
                  System.err.println("Số lượng bạn vừa nhập không hợp lệ. Hãy nhập lại.");
                  continue;
                }
                if (changeQuantity > maxStock) {
                  System.err.printf("Hiện tại trong kho không còn đủ hàng với số lượng mà bạn vừa nhập (max = %d). Hây nhập lại\n", maxStock);
                  continue;
                }
                cartItem.setQuantity(changeQuantity);
                accountService.writeToFile();
                System.out.println("Thay đổi số lượng thành công");
                break;

              } catch (NumberFormatException ex) {
                System.err.println("Số lượng bạn vừa nhập không hợp lệ.");
              }
            }
          }

        } else {
          System.err.printf("Id %d trong giỏ hàng không tồn tại. Hãy nhập lại.\n", cartItemId);
        }
      } catch (NumberFormatException ex) {
        System.err.println("Id mặt hàng không hợp lệ. Hãy nhập lại.");
      }

    }
  }

  public void deleteCartItem(Scanner sc) {
    int cartItemId;
    while (true) {
      ArrayList<CartItem> cartItems = AuthController.currentAccount.getCartItems();
      if (cartItems.isEmpty()) {
        System.err.println("Giỏ hàng đang trống. Hãy thêm sản phẩm vào giỏ hàng trước");
        return;
      }
      displayCartItem();
      System.out.print("Nhập id của mặt hàng mà bạn muốn xóa khỏi giỏ hàng (Nhập '-1' nếu muốn thoát) : ");
      try {
        cartItemId = Integer.parseInt(sc.nextLine());
        if (cartItemId == -1) {
          return;
        }
        if (cartItemId < 0) {
          System.err.println("Id mặt hàng không hợp lệ. Hãy nhập lại.");
          continue;
        }
        //
        Optional<CartItem> cartItemOptional = findById(cartItemId);
        if (cartItemOptional.isPresent()) { // nếu thấy được mặt hàng khớp với id
          delete(cartItemId);
          System.out.println("Xóa thành công");
        } else {
          System.err.printf("Id %d trong giỏ hàng không tồn tại. Hãy nhập lại.\n", cartItemId);
        }
      } catch (NumberFormatException ex) {
        System.err.println("Id mặt hàng không hợp lệ. Hãy nhập lại.");
      }

    }
  }
}
