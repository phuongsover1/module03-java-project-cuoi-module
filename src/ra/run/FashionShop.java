package ra.run;

import ra.controllers.AdminController;
import ra.controllers.AuthController;
import ra.controllers.UserController;
import ra.utility.Utility;

import java.util.Scanner;

public class FashionShop {
  public static Scanner sc = new Scanner(System.in);
  private static AuthController authController = new AuthController();
  private static AdminController adminController = new AdminController();
  private static UserController userController = new UserController();
  public static void main(String[] args) {
    if(authController.menu()) {
      // TODO: Thực hiện chức năng nghiệp vụ trong đây
      // TODO: Nếu ROLE hiện tại là USER thì hiện menu USER, tương tự với ADMIN
      // TODO:  Cuối chức năng luôn có 1 chức năng đăng xuất

    }
  }
}
