package top.whq6.project.bean;

public class BooleanTypeBean {

  private boolean value1;

  private Boolean value2;

  public BooleanTypeBean(boolean value1, Boolean value2) {
    this.value1 = value1;
    this.value2 = value2;
  }

  public boolean isValue1() {
    return value1;
  }

  public void setValue1(boolean value1) {
    this.value1 = value1;
  }


  public Boolean getValue2() {
    return value2;
  }

  public void setValue2(Boolean value2) {
    this.value2 = value2;
  }
}
