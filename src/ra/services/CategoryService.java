package ra.services;

import ra.model.Category;
import ra.utility.Utility;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.Scanner;

public class CategoryService implements IServiceCollectionGenerics<Category, Integer> {
  private static final IOCollectionService<Category> IOCategory = new IOCollectionService<>();

  // TODO: Đọc từ file lên
  private static final ArrayList<Category> categories;

  static {
    Collection<Category> categoryCollectionFromFile = null;
    categoryCollectionFromFile = IOCategory.readFromFile(Utility.CATEGORY_FILE);
    if (categoryCollectionFromFile == null) {
      categoryCollectionFromFile = new ArrayList<>();
    }
    categories = (ArrayList<Category>) categoryCollectionFromFile;
  }

  public static ArrayList<Category> getCategories() {
    return categories;
  }

  private void writeToFile() {
    IOCategory.writeToFile(Utility.CATEGORY_FILE, categories);
  }

  public void createCategory(Scanner sc) {
    Category category = new Category();
    inputName(sc, category);
    save(category);
  }

  private void inputName(Scanner sc, Category category) {
    String name;
    while (true) {
      System.out.print("Hãy nhập tên danh mục: ");
      name = sc.nextLine().trim();
      if (name.equals("")) {
        System.err.println("Tên danh mục không được để trống. Hãy nhập lại");
      } else {
        category.setName(name);
        break;
      }
    }
  }

  public void displayCategories() {
    ArrayList<Category> categories = (ArrayList<Category>) findAll();
    if (categories.isEmpty()) {
      System.out.println("Danh sách danh mục đang rỗng");
      return;
    }
    for (int i = 0; i < categories.size(); i++) {
      System.out.printf("==== Danh mục thứ %d ==== \n", i);
      System.out.println(categories.get(i));
    }
  }

  public void updateCategory(Scanner sc) {
    int idCategory;
    while (true) {
      try {
        System.out.print("Nhập id danh mục muốn sửa (Nếu muốn hủy nhập '-1'): ");
        idCategory = Integer.parseInt(sc.nextLine());
        if (idCategory == -1) {
          return;
        }

        Optional<Category> categoryOptional = findById(idCategory);
        if (!categoryOptional.isPresent()) {
          System.err.printf("Danh mục với id %d không tồn tại\n", idCategory);
        } else {
          int luachon;
          while (true) {
            updateCategoryMenu();
            try {
              luachon = Integer.parseInt(sc.nextLine());
              switch (luachon) {

                case 1:
                  inputName(sc,categoryOptional.get());
                  break;
                case 2:

                  break;

                case 3:

                  return;

                default:
                  System.err.println("Lựa chọn không hợp lệ. Hãy nhập lại");

              }
            }catch (NumberFormatException ex) {
              System.err.println("Lựa chọn không hợp lệ. Hãy nhập lại");
            }

          }
        }
      } catch (NumberFormatException ex) {
        System.err.println("Id danh mục không hợp lệ. Hãy nhập lại.");
      }
    }
  }

  private void changeStatus(Scanner sc, Category category) {
    // TODO: Dừng lại từ bữa trước
    while(true) {
      System.out.println("==== CHỌN STATUS MUỐN THAY ĐỔI ====");
      System.out.println("1. Đang hoạt động");
      System.out.println("2. Ngừng hoạt động");
      System.out.print("Nhập lựa chọn: ");
    }
  }

  private void updateCategoryMenu() {
    System.out.println("==== UPDATE DANH MỤC ====");
    System.out.println("1. Thay đổi tên");
    System.out.println("2. Thay đổi trạng thái");
    System.out.println("3. Thoát");
    System.out.print("Nhập lựa chọn: ");
  }

  @Override
  public void save(Category entity) {
    categories.add(entity);
    writeToFile();
  }

  @Override
  public void delete(Integer id) {
    categories.remove(id);
    writeToFile();

  }

  @Override
  public Optional<Category> findById(Integer id) {
    Optional<Category> categoryOptional;
    try {
      Category category = categories.get(id);
      categoryOptional = Optional.of(category);
    } catch (IndexOutOfBoundsException ex) {
      categoryOptional = Optional.empty();
    }
    return categoryOptional;
  }

  @Override
  public Collection<Category> findAll() {
    return categories;
  }

}
