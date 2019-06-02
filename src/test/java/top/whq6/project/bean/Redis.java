package top.whq6.project.bean;

public class Redis {

  private Integer port;

  private Integer number;

  @Override
  public String toString() {
    return "Redis{" +
        "port=" + port +
        ", number=" + number +
        '}';
  }

  public Integer getPort() {
    return port;
  }

  public void setPort(Integer port) {
    this.port = port;
  }

  public Integer getNumber() {
    return number;
  }

  public void setNumber(Integer number) {
    this.number = number;
  }
}
