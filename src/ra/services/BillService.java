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
    if (account.getCartItems().isEmpty()) {
      System.err.println("Giỏ hàng đang trống. Hãy cho vật phẩm vào giỏ hàng trước");
      return;
    }

    Bill newBill = new Bill(account);
    newBill.setCartItems(new ArrayList<>(account.getCartItems()));
    BigDecimal totalMoney = new BigDecimal(0);
    for (CartItem cartItem :
            account.getCartItems()) {
      BigDecimal result = cartItem.getProduct().getPrice().multiply(new BigDecimal(cartItem.getQuantity()));
      totalMoney = totalMoney.add(result);
    }

    BigDecimal remainMoney = account.getTotalCurrentMoney().subtract(totalMoney);
    if (remainMoney.compareTo(new BigDecimal(0)) < 0) {
      System.err.println("Tài khoản không có đủ tiền, xin hãy nạp thêm");
      return;
    }
    // Trừ tiền
    account.setTotalCurrentMoney(remainMoney);
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
    System.out.println("==== DANH SÁCH HÓA ĐƠN ĐANG ĐƯỢC XỬ LÝ ====");
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
    System.out.println("==== DANH SÁCH HÓA ĐƠN ĐÃ THANH TOÁN THÀNH CÔNG ====");
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

    System.out.println("==== DANH SÁCH HÓA ĐƠN ĐÃ HỦY THANH TOÁN ====");
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

  public void cancelBill(Account currentAccount, Scanner sc) {
    ArrayList<Bill> accountBills = currentAccount.getBills();
    if (accountBills.isEmpty()) {
      System.err.println("Danh sách hóa đơn đang trống");
      return;
    }
    ArrayList<Bill> processingBills = new ArrayList<>();
    for (Bill bill :
            accountBills) {
      if (bill.getBillStatus() == BillStatus.DANG_XU_LY) {
        processingBills.add(bill);
      }
    }
    if(processingBills.isEmpty()){
      System.err.println("Hiện tại không có hóa đơn nào đang chờ được xử lý để mà hủy.");
      return;
    }
    int idBill;
    while(true) {
      displayBills(processingBills);
      System.out.print("Hãy nhập id bill mà bạn muốn hủy. (Nhập '-1' nếu muốn thoát): ");
      try {
        idBill = Integer.parseInt(sc.nextLine());
        if (idBill == -1){
          return;
        }
        boolean isExist = false;
        for (Bill bill:
             processingBills) {
          if (bill.getId() == idBill) {
            isExist =true;
            bill.setBillStatus(BillStatus.DA_HUY);
            // TODO: Cộng tiền lại vào tài khoản
            BigDecimal totalMoney = new BigDecimal(0);
            for (CartItem cartItem :
                    bill.getCartItems()) {
              BigDecimal result = cartItem.getProduct().getPrice().multiply(new BigDecimal(cartItem.getQuantity()));
              totalMoney = totalMoney.add(result);
            }
            currentAccount.setTotalCurrentMoney(currentAccount.getTotalCurrentMoney().add(totalMoney));
            System.out.println("==== ĐÃ HỦY ĐƠN THÀNH CÔNG. SỐ TIỀN " + totalMoney + " ĐÃ ĐƯỢC TRẢ VỀ TÀI KHOẢN ====");
            accountService.writeToFile();
            return;
          }
        }
        if (!isExist) {
          System.err.println("Id bill bạn nhập vào không tồn tại. Hãy nhập lại");
        }
      }catch (NumberFormatException ex) {
        System.err.println("Id của bill không hợp lệ. Hãy nhập lại.");
      }
    }

  }
}
