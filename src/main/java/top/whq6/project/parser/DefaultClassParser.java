package top.whq6.project.parser;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import top.whq6.project.annotation.As;
import top.whq6.project.annotation.AsValueString;
import top.whq6.project.annotation.DateFormatter;
import top.whq6.project.annotation.Deserialize;
import top.whq6.project.annotation.Serialize;
import top.whq6.project.bean.ClassBeanInfo;
import top.whq6.project.bean.ClassFieldBean;
import top.whq6.project.bean.ClassInfo;
import top.whq6.project.cache.DefaultClassCache;


@Slf4j
public class DefaultClassParser implements ClassParser {

  private static final DefaultClassCache<ClassBeanInfo> CLASS_CACHE = new DefaultClassCache<>();

  private static final DefaultClassCache<ClassInfo> CLASS_INFO_CACHE = new DefaultClassCache<>();

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
    Set<String> notSerialized = new HashSet<>();
    Set<String> notDeserialize = new HashSet<>();

    HashMap<String, String> name2AliasTable =
        getName2AliasTable(allFieldBean, notSerialized, notDeserialize);
    HashMap<String, Class<?>> allFieldName2Cls = getAllFieldName2Cls(allFieldBean);
    HashMap<String, String> dateFormatters = getAllDateFormatter(allFieldBean);

    if (log.isDebugEnabled()) {
      log.debug("This class[{}]'s all field names: {}", cls.getName(), fieldBean);
      log.debug("This class[{}]'s and super's all field names: {}", cls.getName(),
          Lists.newArrayList(allFieldBean));
    }

    ClassBeanInfo beanInfo =
        ClassBeanInfo.builder()
            .classInfo(info)
            .allFieldBeans(allFieldBean)
            .fieldBeans(fieldBean.toArray(EMPTY_CLASS_FIELD_BEAN))
            .notSerialized(ImmutableSet.copyOf(notSerialized))
            .notDeserialize(ImmutableSet.copyOf(notDeserialize))
            .dateFormatters(ImmutableMap.copyOf(dateFormatters))
            .name2Alias(ImmutableMap.copyOf(name2AliasTable))
            .allFieldCls(ImmutableMap.copyOf(allFieldName2Cls))
            .build();

    CLASS_CACHE.put(cls.getName(), beanInfo);

    return beanInfo;
  }

  private HashMap<String, String> getAllDateFormatter(ClassFieldBean[] allFieldBean) {

    HashMap<String, String> map = new HashMap<>();
    for (ClassFieldBean fieldBean : allFieldBean) {
      if (fieldBean.isDate()) {
        map.put(fieldBean.getName(), fieldBean.getDateFormatter());
      }
    }
    return map;
  }

  private HashMap<String, Class<?>> getAllFieldName2Cls(ClassFieldBean[] allFieldBean) {
    if (allFieldBean == null || allFieldBean.length == 0) {
      return new HashMap<>(0);
    }

    HashMap<String, Class<?>> map = new HashMap<>(allFieldBean.length);
    for (ClassFieldBean fieldBean : allFieldBean) {
      map.put(fieldBean.getName(), fieldBean.getFieldType());
    }

    return map;
  }

  private HashMap<String, String> getName2AliasTable(
      ClassFieldBean[] allFieldBean, Set<String> notSerialized, Set<String> notDeserialize) {
    if (allFieldBean == null || allFieldBean.length == 0) {
      return new HashMap<>(0);
    }

    HashMap<String, String> map = new HashMap<>(allFieldBean.length);
    for (ClassFieldBean fieldBean : allFieldBean) {
      map.put(fieldBean.getName(), fieldBean.getAlias());
      if (!fieldBean.isSerialization()) {
        notSerialized.add(fieldBean.getName());
      }

      if (!fieldBean.isDeserialize()) {
        notDeserialize.add(fieldBean.getName());
      }
    }
    return map;
  }

  private ClassFieldBean[] getAllFieldInfo(ClassInfo info, List<ClassFieldBean> fieldBean) {

    List<ClassFieldBean> beanArrayList = new ArrayList<>(16);

    beanArrayList.addAll(fieldBean);

    ClassInfo superClass = info.getSuperBeans();

    // base type
    if (superClass == null) {
      return beanArrayList.toArray(EMPTY_CLASS_FIELD_BEAN);
    }

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
    String clsName = cls.getName();

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
              String formatterAnnotationValue = null;
              boolean isDate = type == Date.class;

              if (isDate) {
                formatterAnnotationValue = getDateFormatterAnnotationValue(field);
              }

              return ClassFieldBean.builder()
                  .name(name)
                  .clsName(clsName)
                  .alias(alias)
                  .isValueType(isValueType(cls))
                  .complexType(isComplexType(type))
                  .fieldType(type)
                  .toStringType(toStringType)
                  .modifiers(modifiers)
                  .serialization(serialization)
                  .deserialize(deserialization)
                  .isArray(type.isArray())
                  .isEnum(type.isEnum())
                  .isDate(isDate)
                  .dateFormatter(formatterAnnotationValue)
                  .isStatic(Modifier.isStatic(modifiers))
                  .build();
            })
        .collect(Collectors.toList());
  }

  private String getDateFormatterAnnotationValue(Field field) {
    DateFormatter annotation = field.getAnnotation(DateFormatter.class);
    if (annotation == null) {
      return "";
    }
    return annotation.value();
  }

  private ClassInfo parseClassInfo(Class<?> cls) {

    if (cls == Object.class) {
      return OBJECT_CLASS;
    }

    String name = cls.getName();

    boolean isValueType = isValueType(cls);

    if (log.isDebugEnabled()) {
      log.debug("Class type: {}", cls.getName());
      log.debug("isValueType(cls) return: {}", isValueType);
    }

    // int short double ....
    if (isValueType) {
      return ClassInfo.builder()
          .className(name)
          .clazz(cls)
          .isValueType(true)
          .build();
    }

    Class<?> superclass = cls.getSuperclass();
    Class<?>[] interfaces = cls.getInterfaces();

    ClassInfo superBeanInfo;
    if (superclass == Object.class) {
      superBeanInfo = OBJECT_CLASS;
    } else if (superclass == Enum.class) {
      superBeanInfo = ENUM_CLASS;
    } else {
      if (CLASS_INFO_CACHE.exist(superclass.getName())) {
        superBeanInfo = CLASS_INFO_CACHE.get(superclass.getName());
      } else {
        superBeanInfo = parseClassInfo(superclass);
        CLASS_INFO_CACHE.put(superclass.getName(), superBeanInfo);
      }
    }

    return ClassInfo.builder()
        .className(name)
        .clazz(cls)
        .interfaces(interfaces)
        .superClass(superclass)
        .isValueType(false)
        .superBeans(superBeanInfo)
        .build();
  }

  private boolean getSerializationAnnotationValue(Field field) {

    Serialize annotation = field.getAnnotation(Serialize.class);
    if (annotation == null) {
      return true;
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
    Deserialize annotation = field.getAnnotation(Deserialize.class);
    if (annotation == null) {
      return true;
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
        && type != Boolean.class
        && type != Object.class
        && !isValueType(type);
  }

  private boolean isValueType(Class<?> type) {
    return type == int.class
        || type == short.class
        || type == long.class
        || type == byte.class
        || type == float.class
        || type == double.class
        || type == char.class
        || type == boolean.class;
  }
}
