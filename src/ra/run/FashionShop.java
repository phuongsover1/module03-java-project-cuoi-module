package ra.run;

import ra.controllers.AuthController;

import java.util.Scanner;

public class FashionShop {
  public static Scanner sc = new Scanner(System.in);
  private static AuthController authController = new AuthController();
  public static void main(String[] args) {
    authController.menu();
  }
}
