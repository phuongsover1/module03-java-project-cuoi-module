package ra.controllers;

import ra.enums.BillStatus;
import ra.model.Account;
import ra.model.Bill;
import ra.run.FashionShop;
import ra.services.AccountService;
import ra.services.BillService;
import ra.services.CategoryService;
import ra.services.ProductService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AdminController {
  private static final Scanner adminSC = FashionShop.sc;
  private static final AccountService accountService = new AccountService();
  private static final CategoryService categoryService = new CategoryService();
  private static final ProductService productService = new ProductService();
  private static final BillService billService = new BillService();


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
          case 2:
            quanLySanPham();
            break;
          case 3:
            quanLyDanhMuc();
            break;
          case 4:
            quanLyHoaDon();
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

  private void quanLyHoaDon() {
    int luachon;
    while(true) {
      quanLyHoaDonMenu();
      try {
        luachon = Integer.parseInt(adminSC.nextLine());
        switch (luachon) {
          case 1:
            handleProcessingBill((ArrayList<Bill>) billService.getAllProcessingBills());
            break;

          case 2:
            showAdminProcessBills(AuthController.currentAccount);
            break;

          case 3:
            return;
          default:
            System.err.println("Lựa chọn không hợp lệ. Hãy nhập lại");
        }
      } catch (NumberFormatException ex) {
        System.err.println("Lựa chọn không hợp lệ. Xin vui lòng nhập lại");
      }
    }
  }

  private void showAdminProcessBills(Account currentAccount) {
    billService.showAdminProcessBills(currentAccount, adminSC);
  }

  private void handleProcessingBill(ArrayList<Bill> allProcessingBills) {
    if(allProcessingBills.isEmpty()){
      System.err.println("Hiện tại không có hóa đơn nào đang chờ được xử lý");
      return;
    }
    int idProcessingBill;
    while(true) {
      try{
        billService.displayBills( allProcessingBills);
        System.out.print("Nhập id bill bạn muốn xử lý. (Nhập '-1' để thoát): ");
        idProcessingBill = Integer.parseInt(adminSC.nextLine());
        if (idProcessingBill == -1) {
          return;
        }
        Bill chosenBill = null;
        for (Bill bill:
            allProcessingBills ) {
          if (bill.getId() == idProcessingBill) {
             chosenBill = bill;
             break;
          }
        }
        if (chosenBill == null){
          System.err.println("Id bill bạn vừa nhập không tồn tại. Xin hãy nhập lại");
        }

        if (chosenBill != null) {
          billService.displayBillDetail(chosenBill);
          int luachon;
          boolean isExit = false;
          while(!isExit) {
            System.out.println("Bạn có muốn xác nhận thanh toán cho hóa đơn: ");
            System.out.println("1. Có");
            System.out.println("2. Không ");
            System.out.print("Nhập lựa chọn: ");
            try {
              luachon = Integer.parseInt(adminSC.nextLine());

              switch (luachon) {
                case 1:
                  chosenBill.setBillStatus(BillStatus.DA_THANH_TOAN);
                  chosenBill.setUsername(AuthController.currentAccount.getUsername());
                  AuthController.currentAccount.getBills().add(chosenBill);
                  accountService.writeToFile();
                  isExit = true;
                  break;
                case 2:
                  isExit = true;
                  break;
                default:
                  System.err.println("Lựa chọn không hợp lệ. Hãy nhập lại");
              }
            }catch (NumberFormatException ex) {
              System.err.println("Lựa chọn không hợp lệ, hãy nhập lại.");
            }
          }
        }

      }catch (NumberFormatException ex) {
        System.err.println("Id bill bạn nhập không hợp lệ. Xin hãy nhập lại");
      }
    }
  }

  private void quanLyHoaDonMenu() {
    System.out.println("==== QUẢN LÝ HÓA ĐƠN ====");
    System.out.println("1. Duyệt hóa đơn đang xử lý");
    System.out.println("2. Xem lại hóa đơn đã duyệt");
    System.out.println("3. Thoát");
    System.out.print("Nhập lựa chọn: ");

  }

  private void quanLySanPham() {
    int luachon;
    while (true) {
      try {
        quanLySanPhamMenu();
        luachon = Integer.parseInt(adminSC.nextLine());
        switch (luachon) {
          case 1:
            productService.createProduct(adminSC);
            break;

          case 2:
            productService.displayProducts();
            break;

          case 3:
            productService.updateProduct(adminSC);
            break;
          case 4:
            productService.deleteProduct(adminSC);
            break;
          case 5:
            return;
          default:
            System.err.println("Lựa chọn không hợp lệ. Hãy chọn lại");
        }
      } catch (NumberFormatException ex) {
        System.err.println("Lựa chọn không hợp lệ. Hãy chọn lại");
      }
    }
  }

  private void quanLySanPhamMenu() {
    System.out.println("==== QUẢN LÝ SẢN PHẨM ==== ");
    System.out.println("1. Thêm sản phẩm");
    System.out.println("2. In danh sách sản phẩm");
    System.out.println("3. Sửa sản phẩm");
    System.out.println("4. Xóa sản phẩm");
    System.out.println("5. Thoát");
    System.out.print("Nhập lựa chọn: ");
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
    int luachon;
    while (true) {
      try {
        quanLyDanhMucMenu();
        luachon = Integer.parseInt(adminSC.nextLine());
        switch (luachon) {
          case 1:
            categoryService.createCategory(adminSC);
            break;

          case 2:
            categoryService.displayCategories();
            break;

          case 3:
            categoryService.updateCategory(adminSC);
            break;
          case 4:
            categoryService.deleteCategory(adminSC);
            break;
          case 5:
            return;
          default:
            System.err.println("Lựa chọn không hợp lệ. Hãy chọn lại");
        }
      } catch (NumberFormatException ex) {
        System.err.println("Lựa chọn không hợp lệ. Hãy chọn lại");
      }
    }
  }

  private void quanLyDanhMucMenu() {
    System.out.println("==== QUẢN LÝ DANH MỤC ==== ");
    System.out.println("1. Thêm danh mục");
    System.out.println("2. In danh sách danh mục");
    System.out.println("3. Sửa danh mục");
    System.out.println("4. Xóa danh mục");
    System.out.println("5. Thoát");
    System.out.print("Nhập lựa chọn: ");
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
