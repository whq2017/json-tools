package top.whq6.project.transition;

public class JSONStringTemplate {

  private static final String LEFT_CURLY_BRACKET = "{";
  private static final String RIGHT_CURLY_BRACKET = "}";

  private static final String LEFT_SQUARE_BRACKET = "[";
  private static final String RIGHT_SQUARE_BRACKET = "]";

  private static final String COLON = ":";
  private static final String SEMI_COLON = ";";
  private static final String COMMA = ",";

  private static final String QUOTE = "\"";

  private StringBuffer buffer;

  public JSONStringTemplate() {
    this.buffer = new StringBuffer();
  }

  public StringBuffer start() {
    return this.buffer.append(LEFT_CURLY_BRACKET);
  }

  public String getString() {
    return this.buffer.toString();
  }

  public StringBuffer end() {

    if (',' == this.buffer.charAt(this.buffer.length() - 1)) {
      this.buffer.deleteCharAt(this.buffer.length() - 1);
    }

    return this.buffer.append(RIGHT_CURLY_BRACKET);
  }

  public StringBuffer addArray(String key, Object[] array) {
    addQuote(key)
        .append(COLON)
        .append(LEFT_SQUARE_BRACKET);

    // todo 处理数组数据

    return this.buffer.append(RIGHT_SQUARE_BRACKET).append(COMMA);
  }

  public StringBuffer addObject(String key, Object obj) {
    this.buffer.append(addQuote(key)).append(COLON).append(obj);

    return this.buffer;
  }

  public StringBuffer addObject(Object obj) {

    this.buffer.append(LEFT_CURLY_BRACKET);

    return this.buffer.append(RIGHT_CURLY_BRACKET);
  }


  public StringBuffer addKeyValue(String key, String value) {
    // "\"key\":\"value\","
    addQuote(key).append(COLON);
    return addQuote(value).append(COMMA);
  }

  public StringBuffer addQuote(String val) {
    return this.buffer.append(QUOTE).append(val).append(QUOTE);
  }
}
