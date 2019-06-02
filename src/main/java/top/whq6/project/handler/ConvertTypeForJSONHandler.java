package top.whq6.project.handler;

import top.whq6.project.transition.ObjectTemplate;

public interface ConvertTypeForJSONHandler {

  <T> String toJSONString(T obj, String fieldName, ObjectTemplate template) throws Exception;

  <T> T toObjectField(T obj, String fieldName, Object value, ObjectTemplate template)
      throws Exception;
}
