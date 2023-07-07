package ra.controllers;

import ra.model.Account;
import ra.run.FashionShop;
import ra.services.AccountService;

import java.util.Optional;
import java.util.Scanner;

public class AuthController {
  private final static AccountService accountService = new AccountService();
  private final static Scanner authSC = FashionShop.sc;

  private static Optional<Account> currentAccountOptional = Optional.ofNullable(null);

  private void register() {
    accountService.createNewAccount(authSC);
  }

  // TODO: Làm phần đăng nhập
  private Account login() {
    return null;
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
          case 1:
            currentAccountOptional = Optional.of(login());
            break;
          case 2:
            register();
            break;
          case 3:
            System.out.println("Chào tạm biệt");
            isExit = true;
            break;
        }

      } catch (NumberFormatException ex) {
        System.out.println("Lựa chọn không hợp lệ");
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
