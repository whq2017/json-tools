package top.whq6.project.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Student extends Person {

  private String clazz;

  public String teacher;

  String grade;

  NumberEnum numberEnum;

}
