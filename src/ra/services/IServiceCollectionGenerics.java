package ra.services;

import java.util.Collection;
import java.util.Optional;

public interface IServiceCollectionGenerics<T,I> {
  void save(T entity);
  void delete(I id);

  Optional<T> findById(I id);

  Collection<T> findAll();

}
