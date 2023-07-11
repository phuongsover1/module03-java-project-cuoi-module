package ra.controllers;

import ra.run.FashionShop;
import ra.services.AccountService;
import ra.services.CategoryService;

import java.util.Scanner;

public class UserController {
  private static final Scanner userSC = FashionShop.sc;
  private static final AccountService accountService = new AccountService();
  private static final CategoryService categoryService = new CategoryService();

  public boolean menu() {
    int luachon;
    while (true) {
      try {
        displayMenu();
        luachon = Integer.parseInt(userSC.nextLine());
        if (luachon < 1 || luachon > 5) {
          System.err.println("Lựa chọn không hợp lê");
          continue;
        }
        switch (luachon) {
          case 1:
            accountService.updateAccountMenu(userSC);
            break;
          case 2:
            break;
          default:
            // TODO: Muốn thoát ra màn hình đăng nhập thì trả về false
            return false;
        }
      } catch (NumberFormatException ex) {
        System.err.println("Lựa chọn không hợp lê.");
      }
    }
  }


  private void displayMenu() {
    System.out.println("==== USER ====");
    System.out.println("1. Quản lý profile");
    System.out.println("2. Xem sản phẩm");
    System.out.println("3. Quản lý giỏ hàng");
    System.out.println("4. Quản lý hóa đơn");
    System.out.println("5. Đăng xuất");
    System.out.print("Nhập lựa chọn: ");
  }
}
