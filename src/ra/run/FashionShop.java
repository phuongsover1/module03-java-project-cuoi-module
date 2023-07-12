package ra.run;

import ra.controllers.AdminController;
import ra.controllers.AuthController;
import ra.controllers.UserController;
import ra.enums.Role;
import ra.utility.Utility;

import java.util.Scanner;

public class FashionShop {
  public static final Scanner sc = new Scanner(System.in);
  private static final AuthController authController = new AuthController();
  private static final AdminController adminController = new AdminController();
  private static final UserController userController = new UserController();

  public static void main(String[] args) {
    while (authController.menu()) {
      // TODO: Thực hiện chức năng nghiệp vụ trong đây
      // TODO: Nếu ROLE hiện tại là USER thì hiện menu USER, tương tự với ADMIN
      // TODO: Cuối chức năng luôn có 1 chức năng đăng xuất
      //

      if (AuthController.currentAccount.getRole() == Role.ADMIN) {
        adminController.menu();
      } else {
        userController.menu();
      }

    }
    sc.close();
  }
}
