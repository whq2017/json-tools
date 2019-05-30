package top.whq6.project.knowledge;

import java.lang.reflect.Field;
import org.junit.Assert;
import org.junit.Test;
import top.whq6.project.bean.NumberEnum;

public class TestFieldType {

  @Test
  public void testIsBaseType() {
    int a = 1;
    Object obj = a;

    Class<?> cls = obj.getClass();

    Assert.assertEquals(cls.getName(), "java.lang.Integer");

  }

  @Test
  public void testArrayType() {
    String[] strArray = new String[1];
    String typeName = strArray.getClass().getTypeName();
    // System.out.println(typeName);

    Class<? extends String[]> cls = strArray.getClass();
    Assert.assertTrue(cls.isArray());
    Assert.assertEquals(cls.getName(), "[Ljava.lang.String;");
    Assert.assertEquals(cls.getTypeName(), "java.lang.String[]");

  }

  @Test
  public void testEnumType() {
    Class<? extends NumberEnum> cls = NumberEnum.ONE.getClass();

    Assert.assertEquals(cls.getSuperclass().getName(), "java.lang.Enum");
    // System.out.println(cls);
    // System.out.println(cls.getSuperclass());

    Field[] declaredFields = cls.getSuperclass().getDeclaredFields();
    for (Field field : declaredFields) {
      System.out.println(field.getName());
    }
  }

}
