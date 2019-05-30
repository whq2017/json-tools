package top.whq6.project.cache;

import com.sun.istack.internal.NotNull;
import java.util.concurrent.ConcurrentHashMap;
import top.whq6.project.bean.ClassBeanInfo;


public class DefaultClassCache implements ClassCache<ClassBeanInfo> {

  private static final int DEFAULT_SIZE = 16;

  private static final ConcurrentHashMap<String, ClassBeanInfo>
      CLASS_BEAN_INFO_CONCURRENT_HASH_MAP = new ConcurrentHashMap<>(DEFAULT_SIZE);

  @Override
  public ClassBeanInfo get(String key) {

    return CLASS_BEAN_INFO_CONCURRENT_HASH_MAP.get(key);
  }

  @Override
  public void put(@NotNull String key, @NotNull ClassBeanInfo val) {

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

    ClassBeanInfo info = CLASS_BEAN_INFO_CONCURRENT_HASH_MAP.remove(key);

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
