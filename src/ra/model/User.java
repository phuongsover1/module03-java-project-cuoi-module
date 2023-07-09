package ra.model;

import ra.enums.Sex;

import java.io.Serializable;

public class User implements Serializable {
  private Sex sex;
  private String fullName;
  private String email;
  private String phoneNumber;

  public Sex getSex() {
    return sex;
  }

  public void setSex(Sex sex) {
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
            "Giới tính=" + sex.name() +
            ", Họ và tên='" + fullName + '\'' +
            ", email='" + email + '\'' +
            ", Số điện thoại='" + phoneNumber + '\'' +
            '}';
  }


}
