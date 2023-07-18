package ra.services;

import ra.enums.BillStatus;
import ra.model.Account;
import ra.model.Bill;
import ra.model.CartItem;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Scanner;

public class BillService {
  private static final AccountService accountService = new AccountService();

  public void createBill(Account account) {

    Bill newBill = new Bill(account);
    newBill.setCartItems(new ArrayList<>(account.getCartItems()));
    BigDecimal totalMoney = new BigDecimal(0);
    for (CartItem cartItem :
            account.getCartItems()) {
      BigDecimal result = cartItem.getProduct().getPrice().multiply(new BigDecimal(cartItem.getQuantity()));
      totalMoney = totalMoney.add(result);
    }

    BigDecimal remainMoney =  account.getTotalCurrentMoney().subtract(totalMoney);
    if (remainMoney.compareTo(new BigDecimal(0)) < 0) {
      System.err.println("Tài khoản không có đủ tiền, xin hãy nạp thêm");
      return;
    }
    newBill.setTotalMoney(totalMoney);
    account.getBills().add(newBill);
    account.setCartItems(new ArrayList<>());
    accountService.writeToFile();
    System.out.println("==== ĐÃ THANH TOÁN THÀNH CÔNG, ĐANG CHỜ NHÂN VIÊN XÁC NHẬN ==== ");
  }

  public void showProcessingBills(Account account, Scanner sc) {
    ArrayList<Bill> bills = account.getBills();
    if (bills.isEmpty()) {
      System.err.println("Danh sách hóa đơn đang trống");
      return;
    }
    int i = 0;
    ArrayList<Bill> processingBills = new ArrayList<>();
    for (Bill bill :
            bills) {
      if (bill.getBillStatus() == BillStatus.DANG_XU_LY) {
        System.out.printf("==== HÓA ĐƠN THỨ %d ====\n", ++i);
        System.out.println(bill);
        processingBills.add(bill);
      }
    }
    int luachon;
    while (true) {
      System.out.print("==== BẠN CÓ MUỐN XEM CHI TIẾT HÓA ĐƠN ĐANG ĐƯỢC XỬ LÝ NÀO KHÔNG ====\n");
      System.out.println("1. Có");
      System.out.println("2. Không");
      System.out.print("Nhập lựa chọn: ");
      try {
        luachon = Integer.parseInt(sc.nextLine());
        switch (luachon) {
          case 1:
            chooseBillToDisplayDetail(sc, processingBills);
            break;
          case 2:
            return;
          default:
            System.err.println("Lựa chọn không hợp lệ. Hãy nhập lại");

        }
      } catch (NumberFormatException ex) {
        System.err.println("Lựa chọn không hợp lệ. Hãy nhập lại");
      }

    }
  }

  public void showSuccessfulBills(Account account, Scanner sc) {
    ArrayList<Bill> bills = account.getBills();
    if (bills.isEmpty()) {
      System.err.println("Danh sách hóa đơn đang trống");
      return;
    }
    int i = 0;
    ArrayList<Bill> successfulBills = new ArrayList<>();
    for (Bill bill :
            bills) {
      if (bill.getBillStatus() == BillStatus.DA_THANH_TOAN) {
        System.out.printf("==== HÓA ĐƠN THỨ %d ====\n", ++i);
        System.out.println(bill);
        successfulBills.add(bill);
      }
    }
    int luachon;
    while (true) {
      System.out.print("==== BẠN CÓ MUỐN XEM CHI TIẾT HÓA ĐƠN ĐÃ THANH TOÁN NÀO KHÔNG ====\n");
      System.out.println("1. Có");
      System.out.println("2. Không");
      System.out.print("Nhập lựa chọn: ");
      try {
        luachon = Integer.parseInt(sc.nextLine());
        switch (luachon) {
          case 1:
            chooseBillToDisplayDetail(sc, successfulBills);
            break;
          case 2:
            return;
          default:
            System.err.println("Lựa chọn không hợp lệ. Hãy nhập lại");

        }
      } catch (NumberFormatException ex) {
        System.err.println("Lựa chọn không hợp lệ. Hãy nhập lại");
      }

    }
  }

  private void displayBills(ArrayList<Bill> bills) {
    for (int i = 0; i < bills.size(); i++) {
      System.out.printf("==== HÓA ĐƠN THỨ %d ====\n", i + 1);
      System.out.println(bills.get(i));
    }

  }

  private void displayBillDetail(Bill bill) {
    ArrayList<CartItem> cartItems = bill.getCartItems();
    for (int i = 0; i < cartItems.size(); i++) {
      System.out.printf("==== SẢN PHẨM THỨ %d ==== \n", i + 1);
      System.out.println(cartItems.get(i));
    }
  }

  private void chooseBillToDisplayDetail(Scanner sc, ArrayList<Bill> bills) {
    int billId;
    while (true) {
      displayBills(bills);
      System.out.print("Nhập id bill muốn xem chi tiết (Nếu muốn thoát nhập '-1'): ");
      try {
        billId = Integer.parseInt(sc.nextLine());
        if (billId == -1) {
          return;
        }
        if (billId < 0) {
          System.err.println("Id bill không hợp lệ");
          continue;
        }
        boolean isExist = false;
        for (Bill bill :
                bills) {
          if (bill.getId() == billId) {
            isExist = true;
            displayBillDetail(bill);
            break;
          }
        }
        if (!isExist) {
          System.err.println("Id bill vừa nhập không tồn tại trong danh sách đã hiện ra ở trên");
        }
        if (isExist)
          return;
      } catch (NumberFormatException ex) {
        System.err.println("Id bill không hợp lệ");
      }
    }
  }

  public void showCanceledBills(Account account, Scanner sc) {
    ArrayList<Bill> bills = account.getBills();
    if (bills.isEmpty()) {
      System.err.println("Danh sách hóa đơn đang trống");
      return;
    }
    int i = 0;
    ArrayList<Bill> cancelBills = new ArrayList<>();
    for (Bill bill :
            bills) {
      if (bill.getBillStatus() == BillStatus.DA_HUY) {
        System.out.printf("==== HÓA ĐƠN THỨ %d ====\n", ++i);
        System.out.println(bill);
        cancelBills.add(bill);
      }
    }
    int luachon;
    while (true) {
      System.out.print("==== BẠN CÓ MUỐN XEM CHI TIẾT HÓA ĐƠN ĐÃ HỦY NÀO KHÔNG ====\n");
      System.out.println("1. Có");
      System.out.println("2. Không");
      System.out.print("Nhập lựa chọn: ");
      try {
        luachon = Integer.parseInt(sc.nextLine());
        switch (luachon) {
          case 1:
            chooseBillToDisplayDetail(sc, cancelBills);
            break;
          case 2:
            return;
          default:
            System.err.println("Lựa chọn không hợp lệ. Hãy nhập lại");

        }
      } catch (NumberFormatException ex) {
        System.err.println("Lựa chọn không hợp lệ. Hãy nhập lại");
      }

    }
  }
}
