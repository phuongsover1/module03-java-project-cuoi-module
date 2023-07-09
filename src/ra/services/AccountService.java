package ra.services;

import ra.enums.Role;
import ra.enums.Sex;
import ra.model.Account;
import ra.model.User;
import ra.utility.Utility;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AccountService implements IServiceMapGenerics<Account, String> {
  private static final Map<String, Account> accountMap;
  private static final IOMapService<Account, String> IOAccount = new IOMapService<>();

  static {
    Map<String, Account> accountMapFromFile;
    accountMapFromFile = IOAccount.readFromFile(Utility.ACCOUNT_FILE);
    if (accountMapFromFile == null) {
      accountMapFromFile = new HashMap<>();
    }
    accountMap = accountMapFromFile;
  }

  @Override
  public void save(Account account) {
    accountMap.put(account.getUsername(), account);
    IOAccount.writeToFile(Utility.ACCOUNT_FILE, accountMap);
  }

  @Override
  public void delete(String username) {
    accountMap.remove(username);
    IOAccount.writeToFile(Utility.ACCOUNT_FILE, accountMap);
  }

  @Override
  public Optional<Account> findById(String username) {
    Optional<Account> optionalAccount = Optional.ofNullable(accountMap.get(username));
    return optionalAccount;
  }

  @Override
  public Map<String, Account> findAll() {
    return accountMap;
  }

  public void createNewAccount(Scanner sc) {
    Account newAccount = new Account();
    inputUsername(sc, newAccount);
    inputPassword(sc, newAccount);
    inputRole(sc, newAccount);
    inputAccountProfile(sc, newAccount);
    save(newAccount);
  }


  public void updateAccount(Scanner sc) {

  }

  private void inputAccountProfile(Scanner sc, Account account) {
    System.out.println("==== ĐÃ ĐĂNG KÍ TÀI KHOẢN THÀNH CÔNG. XIN HÃY NHẬP THÊM THÔNG TIN CHO TÀI KHOẢN ====");
    User user = new User();
    inputFullName(sc, user);
    inputEmail(sc, user);
    inputSex(sc, user);
    inputPhoneNumber(sc, user);
    System.out.println("==== CẬP NHẬT THÔNG TIN TÀI KHOẢN THÀNH CÔNG ====");
    account.setUserDetail(user);
  }

  private void inputFullName(Scanner sc, User user) {
    String fullName;
    while (true) {
      System.out.print("Nhập đầy đủ họ và tên: ");
      fullName = sc.nextLine().trim();
      if (fullName.equals("")) {
        System.err.println("Họ tên không hợp lệ. Xin vui lòng nhập lại!!!");
        continue;
      }
      user.setFullName(fullName);
      break;
    }
  }

  private void inputEmail(Scanner sc, User user) {
    String email;
    Pattern emailPattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$",Pattern.CASE_INSENSITIVE);

    while (true) {
      System.out.print("Nhập email của bạn: ");
      email = sc.nextLine().trim();
      if (email.equals("")) {
        System.err.println("Email không được để trống. Xin vui lòng nhập lại!!!");
        continue;
      }
      Matcher emailMatcher = emailPattern.matcher(email);
      if (!emailMatcher.matches()) {
        System.err.println("Email đã nhập khong đúng định dạng. Xin vui lòng nhập lại!!!");
        continue;
      }
      user.setEmail(email);
      break;
    }
  }

  private void inputSex(Scanner sc, User user) {
    int luachon;
    while(true) {
      try {
        System.out.println("Chọn giới tính của bạn: ");
        System.out.println("1. Nam");
        System.out.println("2. Nữ");
        System.out.print("Lựa chọn của bạn: ");
        luachon = Integer.parseInt(sc.nextLine());
        switch (luachon) {
          case 1 -> user.setSex(Sex.NAM);
          case 2 -> user.setSex(Sex.NU);
          default -> {
            System.err.println("Lựa chọn không hợp lệ. Hãy nhập lại");
            continue;
          }
        }
        break;
      } catch (NumberFormatException ex) {
        System.err.println("Lựa chọn không hợp lệ. Hãy nhập lại!!!");
      }
    }
  }

  private void inputPhoneNumber(Scanner sc, User user){
    String phoneNumber;
    Pattern phoneNumberPatter = Pattern.compile("^0[0-9]{9,10}$");
    while (true) {
      System.out.print("Nhập số điện thoại (có độ dài là 10 hoặc 11 số, bắt đầu bằng số 0: ");
      phoneNumber = sc.nextLine().trim();
      if (phoneNumber.equals("")) {
        System.err.println("Số điện thoại không được để trống. Xin vui lòng nhập lại!!!");
        continue;
      }
      System.out.println(phoneNumber.length());
      Matcher phoneNumberMatcher = phoneNumberPatter.matcher(phoneNumber);
      if (!phoneNumberMatcher.matches()) {
        System.err.println("Định dạng số điện thoại không hợp lệ. Xin vui lòng nhập lại ");
        continue;
      }
      user.setPhoneNumber(phoneNumber);
      break;
    }
  }

  private void inputUsername(Scanner sc, Account account) {
    String username;
    while (true) {
      System.out.print("Nhập tên tài khoản: ");
      username = sc.nextLine().trim();
      if (username.equals("")) {
        System.err.println("Tên tài khoản không hợp lệ. Xin vui lòng nhập lại!!!");
        continue;
      }
      if (accountMap.containsKey(username)) {
        System.err.println("Tên tài khoản đã tồn tại. Xin vui lòng chọn tên khác!!! ");
        continue;
      }
      account.setUsername(username);
      break;
    }
  }

  private void inputPassword(Scanner sc, Account account) {
    String password;
    Pattern haveAtLeastOneNumber = Pattern.compile("[0-9]+");
    Pattern haveAtLeastOneUppercaseCharacter = Pattern.compile("[A-Z]+");
    Pattern haveAtLeastFiveCharacters = Pattern.compile(".{5,}");
    Matcher oneNumberMatcher = null;
    Matcher oneUppercaseCharacter = null;
    Matcher fiveCharacters = null;
    while (true) {
      System.out.print("Nhập mật khẩu (Có ít nhất 5 kí tự,bao gồm 1 chữ in hoa, 1 chữ số): ");
      password = sc.nextLine().trim();
      if (password.equals("")) {
        System.err.println("Mật khẩu không hợp lệ. Xin vui lòng nhập lại.");
        continue;
      }
      oneNumberMatcher = haveAtLeastOneNumber.matcher(password);
      if (!oneNumberMatcher.find()) {
        System.err.println("Mật khẩu phải chứa ít nhất một chữ số. Xin vui lòng nhập lại.");
        continue;
      }

      oneUppercaseCharacter = haveAtLeastOneUppercaseCharacter.matcher(password);
      if (!oneUppercaseCharacter.find()) {
        System.err.println("Mật khẩu phải chứa ít nhất một chữ in hoa. Xin vui lòng nhập lại.");
        continue;
      }

      fiveCharacters = haveAtLeastFiveCharacters.matcher(password);
      if (!fiveCharacters.find()) {
        System.err.println("Mật khẩu phải ít nhất 5 ký tự. Xin vui lòng nhập lại.");
        continue;
      }
      account.setPassword(password);
      break;
    }
  }

  private void inputRole(Scanner sc, Account account) {
    int luachon;
    while (true) {
      try {
        displayRoleMenu();
        luachon = Integer.parseInt(sc.nextLine());
        if (luachon < 1 || luachon > 2) {
          System.err.println("Lựa chọn không hợp lệ");
          continue;
        }
        if (luachon == 1)
          account.setRole(Role.USER);
        if (luachon == 2)
          account.setRole(Role.ADMIN);
        break;

      } catch (NumberFormatException ex) {
        System.err.println("Lựa chọn không hợp lệ");
      }
    }
  }

  private void displayRoleMenu() {
    System.out.println("==== CHỌN VAI TRÒ CỦA TÀI KHOẢN ====");
    System.out.println("1. USER");
    System.out.println("2. ADMIN");
    System.out.print("Nhập lựa chọn: ");
  }

  public Optional<Account> getLoginAccount(String username, String password) {
    Optional<Account> accountOptional = Optional.ofNullable(null);
    Optional<Account> returnedAccountOptional = findById(username);
    if (returnedAccountOptional.isPresent()) {
      Account tempAccount = returnedAccountOptional.get();
      if (tempAccount.getPassword().equals(password)) {
        if (tempAccount.isStatus()) {
          accountOptional = returnedAccountOptional;
        } else {
          System.err.println("Tài khoản đã bị khóa!!!");
        }
      } else {
        System.err.println("Mật khẩu không đúng!!!");
      }

    } else {
      System.err.println("Tên tài khoản không đúng!!!");
    }
    return accountOptional;
  }


}
