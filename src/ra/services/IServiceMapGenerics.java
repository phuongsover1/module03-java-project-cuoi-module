package ra.services;


import java.util.Map;
import java.util.Optional;

public interface IServiceMapGenerics<T, I> {
  void save(T entity);
  void delete(I id);
  Optional<T> findById(I id);

  Map<I,T> findAll();
}

