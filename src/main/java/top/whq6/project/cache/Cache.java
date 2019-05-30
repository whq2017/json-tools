package top.whq6.project.cache;

public interface Cache<T> {

  /**
   * Get value by key
   *
   * @param key key
   * @return object, if key not exist return null.
   */
  T get(String key);

  /**
   * Put key and value to container.
   *
   * @param key key
   * @param val value
   */
  void put(String key, T val);

  /**
   * Remove key and value by key.
   *
   * @param key key.
   * @return exist and remove successfully, return true; or not return false.
   */
  boolean remove(String key);

  /**
   * clear container.
   */
  void clear();


  /**
   * @param key key.
   * @return if exist, return true; or false.
   */
  boolean exist(String key);
}
