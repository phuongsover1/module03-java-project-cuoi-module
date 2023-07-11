package ra.services;

import ra.controllers.AuthController;
import ra.enums.Role;
import ra.enums.Sex;
import ra.model.Account;
import ra.model.User;
import ra.utility.Utility;

import java.util.*;
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

  private void writeToFile() {
    IOAccount.writeToFile(Utility.ACCOUNT_FILE, accountMap);
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

  public void createAdminAccount(Scanner sc) {
    Account newAccount = new Account();
    newAccount.setRole(Role.ADMIN);
    inputUsername(sc, newAccount);
    inputPassword(sc, newAccount);
    inputAccountProfile(sc, newAccount);
    save(newAccount);
  }


  public void updateAccountMenu(Scanner sc) {
    int luachon;
    while (true) {
      try {
        displayUpdateAccountMenu();
        luachon = Integer.parseInt(sc.nextLine());

        switch (luachon) {
          case 1:
            inputFullName(sc, AuthController.currentAccount.getUserDetail());
            writeToFile();
            break;
          case 2:
            changeEmail(sc, AuthController.currentAccount.getUserDetail());
            writeToFile();
            break;

          case 3:
            changePhoneNumber(sc, AuthController.currentAccount
                    .getUserDetail());
            writeToFile();
            break;

          case 4:
            inputSex(sc, AuthController.currentAccount.getUserDetail());
            writeToFile();
            break;

          case 5:
            changePassword(sc, AuthController.currentAccount);
            writeToFile();
            break;

          case 6:
            return;

          default:
            System.err.println("Lựa chọn không hợp lệ.");


        }
        // TODO: LLàm chức năng user
      } catch (NumberFormatException ex) {
        System.err.println("Lựa chọn không hợp lệ.");
      }
    }

  }

  private void changeEmail(Scanner sc, User user) {
    String email;
    Pattern emailPattern =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$", Pattern.CASE_INSENSITIVE);

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

      //Nếu nhập giống email ban đầu thì không thay đổi

      // nếu khác email ban đầu đã nhập
      String oldEmail = user.getEmail();
      if (oldEmail != null && oldEmail.equals(email)) {
        System.out.println("Email đã nhập giống với email mà bạn đã đăng kí cho tài khoản này. Không thay đổi email");
        break;
      } else {
        List<String> allEmail = accountMap.values().parallelStream()
                .map(account -> account.getUserDetail().getEmail()).sorted(new Comparator<String>() {
                  @Override
                  public int compare(String o1, String o2) {
                    return o1.compareToIgnoreCase(o2);
                  }
                }).toList();
        int indexEmailExist = Collections.binarySearch(allEmail, email);
        if (indexEmailExist >= 0) {
          System.err.println("Email đã tồn tại trên tài khoản khác. Xin vui lòng chọn email khác!!!");
          continue;
        }
        System.out.println("Thay đổi email thành công");
        user.setEmail(email);
        break;
      }
    }
  }

  private void changePhoneNumber(Scanner sc, User user) {
    String phoneNumber;
    Pattern phoneNumberPatter = Pattern.compile("^0[0-9]{9,10}$");
    while (true) {
      System.out.print("Nhập số điện thoại (có độ dài là 10 hoặc 11 số, bắt đầu bằng số 0: ");
      phoneNumber = sc.nextLine().trim();
      if (phoneNumber.equals("")) {
        System.err.println("Số điện thoại không được để trống. Xin vui lòng nhập lại!!!");
        continue;
      }
      Matcher phoneNumberMatcher = phoneNumberPatter.matcher(phoneNumber);
      if (!phoneNumberMatcher.matches()) {
        System.err.println("Định dạng số điện thoại không hợp lệ. Xin vui lòng nhập lại ");
        continue;
      }
      String oldPhoneNumber = user.getPhoneNumber();
      if (oldPhoneNumber != null && oldPhoneNumber.equals(phoneNumber)) {
        System.out.println("Số điện thoại đã nhập giống với số điện thoại mà bạn đã đăng kí cho tài khoản này. Không thay đổi số điện thoại");
        break;
      }

      List<String> allPhoneNumber = accountMap.values().parallelStream()
              .map(account -> account.getUserDetail().getPhoneNumber())
              .sorted(new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                  return o1.compareTo(o2);
                }
              }).toList();
      int indexPhoneNumberlExist = Collections.binarySearch(allPhoneNumber, phoneNumber);
      if (indexPhoneNumberlExist >= 0) {
        System.err.println(
                "Số điện thoại đã tồn tại trên tài khoản khác. Xin vui lòng chọn số điện thoại khác!!!");
        continue;
      }
      System.out.println("Thay đổi số điện thoại thành công");
      user.setPhoneNumber(phoneNumber);
      break;
    }
  }

  private void changePassword(Scanner sc, Account account) {
    String oldPassword = account.getPassword();
    String oldTypedPassword;
    while (true) {
      System.out.print("Hãy nhập mật khẩu cũ: ");
      oldTypedPassword = sc.nextLine();

      if (!oldTypedPassword.equals(oldPassword)) {
        System.err.println("Mật khẩu cũ bạn đã nhập không đúng");
      } else {
        String newPassword;
        Pattern haveAtLeastOneNumber = Pattern.compile("[0-9]+");
        Pattern haveAtLeastOneUppercaseCharacter = Pattern.compile("[A-Z]+");
        Pattern haveAtLeastFiveCharacters = Pattern.compile(".{5,}");
        Matcher oneNumberMatcher = null;
        Matcher oneUppercaseCharacter = null;
        Matcher fiveCharacters = null;
        while (true) {
          System.out.print("Nhập mật khẩu (Có ít nhất 5 kí tự,bao gồm 1 chữ in hoa, 1 chữ số): ");
          newPassword = sc.nextLine().trim();
          if (newPassword.equals("")) {
            System.err.println("Mật khẩu không hợp lệ. Xin vui lòng nhập lại.");
            continue;
          }
          oneNumberMatcher = haveAtLeastOneNumber.matcher(newPassword);
          if (!oneNumberMatcher.find()) {
            System.err.println("Mật khẩu phải chứa ít nhất một chữ số. Xin vui lòng nhập lại.");
            continue;
          }

          oneUppercaseCharacter = haveAtLeastOneUppercaseCharacter.matcher(newPassword);
          if (!oneUppercaseCharacter.find()) {
            System.err.println("Mật khẩu phải chứa ít nhất một chữ in hoa. Xin vui lòng nhập lại.");
            continue;
          }

          fiveCharacters = haveAtLeastFiveCharacters.matcher(newPassword);
          if (!fiveCharacters.find()) {
            System.err.println("Mật khẩu phải ít nhất 5 ký tự. Xin vui lòng nhập lại.");
            continue;
          }

          String newTypedPassword;
          while (true) {
            System.out.print(
                    "Hãy nhập lại mật khẩu mới (Nếu không nhớ mật khẩu vừa nhập thì nhập '-1' để bắt đầu lại quá trình đổi mật khẩu): ");
            newTypedPassword = sc.nextLine();

            if (newTypedPassword.equals("-1")) {
              System.out.println("Thay đổi mật khẩu mới không thành công");
              break;
            }

            if (newTypedPassword.equals(newPassword)) {
              System.out.println("Thay đổi mật khẩu thành công");
              break;
            } else {
              System.err.println("Mật khẩu nhập lại không đúng");
            }

          }

          if (newTypedPassword.equals(newPassword))
            break;
        }
        // sau khi nhập đúng mật khẩu mới 2 lần thì mới set lại mật khẩu
        account.setPassword(newPassword);
        break;
      }
    }
  }


  private void displayUpdateAccountMenu() {
    System.out.println("==== THÔNG TIN TÀI KHOẢN ====");
    System.out.println(AuthController.currentAccount);
    System.out.println("==== CHỨC NĂNG TRONG QUẢN LÝ PROFILE ====");
    System.out.println("1.Thay đổi họ và tên");
    System.out.println("2.Thay đổi email");
    System.out.println("3.Thay đổi số điện thoại");
    System.out.println("4.Thay đổi giới tính");
    System.out.println("5.Thay đổi mật khẩu");
    System.out.println("6.Thoát");
    System.out.print("Nhập lựa chọn: ");

  }

  private void inputAccountProfile(Scanner sc, Account account) {
    System.out.println(
            "==== ĐÃ ĐĂNG KÍ TÀI KHOẢN THÀNH CÔNG. XIN HÃY NHẬP THÊM THÔNG TIN CHO TÀI KHOẢN ====");
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
    Pattern emailPattern =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$", Pattern.CASE_INSENSITIVE);

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
      List<String> allEmail = accountMap.values().parallelStream()
              .map(account -> account.getUserDetail().getEmail()).sorted(new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                  return o1.compareToIgnoreCase(o2);
                }
              }).toList();
      int indexEmailExist = Collections.binarySearch(allEmail, email);
      if (indexEmailExist >= 0) {
        System.err.println("Email đã tồn tại trên tài khoản khác. Xin vui lòng chọn email khác!!!");
        continue;
      }
      user.setEmail(email);
      break;
    }
  }

  private void inputSex(Scanner sc, User user) {
    int luachon;
    while (true) {
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

  private void inputPhoneNumber(Scanner sc, User user) {
    String phoneNumber;
    Pattern phoneNumberPatter = Pattern.compile("^0[0-9]{9,10}$");
    while (true) {
      System.out.print("Nhập số điện thoại (có độ dài là 10 hoặc 11 số, bắt đầu bằng số 0: ");
      phoneNumber = sc.nextLine().trim();
      if (phoneNumber.equals("")) {
        System.err.println("Số điện thoại không được để trống. Xin vui lòng nhập lại!!!");
        continue;
      }
      Matcher phoneNumberMatcher = phoneNumberPatter.matcher(phoneNumber);
      if (!phoneNumberMatcher.matches()) {
        System.err.println("Định dạng số điện thoại không hợp lệ. Xin vui lòng nhập lại ");
        continue;
      }

      List<String> allPhoneNumber = accountMap.values().parallelStream()
              .map(account -> account.getUserDetail().getPhoneNumber())
              .sorted(new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                  return o1.compareTo(o2);
                }
              }).toList();
      int indexPhoneNumberlExist = Collections.binarySearch(allPhoneNumber, phoneNumber);
      if (indexPhoneNumberlExist >= 0) {
        System.err.println(
                "Số điện thoại đã tồn tại trên tài khoản khác. Xin vui lòng chọn số điện thoại khác!!!");
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

  public void changeAccountStatus(Scanner sc) {
    String username;
    while (true) {
      System.out.print("Nhập tên tài khoản mà muốn thay đổi (Nhập 0 nếu muốn thoát): ");
      username = sc.nextLine().trim();
      if (username.equals("")) {
        System.err.println("Tên tài khoản không được để trống. Xin vui lòng nhập lại ");
        continue;
      }
      if (username.equals("0")) {
        break;
      }
      Optional<Account> accountOptional = findById(username);
      if (accountOptional.isPresent()) {
        Account account = accountOptional.get();
        System.out.println("==== THÔNG TIN TÀI KHOẢN ====");
        System.out.println("Tên tài khoản: " + account.getUsername());
        System.out.println("Quyền tài khoản: " + account.getRole());
        System.out.println(
                "Trạng thái tài khoản: " + (account.isStatus() ? "Đang hoạt động" : "Đã bị khóa"));

        if (account.isStatus()) {
          int luachon;
          while (true) {
            try {
              System.out.println("Bạn có muốn khóa tài khoản ?");
              System.out.println("1. Có");
              System.out.println("2. Không");
              luachon = Integer.parseInt(sc.nextLine());
              if (luachon < 1 || luachon > 2) {
                System.err.println("Lựa chọn không hợp lệ.");
                continue;
              }

              if (luachon == 1) {
                account.setStatus(false);
                System.out.println("Khóa tài khoản thành công");
                IOAccount.writeToFile(Utility.ACCOUNT_FILE, accountMap);
              }
              break;
            } catch (NumberFormatException ex) {
              System.err.println("Lựa chọn không hợp lệ");
            }
          }
        } else {
          int luachon;
          while (true) {
            try {
              System.out.println("Bạn có muốn mở khóa tài khoản ?");
              System.out.println("1. Có");
              System.out.println("2. Không");
              luachon = Integer.parseInt(sc.nextLine());
              if (luachon < 1 || luachon > 2) {
                System.err.println("Lựa chọn không hợp lệ.");
                continue;
              }

              if (luachon == 1) {
                account.setStatus(true);
                System.out.println("Mở khóa tài khoản thành công");
                IOAccount.writeToFile(Utility.ACCOUNT_FILE, accountMap);
              }
              break;
            } catch (NumberFormatException ex) {
              System.err.println("Lựa chọn không hợp lệ");
            }
          }
        }
      } else {
        System.err.println("Không tồn tại tài khoản nào ứng với tên tài khoản ");
      }
    }

  }

}
