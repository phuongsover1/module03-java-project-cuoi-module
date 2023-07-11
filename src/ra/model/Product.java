package ra.model;

import java.io.Serializable;

public class Product implements Serializable {
  // TODO: Giờ giả định cho id ban đầu lúc mới load class là = 0, nhưng sao khi đọc file vào thì curentId chưa chắc đã bằng 0,
  // TODO: Phải tạo thêm một khối static
  private static int currentId = 0;
  private int id;
  private String name;
  private Category category;
  private int stock;
  private boolean status = true;
}
