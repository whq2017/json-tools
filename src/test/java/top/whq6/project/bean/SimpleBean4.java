package top.whq6.project.bean;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import top.whq6.project.annotation.Serialize;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SimpleBean4 {

  private String name;

  @Serialize(false)
  private Double price;

}
