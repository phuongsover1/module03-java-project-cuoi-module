package ra.controllers;

import ra.model.Account;
import ra.run.FashionShop;
import ra.services.AccountService;

import java.util.Optional;
import java.util.Scanner;

public class AuthController {
  private final static AccountService accountService = new AccountService();
  private final static Scanner authSC = FashionShop.sc;

  private static Account currentAccount = new Account("", "");

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
    boolean isExit = false;
    int luachon;
    while (!isExit) {
      try {
        displayMenu();
        luachon = Integer.parseInt(authSC.nextLine());
        switch (luachon) {
          case 1 -> {
            login();
            // TODO: Thực hiện chức năng nghiệp vụ trong đây
            // TODO: Nếu ROLE hiện tại là USER thì hiện menu USER, tương tự với ADMIN
            // TODO:  Cuối chức năng luôn có 1 chức năng đăng xuất

          }

          case 2 -> register();
          case 3 -> {
            System.out.println("Chào tạm biệt");
            isExit = true;
          }
          default -> System.err.println("Lựa khọn không hợp lệ");
        }

      } catch (NumberFormatException ex) {
        System.err.println("Lựa chọn không hợp lệ");
      }
    }

    return isExit;
  }

  private void displayMenu() {
    System.out.println("==== CHÀO MỪNG BẠN ĐẾN VỚI SHOP ====");
    System.out.println("1.Đăng nhập");
    System.out.println("2.Đăng kí");
    System.out.println("3.Thoát");
    System.out.print("Nhập lựa chọn: ");
  }


}
