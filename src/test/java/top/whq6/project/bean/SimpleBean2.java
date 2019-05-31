package top.whq6.project.bean;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import top.whq6.project.annotation.As;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SimpleBean2 {

  @As("nickname")
  private String name;

  @As("Age")
  private Integer age;

}
