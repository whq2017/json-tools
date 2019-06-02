package top.whq6.project.bean;

import java.util.EnumMap;
import lombok.Getter;
import lombok.Setter;
import top.whq6.project.handler.ArrayTypeForJSONHandler;
import top.whq6.project.handler.BaseTypeForJSONHandler;
import top.whq6.project.handler.BooleanTypeForJSONHandler;
import top.whq6.project.handler.ComplexTypeForJSONHandler;
import top.whq6.project.handler.ConvertTypeForJSONHandler;
import top.whq6.project.handler.DateTypeForJSONHandler;
import top.whq6.project.handler.EnumNameForJSONHandler;

@Getter
public class Configuration {

  @Setter
  private boolean asValueString;

  @Setter
  private String dateFormatter;

  @Setter
  private boolean deserialize;

  @Setter
  private boolean serialize;

  @Setter
  private boolean serializeNull;

  private EnumMap<TypeHandlerEnum, ConvertTypeForJSONHandler> convertTypeHandlers;

  public Configuration() {
    asValueString = false;
    deserialize = true;
    serialize = true;
    dateFormatter = "yyyy-MM-dd HH:mm:ss";
    serializeNull = false;
    convertTypeHandlers = new EnumMap<>(TypeHandlerEnum.class);

    registerConvertTypeHandler();
  }

  private void registerConvertTypeHandler() {
    registerConvertTypeHandler(TypeHandlerEnum.BASE, new BaseTypeForJSONHandler());
    registerConvertTypeHandler(TypeHandlerEnum.BOOLEAN, new BooleanTypeForJSONHandler());
    registerConvertTypeHandler(TypeHandlerEnum.DATE, new DateTypeForJSONHandler());
    registerConvertTypeHandler(TypeHandlerEnum.COMPLEX, new ComplexTypeForJSONHandler());
    registerConvertTypeHandler(TypeHandlerEnum.ENUM, new EnumNameForJSONHandler());
    // registerConvertTypeHandler(TypeHandlerEnum.ENUM, new EnumOrdinalForJSONHandler());
    registerConvertTypeHandler(TypeHandlerEnum.ARRAY, new ArrayTypeForJSONHandler());
  }

  public void registerConvertTypeHandler(TypeHandlerEnum name, ConvertTypeForJSONHandler handler) {

    if (name == null || handler == null) {
      return;
    }

    this.convertTypeHandlers.put(name, handler);
  }

  public ConvertTypeForJSONHandler getHandlerByName(TypeHandlerEnum name) {
    return this.convertTypeHandlers.get(name);
  }
}
