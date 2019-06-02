package top.whq6.project.knowledge;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.junit.Assert;
import org.junit.Test;
import top.whq6.project.bean.BooleanTypeBean;
import top.whq6.project.bean.SimpleBean5;

public class TestMethod {

  @Test
  public void testSuperMethodByReflect()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

    SimpleBean5 bean = new SimpleBean5("li", 22.1);
    bean.setName("apple");
    bean.setPrice(12.0);

    Method getPrice = SimpleBean5.class.getSuperclass().getDeclaredMethod("getPrice");
    Object invoke = getPrice.invoke(bean);
    System.out.println(invoke);
  }

  @Test
  public void testMethod()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

  }

  @Test
  public void testType()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

    String name1 = int.class.getName();
    String name2 = Integer.class.getName();
    String simpleName = Integer.class.getSimpleName();
    System.out.println(name1);
    System.out.println(name2);
    System.out.println(simpleName);
  }

  @Test
  public void testBoolean()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

    BooleanTypeBean booleanTypeBean = new BooleanTypeBean(true, false);

    Method getValue2 = booleanTypeBean.getClass().getDeclaredMethod("getValue2");
    Assert.assertNotNull(getValue2);

  }
}
