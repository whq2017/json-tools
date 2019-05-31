package top.whq6.project.bean;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import top.whq6.project.annotation.Serializable;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SimpleBean4 {

  private String name;

  @Serializable(false)
  private Double price;

}
