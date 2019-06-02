package top.whq6.project.handler;

import top.whq6.project.transition.ObjectTemplate;

public class ComplexTypeForJSONHandler implements ConvertTypeForJSONHandler {

  @Override
  public <T> String toJSONString(T obj, String fieldName,
      ObjectTemplate template) {
    return null;
  }

  @Override
  public <T> T toObjectField(T obj, String fieldName, Object value,
      ObjectTemplate template) {
    return null;
  }
}
