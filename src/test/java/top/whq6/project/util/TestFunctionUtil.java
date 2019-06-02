package top.whq6.project.util;

import org.junit.Assert;
import org.junit.Test;

public class TestFunctionUtil {

  @Test
  public void testToGetMethodName() {
    String name = FunctionUtil.toGetMethodName.apply("name", false);
    Assert.assertEquals(name, "getName");

    String boy = FunctionUtil.toGetMethodName.apply("boy", true);
    Assert.assertEquals(boy, "isBoy");
  }

  @Test
  public void testToSetMethodName() {
    String name = FunctionUtil.toSetMethodName.apply("name");
    Assert.assertEquals(name, "setName");
  }

  @Test
  public void testTKeyValueString() {
    String name = FunctionUtil.toKeyValueString.apply("name", "XiMing");
    Assert.assertEquals(name, "\"name\":\"XiMing\"");
  }


}
