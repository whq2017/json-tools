package top.whq6.project.parser;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import top.whq6.project.annotation.As;
import top.whq6.project.annotation.AsValueString;
import top.whq6.project.annotation.NotDeserialize;
import top.whq6.project.annotation.NotSerializable;
import top.whq6.project.bean.ClassBeanInfo;
import top.whq6.project.bean.ClassFieldBean;
import top.whq6.project.bean.ClassInfo;
import top.whq6.project.cache.DefaultClassCache;

public class DefaultClassParser implements ClassParser {

  private static final DefaultClassCache CLASS_CACHE = new DefaultClassCache();

  private static final ClassInfo OBJECT_CLASS =
      ClassInfo.builder().clazz(Object.class).className(Object.class.getName()).build();
  private static final ClassInfo ENUM_CLASS =
      ClassInfo.builder().clazz(Enum.class).className(Enum.class.getName()).build();

  private static final ClassFieldBean[] EMPTY_CLASS_FIELD_BEAN = new ClassFieldBean[0];

  @Override
  public ClassBeanInfo parser(Class<?> cls) {

    if (CLASS_CACHE.exist(cls.getName())) {
      return CLASS_CACHE.get(cls.getName());
    }

    ClassInfo info = parseClassInfo(cls);
    List<ClassFieldBean> fieldBean = parseFieldInfo(cls);
    ClassFieldBean[] allFieldBean = getAllFieldInfo(info, fieldBean);

    ClassBeanInfo beanInfo =
        ClassBeanInfo.builder()
            .classInfo(info)
            .fieldBeans(fieldBean.toArray(EMPTY_CLASS_FIELD_BEAN))
            .allFieldBeans(allFieldBean)
            .build();

    CLASS_CACHE.put(cls.getName(), beanInfo);

    return beanInfo;
  }

  private ClassFieldBean[] getAllFieldInfo(ClassInfo info, List<ClassFieldBean> fieldBean) {

    List<ClassFieldBean> beanArrayList = new ArrayList<>(16);

    beanArrayList.addAll(fieldBean);

    ClassInfo superClass = info.getSuperBeans();
    while (superClass != OBJECT_CLASS && superClass != ENUM_CLASS) {
      // parser superClass
      List<ClassFieldBean> classFieldBeans = parseFieldInfo(superClass.getClazz());
      beanArrayList.addAll(classFieldBeans);

      superClass = superClass.getSuperBeans();
    }

    return beanArrayList.toArray(EMPTY_CLASS_FIELD_BEAN);
  }

  private List<ClassFieldBean> parseFieldInfo(Class<?> cls) {

    Field[] declaredFields = cls.getDeclaredFields();

    if (declaredFields.length == 0) {
      return new ArrayList<>();
    }

    return Arrays.stream(declaredFields)
        .map(
            field -> {
              int modifiers = field.getModifiers();
              String name = field.getName();
              String alias = getAliasAnnotationValue(field, name);
              Class<?> type = field.getType();
              boolean toStringType = getToStringTypeAnnotationValue(field);
              boolean serialization = getSerializationAnnotationValue(field);
              boolean deserialization = getDeserializationAnnotationValue(field);

              return ClassFieldBean.builder()
                  .name(name)
                  .alias(alias)
                  .complexType(isComplexType(type))
                  .fieldType(type)
                  .toStringType(toStringType)
                  .modifiers(modifiers)
                  .serialization(serialization)
                  .deserialize(deserialization)
                  .isArray(type.isArray())
                  .isEnum(type.isEnum())
                  .build();
            })
        .collect(Collectors.toList());
  }

  private ClassInfo parseClassInfo(Class<?> cls) {

    String name = cls.getName();
    Class<?> superclass = cls.getSuperclass();
    Class<?>[] interfaces = cls.getInterfaces();

    ClassInfo superBeanInfo;
    if (superclass == Object.class) {
      superBeanInfo = OBJECT_CLASS;
    } else if (superclass == Enum.class) {
      superBeanInfo = ENUM_CLASS;
    } else {
      superBeanInfo = parseClassInfo(superclass);
    }

    return ClassInfo.builder()
        .className(name)
        .clazz(cls)
        .interfaces(interfaces)
        .superClass(superclass)
        .superBeans(superBeanInfo)
        .build();
  }

  private boolean getSerializationAnnotationValue(Field field) {

    NotSerializable annotation = field.getAnnotation(NotSerializable.class);
    if (annotation == null) {
      return false;
    }

    return annotation.value();
  }

  private boolean getToStringTypeAnnotationValue(Field field) {

    AsValueString asValueString = field.getAnnotation(AsValueString.class);

    if (asValueString == null) {
      return false;
    }

    return asValueString.value();
  }

  private String getAliasAnnotationValue(Field field, String name) {

    As as = field.getAnnotation(As.class);
    if (as == null) {
      return name;
    }

    String val = as.value().trim();
    if ("".equals(val)) {
      return name;
    }

    return val;
  }

  private boolean getDeserializationAnnotationValue(Field field) {
    NotDeserialize annotation = field.getAnnotation(NotDeserialize.class);
    if (annotation == null) {
      return false;
    }

    return annotation.value();
  }

  private boolean isComplexType(Class<?> type) {
    return type != Integer.class
        && type != Short.class
        && type != Long.class
        && type != Byte.class
        && type != Float.class
        && type != Double.class
        && type != Character.class
        && type != String.class
        && type != Object.class;
  }
}