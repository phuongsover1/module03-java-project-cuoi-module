package ra.services;

import ra.model.Category;
import ra.model.Product;
import ra.utility.Utility;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.Scanner;

public class ProductService implements IServiceCollectionGenerics<Product, Integer> {

  private static final ArrayList<Product> products;
  private static final IOCollectionService<Product> IOProduct = new IOCollectionService<>(Utility.PRODUCT_FILE);
  private static final CategoryService categoryService = new CategoryService();

  static {
    Collection<Product> productCollectionFromFile;
    productCollectionFromFile = IOProduct.readFromFile();
    if (productCollectionFromFile == null) {
      productCollectionFromFile = new ArrayList<>();
    }
    products = (ArrayList<Product>) productCollectionFromFile;
  }

  public static ArrayList<Product> getProducts() {
    return products;
  }

  public void writeToFile() {
    IOProduct.writeToFile(products);
  }

  @Override
  public void save(Product entity) {
    products.add(entity);
    IOProduct.writeToFile(products);
  }

  @Override
  public void delete(Integer id) {
    try {
      products.remove(id.intValue());

      for (int i = id; i < products.size(); i++) {
        Product product = products.get(i);
        product.setId(product.getId() - 1);
      }
      IOProduct.writeToFile(products);
    } catch (IndexOutOfBoundsException ex) {
      System.err.println("id không tồn tại. Xóa thất bại");
    }
  }

  @Override
  public Optional<Product> findById(Integer id) {
    Optional<Product> productOptional;
    try {
      Product product = products.get(id.intValue());
      productOptional = Optional.of(product);
    } catch (IndexOutOfBoundsException ex) {
      productOptional = Optional.empty();
    }
    return productOptional;
  }

  @Override
  public Collection<Product> findAll() {
    return products;
  }

  public void createProduct(Scanner adminSC) {
    //Kiểm tra đã tồn tại danh mục trước khi thêm sản phẩm
    if (categoryService.findAll().isEmpty()) {
      System.err.println("Danh sách danh mục đang trống. Xin hãy thêm danh mục trước");
      return;
    }
    Product product = new Product();
    inputName(adminSC, product);
    inputPrice(adminSC, product);
    inputCategory(adminSC, product);
    inputStock(adminSC, product);
    save(product);
  }

  private void inputName(Scanner sc, Product product) {
    String name;
    while (true) {
      System.out.print("Hãy nhập tên sản phẩm: ");
      name = sc.nextLine().trim();
      if (name.equals("")) {
        System.err.println("Tên sản phẩm không được để trống. Hãy nhập lại");
      } else {
        product.setName(name);
        break;
      }
    }
  }

  private void inputPrice(Scanner sc, Product product) {
    double price;
    while (true) {
      System.out.print("Hãy nhập giá của sản phẩm: ");
      try {
        price = Double.parseDouble(sc.nextLine());
        if (price < 0) {
          System.err.println("Giá phải >= 0. Hãy nhập lại");
          continue;
        }
        product.setPrice(new BigDecimal(price));
        break;
      } catch (NumberFormatException ex) {
        System.err.println("Giá khong hợp lệ. Hãy nhập lại");
      }
    }
  }

  private void inputCategory(Scanner sc, Product product) {
    if (categoryService.findAll().isEmpty()) {
      System.err.println("Danh sách danh mục đang trống. Xin hãy thêm danh mục trước");
      return;
    }
    int idCategory;
    while (true) {
      try {
        System.out.print("Nhập id danh mục muốn mới : ");
        idCategory = Integer.parseInt(sc.nextLine());

        Optional<Category> categoryOptional = categoryService.findById(idCategory);
        if (!categoryOptional.isPresent()) {
          System.err.printf("Danh mục với id %d không tồn tại\n", idCategory);
        } else {
          product.setCategory(categoryOptional.get());
          return;
        }
      } catch (NumberFormatException ex) {
        System.err.println("Id danh mục không hợp lệ. Hãy nhập lại.");
      }
    }
  }

  private void inputStock(Scanner sc, Product product) {
    int stock;
    while (true) {
      System.out.print("Nhập số lượng tồn: ");
      try {
        stock = Integer.parseInt(sc.nextLine());
        if (stock < 0) {
          System.err.println("Số lượng tồn không hợp lệ. Hãy nhập lại");
          continue;
        }
        product.setStock(stock);
        break;
      } catch (NumberFormatException ex) {
        System.err.println("Số lượng tồn không hợp lê. Hãy nhập lại");
      }
    }
  }

  public void displayProducts() {
    if (products.isEmpty()) {
      System.err.println("Danh sách sản phẩm đang trống. Hãy thêm sản phẩm trước");
      return;
    }
    for (int i = 0; i < products.size(); i++) {
      System.out.printf("==== Sản phẩm thứ %d ==== \n", i);
      System.out.println(products.get(i));
    }
  }

