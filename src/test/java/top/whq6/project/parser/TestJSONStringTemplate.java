package top.whq6.project.parser;

import org.junit.Test;
import top.whq6.project.transition.JSONStringTemplate;

public class TestJSONStringTemplate {


  @Test
  public void testAddQuote() {
    JSONStringTemplate template = new JSONStringTemplate();
    template.addQuote("key");
    template.addQuote("val");
    System.out.println(template.getString());
  }

  @Test
  public void testAddKeyValue() {
    JSONStringTemplate template = new JSONStringTemplate();
    template.addKeyValue("key", "value");
    System.out.println(template.getString());
  }

  @Test
  public void test() {
    JSONStringTemplate template = new JSONStringTemplate();
    template.start();
    template.addKeyValue("key", "value");
    template.end();
    System.out.println(template.getString());
  }
}
