package ra.services;

import ra.enums.Role;
import ra.model.Account;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AccountService implements IServiceMapGenerics<Account, String> {
  private static final Map<String, Account> accountMap = new HashMap<>();

  @Override
  public void save(Account account) {
    accountMap.put(account.getUsername(), account);
  }

  @Override
  public void delete(String username) {
    accountMap.remove(username);
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
    save(newAccount);
  }

  public void updateAccount(Scanner sc) {

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
        accountOptional = returnedAccountOptional;
      } else {
        System.err.println("Mật khẩu không đúng!!!");
      }
    } else {
      System.err.println("Tên tài khoản không đúng!!!");
    }
    return accountOptional;
  }


}
