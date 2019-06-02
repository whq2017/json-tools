package top.whq6.project.util;

import static top.whq6.project.util.JSONSymbol.COLON;
import static top.whq6.project.util.JSONSymbol.QUOTE;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.apache.commons.lang3.StringUtils;

public class FunctionUtil {

  private static final BiFunction<String, String, String> toMethodNameByPrefix =
      (name, prefix) -> prefix + StringUtils.capitalize(name);

  private static final Function<Object, String> caseValue =
      (v) -> v.getClass() != String.class ? v.toString() : QUOTE + v + QUOTE;

  public static final BiFunction<String, Boolean, String> toGetMethodName =
      (fieldName, isBoolean) ->
          isBoolean
              ? toMethodNameByPrefix.apply(fieldName, "is")
              : toMethodNameByPrefix.apply(fieldName, "get");

  public static final Function<String, String> toSetMethodName =
      (fieldName) -> toMethodNameByPrefix.apply(fieldName, "set");

  public static final BiFunction<String, Object, String> toKeyValueString =
      (k, v) -> QUOTE + k + QUOTE + COLON + caseValue.apply(v);
}
