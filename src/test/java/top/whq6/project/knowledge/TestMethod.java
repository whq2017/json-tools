package top.whq6.project.knowledge;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.junit.Test;
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

}
