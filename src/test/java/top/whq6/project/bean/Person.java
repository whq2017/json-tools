package top.whq6.project.bean;

public class Person {

  private static final String DATA_ONE = "one";
  private static final String DATA_FIVE = "five";

  private String dataTwo;
  private String dataSix;

  private volatile String dataThree;
  private volatile String dataFour;

  private transient String val;
}
