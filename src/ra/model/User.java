package ra.model;

public class User {
  private boolean sex;
  private String fullName;
  private String email;
  private String phoneNumber;

  public boolean isSex() {
    return sex;
  }

  public void setSex(boolean sex) {
    this.sex = sex;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  @Override
  public String toString() {
    return "Thông tin người dùng {" +
            "Giới tính=" + (sex ? "Nam" : "Nữ") +
            ", Họ và tên='" + fullName + '\'' +
            ", email='" + email + '\'' +
            ", Số điện thoại='" + phoneNumber + '\'' +
            '}';
  }


}
