package top.whq6.project.bean;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
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
   * All fields. Super class field and this class field.
   */
  private ClassFieldBean[] allFieldBeans;

  /**
   * All fields Class.
   */
  private ImmutableMap<String, Class<?>> allFieldCls;

  private ImmutableMap<String, String> name2Alias;

  private ImmutableMap<String, String> dateFormatters;

  private ImmutableSet<String> notSerialized;

  private ImmutableSet<String> notDeserialize;
}

