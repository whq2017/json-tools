package top.whq6.project.handler;

import com.google.common.collect.ImmutableMap;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import top.whq6.project.bean.Configuration;
import top.whq6.project.transition.ObjectTemplate;
import top.whq6.project.util.FunctionUtil;

@Slf4j
public class BaseTypeForJSONHandler implements ConvertTypeForJSONHandler {

  @Override
  public <T> String toJSONString(T obj, String fieldName, ObjectTemplate template)
      throws Exception {

    Object returnValue;
    Method method = null;
    try {
      method = getMethod(obj, fieldName);

      returnValue = method.invoke(obj);

      if (log.isDebugEnabled()) {
        log.debug(
            "Object [{}] field [{}] return value [{}] (class: [{}])",
            obj,
            fieldName,
            returnValue,
            returnValue.getClass());
      }

    } catch (NoSuchMethodException e) {
      log.error("Not found [{}] field's getter method: {}", fieldName, e.getMessage());
      throw e;
    } catch (IllegalAccessException | InvocationTargetException e) {
      log.error(
          "Don't access the method [{}], or invoke target exception: {}",
          method.getName(),
          e.getMessage());
      throw e;
    }

    return returnValue2JSON(fieldName, template, returnValue);
  }

  protected String returnValue2JSON(String fieldName, ObjectTemplate template, Object returnValue) {
    ImmutableMap<String, String> name2Alias = template.getName2Alias();
    Set<String> notSerialized = template.getNotSerialized();
    Configuration configuration = template.getConfiguration();

    if (notSerialized.contains(fieldName)) {
      return null;
    }

    if (returnValue == null) {
      if (configuration.isSerializeNull()) {
        returnValue = "";
      } else {
        return null;
      }
    }

    return FunctionUtil.toKeyValueString.apply(name2Alias.get(fieldName), returnValue);
  }

  protected <T> Method getMethod(T obj, String fieldName) throws NoSuchMethodException {
    return obj.getClass().getDeclaredMethod(FunctionUtil.toGetMethodName.apply(fieldName, false));
  }

  @Override
  public <T> T toObjectField(T obj, String fieldName, Object value, ObjectTemplate template)
      throws Exception {

    ImmutableMap<String, Class<?>> allFieldCls = template.getAllFieldCls();
    Class<?> fieldTypeClass = allFieldCls.get(fieldName);

    final String setMethodName = FunctionUtil.toSetMethodName.apply(fieldName);
    try {
      Method method = obj.getClass().getDeclaredMethod(setMethodName, fieldTypeClass);
      method.invoke(obj, castValue(value, fieldTypeClass));
    } catch (NoSuchMethodException e) {
      log.error("The field [{}] has not setter method: [{}]", fieldName, setMethodName);
      throw e;
    }

    return obj;
  }

  private Object castValue(Object value, Class<?> fieldTypeClass) {

    switch (fieldTypeClass.getSimpleName()) {
      case "Integer":
      case "int":
        value = Integer.valueOf(value.toString());
        break;
      case "Short":
      case "short":
        value = Short.valueOf(value.toString());
        break;
      case "Long":
      case "long":
        value = Long.valueOf(value.toString());
        break;

      case "Byte":
      case "byte":
        value = Byte.valueOf(value.toString());
        break;
      case "Float":
      case "float":
        value = Float.valueOf(value.toString());
        break;
      case "Double":
      case "double":
        value = Double.valueOf(value.toString());
        break;
      case "Character":
      case "char":
        value = value.toString().charAt(0);
        break;
      case "Boolean":
      case "boolean":
        value = Boolean.valueOf(value.toString());
        break;
      default:
        value = String.valueOf(value);
    }

    return value;
  }
}
