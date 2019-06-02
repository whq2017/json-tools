package top.whq6.project.bean;

import java.io.Serializable;

public class User implements Serializable {

  private static final Long serialVersionUID = -5690943740993091223L;
  private Long id;
  private String name;
  private String phone;
  private String locale;

  public Long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getLocale() {
    return locale;
  }

  public void setLocale(String locale) {
    this.locale = locale;
  }

  public static User random() {
    User u = new User();
    u.setId(System.currentTimeMillis());
    u.setName("" + System.currentTimeMillis());
    u.setPhone("137000" + System.currentTimeMillis() % 100000);
    u.setLocale("中国");
    return u;
  }
}