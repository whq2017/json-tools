package top.whq6.project.knowledge;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import org.junit.Assert;
import org.junit.Test;
import top.whq6.project.bean.Person;
import top.whq6.project.bean.Student;

public class TestObjectField {

  @Test
  public void testObjectField() {
    Field[] declaredFields = Object.class.getDeclaredFields();
    Assert.assertEquals(declaredFields.length, 0);
  }

  @Test
  public void testPersonField() {
    Field[] declaredFields = Person.class.getDeclaredFields();
    Assert.assertEquals(declaredFields.length, 3);
  }

  @Test
  public void testPersonFieldNotStatic() {
    Field[] declaredFields = Person.class.getDeclaredFields();
    for (Field declaredField : declaredFields) {
      int modifiers = declaredField.getModifiers();
      String name = declaredField.getName();
      System.out.println(name + ": " + modifiers);
    }
  }

  @Test
  public void testPersonFieldNotStatic2() throws NoSuchFieldException {

    // static
    Field data_one = Person.class.getDeclaredField("DATA_ONE");
    Assert.assertTrue(Modifier.isStatic(data_one.getModifiers()));

    // volatile
    Field dataThrees = Person.class.getDeclaredField("dataThree");
    Assert.assertTrue(Modifier.isVolatile(dataThrees.getModifiers()));

    // transient
    Field val = Person.class.getDeclaredField("val");
    Assert.assertTrue(Modifier.isTransient(val.getModifiers()));

    // private
    Field dataTwo = Person.class.getDeclaredField("dataTwo");
    Assert.assertTrue(Modifier.isPrivate(dataTwo.getModifiers()));
  }

  @Test
  public void testStudentField() {
    // 声明的所有属性
    Field[] declaredFields = Student.class.getDeclaredFields();
    Assert.assertEquals(declaredFields.length, 3);

    // 只能拿到public修饰符修饰的属性
    Field[] fields = Student.class.getFields();
    Assert.assertEquals(fields.length, 1);

    Assert.assertEquals(fields[0].getName(), "teacher");
  }
}
