package top.whq6.project.handler;

import com.google.common.collect.ImmutableMap;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import top.whq6.project.bean.Configuration;
import top.whq6.project.transition.ObjectTemplate;
import top.whq6.project.util.FunctionUtil;

@Slf4j
public class DateTypeForJSONHandler extends BaseTypeForJSONHandler {

  private static final ZoneId zoneId = ZoneId.systemDefault();

  @Override
  public <T> String toJSONString(T obj, String fieldName, ObjectTemplate template)
      throws Exception {

    Configuration configuration = template.getConfiguration();
    String dateFormatter = configuration.getDateFormatter();

    ImmutableMap<String, String> allDateFormatters = template.getAllDateFormatters();

    String df = allDateFormatters.get(fieldName);

    if (!"".equals(df.trim())) {
      dateFormatter = df;
    }

    String dateString = null;
    String methodName = FunctionUtil.toGetMethodName.apply(fieldName, false);
    try {
      Method method = obj.getClass().getDeclaredMethod(methodName);
      Object returnValue = method.invoke(obj);
      dateString = formatterDate((Date) returnValue, dateFormatter);

    } catch (NoSuchMethodException e) {
      log.error("Not found [{}] field's method [{}]: {}", fieldName, methodName, e.getCause());
      throw e;
    } catch (IllegalAccessException | InvocationTargetException e) {
      log.error(
          "Don't access the method [{}], or invoke target exception: {}", methodName, e.getCause());
      e.printStackTrace();
    }

    return returnValue2JSON(fieldName, template, dateString);
  }

  @Override
  public <T> T toObjectField(T obj, String fieldName, Object value, ObjectTemplate template)
      throws Exception {

    ImmutableMap<String, Class<?>> allFieldCls = template.getAllFieldCls();
    String dateFormatter = template.getConfiguration().getDateFormatter();

    String methodName = FunctionUtil.toSetMethodName.apply(fieldName);

    try {
      Method method = obj.getClass().getDeclaredMethod(methodName, allFieldCls.get(fieldName));
      method.invoke(obj, formatterDate(value.toString(), dateFormatter));
    } catch (NoSuchMethodException e) {
      log.error("Not found field [{}] setter method [{}]: {}", fieldName, methodName, e.getCause());
      throw e;
    } catch (IllegalAccessException | InvocationTargetException e) {
      log.error("Don't access setter method [{}] or invoke failed.", methodName, e.getCause());
      throw e;
    }

    return obj;
  }

  private String formatterDate(Date date, String dateFormatter) {
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateFormatter);
    Instant instant = date.toInstant();
    LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();

    return localDateTime.format(dateTimeFormatter);
  }

  private Date formatterDate(String dateString, String dateFormatter) {

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateFormatter);

    LocalDateTime localDateTime = LocalDateTime.parse(dateString, dateTimeFormatter);

    ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);

    return Date.from(zonedDateTime.toInstant());
  }
}