  public void updateProduct(Scanner sc) {
    if (products.isEmpty()) {
      System.err.println("Danh sách sản phẩm đang trống. Hãy thêm sản phẩm trước");
      return;
    }

    int idProduct;
    while (true) {
      try {
        System.out.print("Nhập id sản phẩm muốn sửa (Nếu muốn hủy nhập '-1'): ");
        idProduct = Integer.parseInt(sc.nextLine());
        if (idProduct == -1) {
          return;
        }

        Optional<Product> productOptional = findById(idProduct);
        if (!productOptional.isPresent()) {
          System.err.printf("Sản phẩm với id %d không tồn tại\n", idProduct);
        } else {
          int luachon;
          Product product = productOptional.get();
          while (true) {
            updateProductMenu();
            try {
              luachon = Integer.parseInt(sc.nextLine());
              switch (luachon) {

                case 1:
                  inputName(sc, product);
                  IOProduct.writeToFile(products);
                  break;
                case 2:
                  inputPrice(sc, product);
                  IOProduct.writeToFile(products);
                  break;
                case 3:
                  inputCategory(sc, product);
                  IOProduct.writeToFile(products);
                  break;
                case 4:
                  inputStock(sc, product);
                  IOProduct.writeToFile(products);
                  break;
                case 5:
                  inputStatus(sc, product);
                  IOProduct.writeToFile(products);
                  break;
                case 6:
                  return;
                default:
                  System.err.println("Lựa chọn không hợp lệ. Hãy nhập lại");

              }
            } catch (NumberFormatException ex) {
              System.err.println("Lựa chọn không hợp lệ. Hãy nhập lại");
            }

          }
        }
      } catch (NumberFormatException ex) {
        System.err.println("Id danh mục không hợp lệ. Hãy nhập lại.");
      }
    }
  }

  private void inputStatus(Scanner sc, Product product) {
    int luachon;
    while (true) {

      System.out.println("==== CHỌN STATUS MUỐN THAY ĐỔI ====");
      System.out.println("1. Đang được bán");
      System.out.println("2. Ngừng bán");
      System.out.print("Nhập lựa chọn: ");
      try {
        luachon = Integer.parseInt(sc.nextLine());

        switch (luachon) {
          case 1:
            product.setStatus(true);
            return;
          case 2:
            product.setStatus(false);
            return;
          default:
            System.err.println("Lựa chọn không hợp lệ. Hãy chọn lại");
        }
      } catch (NumberFormatException ex) {
        System.err.println("Lựa chọn không hợp lê. Hãy chọn lại ");
      }
    }

  }

  private void updateProductMenu() {
    System.out.println("==== UPDATE SẢN PHẨM ====");
    System.out.println("1. Thay đổi tên");
    System.out.println("2. Thay đổi giá");
    System.out.println("3. Thay đổi danh mục");
    System.out.println("4. Thay đổi số lượng tồn");
    System.out.println("5. thay đổi trạng thái");
    System.out.println("6. Thoát");
    System.out.print("Nhập lựa chọn: ");
  }

  public void deleteProduct(Scanner sc) {
    if (findAll().isEmpty()) {
      System.err.println("Danh sách sản phẩm đang trống. Xin hãy thêm sản phẩm trước");
      return;
    }
    int idProduct;
    while (true) {
      try {
        System.out.print("Nhập id sản phẩm mục muốn sửa (Nếu muốn hủy nhập '-1'): ");
        idProduct = Integer.parseInt(sc.nextLine());
        if (idProduct == -1) {
          return;
        }

        Optional<Product> productOptional = findById(idProduct);
        if (!productOptional.isPresent()) {
          System.err.printf("Sản phẩm với id %d không tồn tại\n", idProduct);
        } else {
          delete(idProduct);
          System.out.println("Xóa sản phẩm thành công");
          return;
        }
      } catch (NumberFormatException ex) {
        System.err.println("Id sản phẩm không hợp lệ. Hãy nhập lại.");
      }
    }
  }

  public void findProduct(Scanner sc) {
    int luachon;
    while (true) {
      findProductMenu();
      try {
        luachon = Integer.parseInt(sc.nextLine());

        switch (luachon) {
          case 1:
            findProductByName(sc);
            return;
          case 2:
            findProductByCategoryName(sc);
            return;
          case 3:
            return;
          default:
            System.err.println("Lựa chọn không hợp lệ. Hãy nhập lại");
        }
      } catch (NumberFormatException ex) {
        System.err.println("Lựa chọn không hợp lệ. Hãy nhập lại");
      }
    }
  }

  private void findProductByName(Scanner sc) {
    String productName;
    while (true) {
      System.out.print("Nhập tên hoặc một phần của sản phẩm mà bạn muốn tìm kiếm: ");
      productName = sc.nextLine().trim();
      if (productName.equals("")) {
        System.err.println("Tên sản phẩm không được để trống được để trống");
        continue;
      }
      System.out.println("==== DANH SÁCH SẢN PHẨM TÌM THẤY ====");
      int i = 0;
      for (Product product :
              products) {
        if (product.getName().toLowerCase().contains(productName.toLowerCase())) {
          System.out.printf(" ==== Sản phẩm thứ %d ==== \n", ++i);
          System.out.println(product);
        }
      }
      break;
    }
  }

  private void findProductByCategoryName(Scanner sc) {
    String categoryName;
    while (true) {
      System.out.print("Nhập tên hoặc một phần của danh mục mà bạn muốn tìm kiếm: ");
      categoryName = sc.nextLine().trim();
      if (categoryName.equals("")) {
        System.err.println("Tên danh mục để trống được để trống");
        continue;
      }
      System.out.println("==== DANH SÁCH SẢN PHẨM TÌM THẤY ====");
      int i = 0;
      for (Product product :
              products) {
        if (product.getCategory().getName().toLowerCase().contains(categoryName.toLowerCase())) {
          System.out.printf(" ==== Sản phẩm thứ %d ==== \n", ++i);
          System.out.println(product);
        }
      }
      break;
    }
  }

  private void findProductMenu() {
    System.out.println("==== TÌM KIẾM SẢN PHẨM ==== ");
    System.out.println("1.Theo tên");
    System.out.println("2.Theo tên danh mục");
    System.out.println("3.Thoát");
    System.out.print("Nhập lựa chọn: ");
  }
}
