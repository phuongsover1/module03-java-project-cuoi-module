package ra.controllers;

import ra.enums.Role;
import ra.model.Account;
import ra.run.FashionShop;
import ra.services.AccountService;

import java.util.Optional;
import java.util.Scanner;

public class AuthController {
  private final static AccountService accountService = new AccountService();
  private final static AdminController adminController = new AdminController();

  private final static Scanner authSC = FashionShop.sc;

  public static Account currentAccount = new Account("","");

  private void register() {
    System.out.println("==== ĐĂNG KÍ ====");
    accountService.createNewAccount(authSC);
  }

  private void login() {
    System.out.println("==== ĐĂNG NHẬP ====");
    String username;
    String password;
    while (true) {
      System.out.print("Nhập tên tài khoản: ");
      username = authSC.nextLine().trim();
      if (username.equals("")) {
        System.err.println("Tên đăng nhập không được để trống. Xin vui lòng nhập lại.");
        continue;
      }
      break;
    }

    while (true) {
      System.out.print("Nhập mật khẩu: ");
      password = authSC.nextLine().trim();
      if (password.equals("")) {
        System.err.println("Mật khẩu không được để trống. Xin vui lòng nhập lại.");
        continue;
      }
      break;
    }
    Optional<Account> returnedAccountOptional = accountService.getLoginAccount(username, password);
    returnedAccountOptional.ifPresent(account -> currentAccount = account);
  }

  private void findAll() {
    System.out.println("Tất cả tài khoản: ");
    System.out.println(accountService.findAll());

  }

  public boolean menu() {
    int luachon;
    while (true) {
      try {
        displayMenu();
        luachon = Integer.parseInt(authSC.nextLine());
        switch (luachon) {
          case 1 -> {
            login();
            if (!currentAccount.getUsername().equals(""))
              return true;
          }

          case 2 -> register();
          case 3 -> {
            System.out.println("Chào tạm biệt");
            return false;
          }
          default -> System.err.println("Lựa khọn không hợp lệ");
        }

      } catch (NumberFormatException ex) {
        System.err.println("Lựa chọn không hợp lệ");
      }
    }
  }

  private void displayMenu() {
    System.out.println("==== CHÀO MỪNG BẠN ĐẾN VỚI SHOP ====");
    System.out.println("1.Đăng nhập");
    System.out.println("2.Đăng kí");
    System.out.println("3.Thoát");
    System.out.print("Nhập lựa chọn: ");
  }


}
