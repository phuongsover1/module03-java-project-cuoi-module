package ra.controllers;

import ra.model.Account;
import ra.run.FashionShop;
import ra.services.*;

import java.math.BigDecimal;
import java.util.Scanner;

public class UserController {
  private static final Scanner userSC = FashionShop.sc;
  private static final AccountService accountService = new AccountService();
  private static final CategoryService categoryService = new CategoryService();
  private static final ProductService productService = new ProductService();
  private static final CartService cartService = new CartService();
  private static final BillService billService = new BillService();


  private void logout() {
    AuthController.currentAccount = new Account("", "");
  }

  public boolean menu() {
    int luachon;
    while (true) {
      try {
        displayMenu();
        luachon = Integer.parseInt(userSC.nextLine());
        if (luachon < 1 || luachon > 6) {
          System.err.println("Lựa chọn không hợp lê");
          continue;
        }
        switch (luachon) {
          case 1:
            accountService.updateAccountMenu(userSC);
            break;
          case 2:
            xemSanPham();
            break;
          case 3:
            quanLyGioHang();
            break;
          case 4:
            quanLyHoaDon();
            break;
          case 5:
            napTienTaiKhoan(userSC, AuthController.currentAccount);
            break;
          case 6:
            // TODO: Muốn thoát ra màn hình đăng nhập thì trả về false
            logout();
            return false;
        }
      } catch (NumberFormatException ex) {
        System.err.println("Lựa chọn không hợp lê.");
      }
    }
  }

  private void napTienTaiKhoan(Scanner scanner, Account currentAccount)  {
    System.out.println("Số tiền hiện tại của tại khoản: " + currentAccount.getTotalCurrentMoney());
    String money;
    while(true) {
      try {
        System.out.print("Hãy nhập số tiền cần nạp (Nhập '-1' đẻ thoát): ");
        money =  scanner.nextLine();
        if (money.equals("-1"))
          return;
        double moneyDouble = Double.parseDouble(money);

        BigDecimal moneyDecimal = new BigDecimal(money);
        currentAccount.setTotalCurrentMoney(currentAccount.getTotalCurrentMoney().add(moneyDecimal));
        System.out.println("==== Đã nạp thành công. Số tiền mới hiện tại: " + currentAccount.getTotalCurrentMoney());
        accountService.writeToFile();
        break;
      } catch (NumberFormatException ex) {
        System.err.println("Số tiền bạn nhập không hợp lệ. Hãy nhập lại");
      }
    }
  }

  private void quanLyHoaDon() {
    int luachon;
    while (true) {
      quanLyHoaDonMenu();
      try {
        luachon = Integer.parseInt(userSC.nextLine());
        switch (luachon) {
          case 1:
            billService.showProcessingBills(AuthController.currentAccount, userSC);
            break;
          case 2:
            billService.showSuccessfulBills(AuthController.currentAccount, userSC);
            break;
          case 3:
            billService.showCanceledBills(AuthController.currentAccount, userSC);
            break;
          case 4:
            // TODO: Hủy đơn
            billService.cancelBill(AuthController.currentAccount, userSC);
            break;
          case 5:
            return;
          default:
            System.err.println("Lựa chọn không hợp lệ. Hãy nhập lại");
        }
      } catch (NumberFormatException ex) {
        System.err.println("Lựa chọn không hợp lệ. Hãy nhập lại");
      }
    }
  }

  private void quanLyHoaDonMenu() {
    System.out.println("==== QUẢN LÝ HÓA ĐƠN ==== ");
    System.out.println("1. Danh sách hóa đơn đang được xử lý");
    System.out.println("2. Danh sách hóa đơn đã thanh toán");
    System.out.println("3. Danh sách hóa đơn đã bị hủy");
    System.out.println("4. Hủy hóa đơn (nhớ cộng lại tiền đã mất cho hóa đơn đó): ");
    System.out.println("5. Thoát");
    System.out.print("Nhập lựa chọn: ");
  }

  private void quanLyGioHang() {
    int luachon;
    while (true) {
      quanLyGiohangMenu();
      try {
        luachon = Integer.parseInt(userSC.nextLine());
        switch (luachon) {
          case 1:
            cartService.addToCart(userSC);
            break;
          case 2:
            cartService.displayCartItem();
            break;
          case 3:
            cartService.changeQuantity(userSC);
            break;
          case 4:
            cartService.deleteCartItem(userSC);
            break;
          case 5:
            //  THANH TOÁN GIỎ HÀNG
            billService.createBill(AuthController.currentAccount);
            break;
          case 6:
            return;
          default:
            System.err.println("Lựa chọn không hợp lệ. Hãy nhập lại");
        }
      } catch (NumberFormatException ex) {
        System.err.println("Lựa chọn không hợp lệ. Hãy nhập lại");
      }
    }
  }

  private void quanLyGiohangMenu() {
    System.out.println("==== QUẢN LÝ GIỎ HÀNG ====");
    System.out.println("1. Thêm sản phẩm vào giỏ hàng");
    System.out.println("2. Danh sách sản phẩm trong giỏ hàng");
    System.out.println("3. Sửa số lượng sản phẩm");
    System.out.println("4. Xóa sản phẩm trong giỏ hàng");
    System.out.println("5. Thanh toán giỏ hàng");
    System.out.println("6. Thoát");
    System.out.print("Nhập lựa chọn: ");
  }

  private void xemSanPham() {
    int luachon;
    while (true) {
      try {
        xemSanPhamMenu();
        luachon = Integer.parseInt(userSC.nextLine());
        switch (luachon) {
          case 1:
            productService.displayProducts();
            break;
          case 2:
            productService.findProduct(userSC);
            break;
          case 3:
            return;
          default:
            System.err.println("Lựa chọn không hợp lệ. Hãy nhập lại");
        }
      } catch (NumberFormatException ex) {
        System.err.println("Lựa chọn không hợp lệ. Hãy nhập lại");
      }
    }
  }

  private void xemSanPhamMenu() {
    System.out.println("==== XEM SẢN PHẨM ==== ");
    System.out.println("1.Xem danh sách sản phẩm");
    System.out.println("2.Tìm kiếm sản phẩm theo tên, danh mục");
    System.out.println("3.Thoát");
    System.out.print("Nhập lựa chọn: ");
  }

  private void displayMenu() {
    System.out.println("==== USER ====");
    System.out.println("1. Quản lý profile");
    System.out.println("2. Xem sản phẩm");
    System.out.println("3. Quản lý giỏ hàng");
    System.out.println("4. Quản lý hóa đơn");
    System.out.println("5. Nạp tiền vào tài khoản");
    System.out.println("6. Đăng xuất");
    System.out.print("Nhập lựa chọn: ");
  }
}
