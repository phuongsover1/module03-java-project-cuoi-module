package ra.model;

import ra.enums.BillStatus;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

public class Bill implements Serializable {
  private final int id;
  private BillStatus billStatus = BillStatus.DANG_XU_LY;
  private BigDecimal totalMoney;
  private ArrayList<CartItem> cartItems;
  private int staffId;

  public Bill(Account account) {
    ArrayList<Bill> bills = account.getBills();
    if (bills.isEmpty()) {
      id = 0;
    } else {
      id = bills.get(bills.size() - 1).id + 1;
    }
  }

  public int getId() {
    return id;
  }


  public BillStatus getBillStatus() {
    return billStatus;
  }

  public void setBillStatus(BillStatus billStatus) {
    this.billStatus = billStatus;
  }

  public BigDecimal getTotalMoney() {
    return totalMoney;
  }

  public void setTotalMoney(BigDecimal totalMoney) {
    this.totalMoney = totalMoney;
  }

  public ArrayList<CartItem> getCartItems() {
    return cartItems;
  }

  public void setCartItems(ArrayList<CartItem> cartItems) {
    this.cartItems = cartItems;
  }

  public int getStaffId() {
    return staffId;
  }

  public void setStaffId(int staffId) {
    this.staffId = staffId;
  }

  @Override
  public String toString() {
    return "ID: " + id + "\n" +
            "Tổng số tiền: " + totalMoney + "\n" +
            "Trạng thái đơn hàng: " + (billStatus == BillStatus.DANG_XU_LY ? "Đang xử lý" : (billStatus == BillStatus.DA_THANH_TOAN) ? "Đã thanh toán" : "Đã hủy");
  }
}
