package top.whq6.project.parser;

import java.util.Date;
import org.junit.Assert;
import org.junit.Test;
import top.whq6.project.bean.BooleanTypeBean;
import top.whq6.project.bean.Configuration;
import top.whq6.project.bean.DateTypeBean;
import top.whq6.project.bean.DateTypeBean2;
import top.whq6.project.bean.SimpleBean;
import top.whq6.project.bean.SimpleBean2;
import top.whq6.project.bean.SimpleBeanValueType;

public class TestDefaultObjectParser {

  @Test
  public void testCreateObject() {
    DefaultObjectParser defaultObjectParser = new DefaultObjectParser(new Configuration());

    Assert.assertNotNull(defaultObjectParser);
  }

  @Test
  public void testParseRefObject() {
    SimpleBean bean = new SimpleBean("123", 2);
    DefaultObjectParser defaultObjectParser = new DefaultObjectParser(new Configuration());
    defaultObjectParser.toJSONString(bean);
  }

  @Test
  public void testParseValueObject() {
    SimpleBeanValueType bean = new SimpleBeanValueType("123", 2, new Byte("1"));
    DefaultObjectParser defaultObjectParser = new DefaultObjectParser(new Configuration());
    defaultObjectParser.toJSONString(bean);
  }

  @Test
  public void testSimpleBean2Object() {
    SimpleBean2 bean = new SimpleBean2("123", 4);
    DefaultObjectParser defaultObjectParser = new DefaultObjectParser(new Configuration());
    defaultObjectParser.toJSONString(bean);
  }

  @Test
  public void testBooleanType() {

    BooleanTypeBean booleanTypeBean = new BooleanTypeBean(true, false);
    DefaultObjectParser defaultObjectParser = new DefaultObjectParser(new Configuration());
    defaultObjectParser.toJSONString(booleanTypeBean);
  }

  @Test
  public void testDateType() {

    DateTypeBean booleanTypeBean = new DateTypeBean(new Date(System.currentTimeMillis()));
    DefaultObjectParser defaultObjectParser = new DefaultObjectParser(new Configuration());
    defaultObjectParser.toJSONString(booleanTypeBean);
  }

  @Test
  public void testDateTypeAnnotation() {

    DateTypeBean2 booleanTypeBean =
        new DateTypeBean2(new Date(System.currentTimeMillis()),
            new Date(System.currentTimeMillis()));
    DefaultObjectParser defaultObjectParser = new DefaultObjectParser(new Configuration());
    defaultObjectParser.toJSONString(booleanTypeBean);
  }
}
