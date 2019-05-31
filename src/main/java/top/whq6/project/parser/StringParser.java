package top.whq6.project.parser;

public interface StringParser extends Parser {

  <T> T toObject(String jsonString, Class<T> cls);
}
