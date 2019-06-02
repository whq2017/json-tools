package top.whq6.project.bean;

public class Mongo {

  private Integer port;
  private String host;
  private String _test;


  public String get_test() {
    return _test;
  }

  public void set_test(String _test) {
    this._test = _test;
  }

  @Override
  public String toString() {
    return "Mongo{" +
        "port=" + port +
        ", host='" + host + '\'' +
        '}';
  }

  public Integer getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }
}
