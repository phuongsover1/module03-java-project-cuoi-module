package ra.model;

import ra.enums.Role;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Objects;

public class Account implements Serializable {
  private String username;
  private String password;
  private User userDetail;
  private boolean status = true;
  private Role role;
  private BigDecimal totalCurrentMoney = new BigDecimal(0);
  private ArrayList<CartItem> cartItems = new ArrayList<>();


  public Account() {
  }

  public Account(String username, String password) {
    this.username = username;
    this.password = password;
  }

  public BigDecimal getTotalCurrentMoney() {
    return totalCurrentMoney;
  }

  public void setTotalCurrentMoney(BigDecimal totalCurrentMoney) {
    this.totalCurrentMoney = totalCurrentMoney;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public User getUserDetail() {
    return userDetail;
  }

  public void setUserDetail(User userDetail) {
    this.userDetail = userDetail;
  }

  public boolean isStatus() {
    return status;
  }

  public void setStatus(boolean status) {
    this.status = status;
  }

  @Override
  public String toString() {
    return "Thông tin tài khoản{" +
            "Tên tài khoản='" + username + '\'' +
            ", Mật khẩu='" + password + '\'' +
             ",Tổng số tiền trong ví= " + totalCurrentMoney +
            ", Trạng thái=" + (status ? "Đang hoạt động" : "Đã bị khóa") +
            userDetail +
            '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Account account = (Account) o;
    return Objects.equals(username, account.username);
  }

  @Override
  public int hashCode() {
    return Objects.hash(username);
  }

  public ArrayList<CartItem> getCartItems() {
    return cartItems;
  }
}
