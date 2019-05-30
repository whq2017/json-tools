package top.whq6.project.bean;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;


@Getter
@Builder
@ToString
public class ClassInfo {

  /**
   * The class full-qualified name.
   */
  private String className;

  private Class<?> clazz;

  private Class<?> superClass;

  private Class<?>[] interfaces;

  /**
   * Super class bean.
   */
  private ClassInfo superBeans;

}
