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
  public static ArrayList<Category> getCategories (){
          return categories;
  }

  static {
    Collection<Category> categoryCollectionFromFile = null;
    categoryCollectionFromFile = IOCategory.readFromFile(Utility.ACCOUNT_FILE);
    if (categoryCollectionFromFile == null) {
      categoryCollectionFromFile = new ArrayList<>();
    }
    categories = (ArrayList<Category>) categoryCollectionFromFile;
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
    while(true) {
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
    }catch (IndexOutOfBoundsException ex) {
      categoryOptional = Optional.empty();
    }
    return categoryOptional;
  }

  @Override
  public Collection<Category> findAll() {
    return categories;
  }

}
