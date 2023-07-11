package ra.controllers;

import ra.model.Account;
import ra.run.FashionShop;
import ra.services.AccountService;

import java.util.Scanner;

public class AdminController {
  private static final Scanner adminSC = FashionShop.sc;
  private static final AccountService accountService = new AccountService();

  public boolean menu() {
    int luachon;
    while (true) {
      displayMenu();
      try {
        luachon = Integer.parseInt(adminSC.nextLine());

        switch (luachon) {
          case 1:
            quanLyAccount();
            break;

          case 5:
            AuthController.currentAccount = new Account("", "");
            return false;
          default:
            System.err.println("Lựa chọn không hợp lệ!!!");

        }
      } catch (NumberFormatException ex) {
        System.err.println("Lựa chọn không hợp lệ!!!");
      }

    }
  }

  private void quanLyAccount() {
    int luachon;
    while (true) {
      try {
        quanLyAccountMenu();
        luachon = Integer.parseInt(adminSC.nextLine());
        switch (luachon) {
          case 1:
            accountService.createAdminAccount(adminSC);
            break;
          case 2:
            accountService.changeAccountStatus(adminSC);
            break;
          case 3:
            // TODO: Làm quản lý danh mục
            return;

          default:
            System.err.println("Lựa chọn không hợp lệ.");
        }
      } catch (NumberFormatException ex) {
        System.err.println("Lựa chọn không hợp lệ.");
      }
    }
  }

  private void quanLyAccountMenu() {
    // TODO: Làm thêm in danh sách tất cả các tài khoản
    System.out.println("==== QUẢN LÝ ACCOUNT ====");
    System.out.println("1. Tạo account quản lý");
    System.out.println("2. Khóa mở account");
    System.out.println("3. Thoát");
    System.out.print("Nhập lựa chọn: ");
  }

   private void quanLyDanhMuc() {

   }

   private void quanLyDanhMucMenu() {
     System.out.println("==== QUẢN LÝ DANH MỤC ==== ");
     System.out.println("1. Thêm danh mục");
     System.out.println("2. In danh sách danh mục");
     System.out.println("3. Sửa danh mục");
     System.out.println("4. Xóa danh mục");
     System.out.println("5. Thoát");
   }
  private void displayMenu() {
    System.out.println("==== QUẢN LÝ ====");
    System.out.println("1.Quản lý account");
    System.out.println("2.Quản lý sản phẩm");
    System.out.println("3.Quản lý danh mục");
    System.out.println("4.Quản lý hóa đơn");
    System.out.println("5.Đăng xuất");
    System.out.print("Nhập lựa chọn: ");
  }
}
