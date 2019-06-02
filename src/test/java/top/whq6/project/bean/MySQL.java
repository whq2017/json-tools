package top.whq6.project.bean;

import top.whq6.project.annotation.As;
import top.whq6.project.annotation.Serialize;

public class MySQL {

  private String driver;

  @As("URL")
  private String url;

  private String password;

  @Serialize
  private String username;

  @Override
  public String toString() {
    return "MySQL{" +
        "driver='" + driver + '\'' +
        ", url='" + url + '\'' +
        ", password='" + password + '\'' +
        ", username='" + username + '\'' +
        '}';
  }

  public String getDriver() {
    return driver;
  }

  public void setDriver(String driver) {
    this.driver = driver;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }
}
