package top.whq6.project.cache;

import com.sun.istack.internal.NotNull;
import java.util.concurrent.ConcurrentHashMap;


public abstract class AbstractCache<T> implements ClassCache<T> {

  private static final int DEFAULT_SIZE = 16;

  private final ConcurrentHashMap<String, T>
      CLASS_BEAN_INFO_CONCURRENT_HASH_MAP = new ConcurrentHashMap<>(DEFAULT_SIZE);

  @Override
  public T get(String key) {

    return CLASS_BEAN_INFO_CONCURRENT_HASH_MAP.get(key);
  }

  @Override
  public void put(@NotNull String key, @NotNull T val) {

    if (key == null || val == null) {
      return;
    }

    CLASS_BEAN_INFO_CONCURRENT_HASH_MAP.put(key, val);
  }

  @Override
  public boolean remove(@NotNull String key) {

    if (key == null || "".equals(key)) {
      return false;
    }

    T info = CLASS_BEAN_INFO_CONCURRENT_HASH_MAP.remove(key);

    return info == null;
  }

  @Override
  public void clear() {
    CLASS_BEAN_INFO_CONCURRENT_HASH_MAP.clear();
  }

  @Override
  public boolean exist(String key) {

    if (key == null || "".equals(key)) {
      return false;
    }

    return CLASS_BEAN_INFO_CONCURRENT_HASH_MAP.containsKey(key);
  }
}
