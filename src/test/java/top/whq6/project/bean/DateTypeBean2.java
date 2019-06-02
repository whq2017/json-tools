package top.whq6.project.bean;

import java.util.Date;
import top.whq6.project.annotation.DateFormatter;

public class DateTypeBean2 {

  private Date date;

  @DateFormatter("yyyy-MM-dd")
  private Date date2;

  public DateTypeBean2(Date date, Date date2) {
    this.date = date;
    this.date2 = date2;
  }

  public Date getDate2() {
    return date2;
  }

  public void setDate2(Date date2) {
    this.date2 = date2;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }
}
