package top.whq6.project.parser;

import org.junit.Test;
import top.whq6.project.bean.SimpleBean;
import top.whq6.project.bean.SimpleBean2;
import top.whq6.project.bean.SimpleBean3;
import top.whq6.project.bean.SimpleBean5;

public class TestObject2JSON {

  @Test
  public void testSimpleBean() {

    SimpleBean bean = new SimpleBean("name", 12);

    Object2StringParser parser = new Object2StringParser();
    String jsonString = parser.toJSONString(bean);
    System.out.println(jsonString);
  }

  @Test
  public void testSimpleBean2() {

    SimpleBean2 bean = new SimpleBean2("lq", 23);

    Object2StringParser parser = new Object2StringParser();
    String jsonString = parser.toJSONString(bean);
    System.out.println(jsonString);
  }

  @Test
  public void testSimpleBean3() {

    SimpleBean3 bean = new SimpleBean3("lq", 23);

    Object2StringParser parser = new Object2StringParser();
    String jsonString = parser.toJSONString(bean);
    System.out.println(jsonString);
  }

  @Test
  public void testSimpleBean4() {

    SimpleBean5 bean = new SimpleBean5(null, 23.0);
    bean.setName("apple");
    bean.setPrice(12.0);

    Object2StringParser parser = new Object2StringParser();
    String jsonString = parser.toJSONString(bean);
    System.out.println(jsonString);
  }


}
