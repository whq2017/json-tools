package top.whq6.project.parser;

import top.whq6.project.bean.ClassBeanInfo;

public interface ClassParser {

  ClassBeanInfo parser(Class<?> cls);
}
