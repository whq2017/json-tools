package top.whq6.project.bean;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class ClassFieldBean {

  /**
   * The field name
   */
  private String name;

  /**
   * The field type.
   *
   * Recursive serialization if the field type is complex type. And set this field #{@link
   * ClassFieldBean#complexType} to true.
   */
  private Class<?> fieldType;


  /**
   * The value is true if the field type is complex type.
   */
  private boolean complexType;

  /**
   * JSON string key. default #{@link ClassFieldBean#name}.
   *
   * The value is set by #{@link top.whq6.project.annotation.As}.
   */
  private String alias;

  /**
   * Indicates whether the field to serialize. default false.
   *
   * The value is set by #{@link top.whq6.project.annotation.NotSerializable}.
   */
  private boolean serialization;

  /**
   * Indicates whether deserialize to field. default false.
   *
   * The value is set by #{@link top.whq6.project.annotation.NotDeserialize}.
   */
  private boolean deserialize;


  /**
   * Set value to String type. default false.
   *
   * The value is set by #{@link top.whq6.project.annotation.AsValueString}.
   */
  private boolean toStringType;

  /**
   * The field modifiers
   *
   * Using #{@link java.lang.reflect.Modifier} static function to determine
   */
  private int modifiers;

  /**
   * Determines if this {@code Class} object represents an array class
   */
  private boolean isArray;

  private boolean isEnum;
}
