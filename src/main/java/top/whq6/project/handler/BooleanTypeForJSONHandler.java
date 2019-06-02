package top.whq6.project.handler;

import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import top.whq6.project.util.FunctionUtil;

/**
 * @see BaseTypeForJSONHandler
 */
@Slf4j
public class BooleanTypeForJSONHandler extends BaseTypeForJSONHandler {

  @Override
  protected <T> Method getMethod(T obj, String fieldName) throws NoSuchMethodException {

    Method method = null;
    try {
      // fieldName value is start. such as: field isBoy
      if (fieldName.startsWith("is")) {
        return obj.getClass().getDeclaredMethod(fieldName);
      }

      method = obj.getClass()
          .getDeclaredMethod(FunctionUtil.toGetMethodName.apply(fieldName, true));
      return method;
    } catch (NoSuchMethodException e) {
      try {
        method = obj.getClass()
            .getDeclaredMethod(FunctionUtil.toGetMethodName.apply(fieldName, false));
      } catch (NoSuchMethodException ex) {
        log.error("Field [{}] getter method is not found", fieldName);
        throw ex;
      }

    }

    if (log.isDebugEnabled()) {
      log.debug("Field [{}] getter method: [{}]", fieldName, method.getName());
    }

    return method;
  }
}
