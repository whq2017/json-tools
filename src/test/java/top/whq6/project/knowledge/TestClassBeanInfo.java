package top.whq6.project.knowledge;

import org.junit.Test;
import top.whq6.project.bean.ClassBeanInfo;
import top.whq6.project.bean.Person;
import top.whq6.project.parser.DefaultClassParser;

public class TestClassBeanInfo {

  @Test
  public void testPerson() {
    DefaultClassParser parser = new DefaultClassParser();
    ClassBeanInfo info = parser.parser(Person.class);

    System.out.println(info);
  }

  @Test
  public void testClassInfo() {

  }
}
