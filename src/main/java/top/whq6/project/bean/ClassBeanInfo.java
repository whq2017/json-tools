package top.whq6.project.bean;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class ClassBeanInfo {

  private ClassInfo classInfo;

  /**
   * The class fields.
   */
  private ClassFieldBean[] fieldBeans;

  /**
   * All fields.
   */
  private ClassFieldBean[] allFieldBeans;
}

