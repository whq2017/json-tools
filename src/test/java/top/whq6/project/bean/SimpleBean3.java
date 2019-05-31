package top.whq6.project.bean;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import top.whq6.project.annotation.As;
import top.whq6.project.annotation.Serializable;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SimpleBean3 {

  @As("nickname")
  private String name;

  @Serializable(false)
  private Integer age;

}
