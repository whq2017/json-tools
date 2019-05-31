package top.whq6.project.parser;

import com.google.common.collect.ImmutableMap;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import top.whq6.project.bean.ClassBeanInfo;
import top.whq6.project.bean.ClassFieldBean;
import top.whq6.project.cache.TemplateCache;
import top.whq6.project.transition.JSONStringTemplate;
import top.whq6.project.transition.ObjectTemplate;

@Slf4j
public class Object2StringParser implements ObjectParser {

  private static final DefaultClassParser CLASS_PARSER = new DefaultClassParser();

  private static final TemplateCache TEMPLATE_CACHE = new TemplateCache();

  private static final Object EMPTY_OBJECT = new Object();

  private final ThreadLocal<JSONStringTemplate> jsonLocal;

  private final ThreadLocal<ObjectTemplate> templateLocal;

  public Object2StringParser() {
    this.jsonLocal = new ThreadLocal<>();
    this.templateLocal = new ThreadLocal<>();
    jsonLocal.set(new JSONStringTemplate());
  }

  @Override
  public String toJSONString(Object obj) {
    parse(obj);
    String jsonString = fillTemplateByValue(obj, templateLocal.get());
    templateLocal.remove();
    jsonLocal.remove();
    return jsonString;
  }

  @Override
  public void parse(Object obj) {
    Class<?> cls = obj.getClass();

    try {
      ObjectTemplate template = parse(cls);
      templateLocal.set(template);
    } catch (CloneNotSupportedException e) {
      e.printStackTrace();
    }
  }

  private ObjectTemplate parse(Class<?> cls) throws CloneNotSupportedException {
    ClassBeanInfo beanInfo = CLASS_PARSER.parser(cls);

    if (log.isDebugEnabled()) {
      log.debug("Class name: {}", cls.getName());

      log.debug("Not Deserialize: {}", beanInfo.getNotDeserialize());
      log.debug("Not Serialized: {}", beanInfo.getNotSerialized());

      log.debug("Name to alias: {}", beanInfo.getName2Alias());
      log.debug("All Field Beans: {}", Arrays.asList(beanInfo.getAllFieldBeans()));
    }

    return parseClassBeanToTemplate(beanInfo, cls);
  }

  /**
   * fill template data and generate string.
   *
   * @param obj data object
   * @param objectTemplate objectTemplate
   * @return json string
   */
  private String fillTemplateByValue(Object obj, ObjectTemplate objectTemplate) {

    parseObjectToTemplate(obj, objectTemplate);

    return jsonLocal.get().getString();
  }

  private void parseObjectToTemplate(Object obj, ObjectTemplate objectTemplate) {
    try {
      dealBaseTypeValues(obj, objectTemplate);
      dealStaticTypeValues(obj, objectTemplate);
      dealComplexTypeValues(obj, objectTemplate);

      JSONStringTemplate jsonStringTemplate = jsonLocal.get();

      /////////////////////////////////////////////////////
      jsonStringTemplate.start();

      baseTypeToJSONString(jsonStringTemplate, objectTemplate);
      complexTypeToJSONString(jsonStringTemplate, objectTemplate);

      jsonStringTemplate.end();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void complexTypeToJSONString(
      JSONStringTemplate jsonStringTemplate, ObjectTemplate objectTemplate) {

    HashMap<String, ObjectTemplate> complexTypeValues = objectTemplate.getComplexTypeValues();

    if (complexTypeValues.size() == 0) {
      return;
    }

    complexTypeValues.forEach(
        (k, v) -> {
          baseTypeToJSONString(jsonStringTemplate, v);
          complexTypeToJSONString(jsonStringTemplate, v);
        });
  }


  private void baseTypeToJSONString(
      JSONStringTemplate jsonStringTemplate, ObjectTemplate objectTemplate) {
    // todo isNullToString not to deal
    baseTypeToJSONString(jsonStringTemplate, objectTemplate, false);
  }

  private void baseTypeToJSONString(
      JSONStringTemplate jsonStringTemplate,
      ObjectTemplate objectTemplate,
      boolean isNullToString) {
    HashMap<String, Object> baseTypeValues = objectTemplate.getBaseTypeValues();
    ImmutableMap<String, String> name2Alias = objectTemplate.getName2Alias();
    Set<String> notSerialized = objectTemplate.getNotSerialized();

    baseTypeValues.forEach(
        (k, v) -> {
          if (!notSerialized.contains(k)) {
            if (v == null) {
              if (isNullToString) {
                jsonStringTemplate.addKeyValue(name2Alias.get(k), "null");
              }

            } else {
              jsonStringTemplate.addKeyValue(name2Alias.get(k), v.toString());
            }
          }
        });

    if (log.isDebugEnabled()) {
      log.debug("Base type to json string: {}", jsonStringTemplate.getString());
    }
  }

  private void dealComplexTypeValues(Object obj, ObjectTemplate objectTemplate) throws Exception {

    HashMap<String, ObjectTemplate> complexTypeValues = objectTemplate.getComplexTypeValues();

    if (complexTypeValues.size() == 0) {
      return;
    }

    for (String name : complexTypeValues.keySet()) {
      Field declaredField = obj.getClass().getDeclaredField(name);
      Object fieldValue = declaredField.get(obj);
      if (log.isDebugEnabled()) {
        log.debug("field: [{}], value: [{}] complexTypeValues", name, fieldValue);
      }

      if (declaredField.getType().isArray()) {
        dealArrayTypeValues(fieldValue, declaredField, complexTypeValues.get(name));
      } else {
        dealBaseTypeValues(fieldValue, complexTypeValues.get(name));
        dealComplexTypeValues(fieldValue, complexTypeValues.get(name));
      }
    }
  }

  private void dealArrayTypeValues(
      Object fieldValue, Field declaredField, ObjectTemplate objectTemplate) {
    Class<?> cls = fieldValue.getClass();
    // todo 处理数组

  }

  private void dealBaseTypeValues(Object obj, ObjectTemplate objectTemplate)
      throws NoSuchMethodException {
    dealBaseFieldValue(obj, objectTemplate.getBaseTypeValues(), objectTemplate.getAllFieldCls());
  }

  private void dealStaticTypeValues(Object obj, ObjectTemplate objectTemplate)
      throws NoSuchMethodException {

    dealBaseFieldValue(
        obj, objectTemplate.getStaticModifierValues(), objectTemplate.getAllFieldCls());
  }

  private void dealBaseFieldValue(
      Object obj, HashMap<String, Object> map, ImmutableMap<String, Class<?>> allFieldCls)
      throws NoSuchMethodException {

    if (map.size() == 0) {
      return;
    }

    for (String key : map.keySet()) {
      Object value = parseBaseTypeValue(obj, key, allFieldCls.get(key));

      if (log.isDebugEnabled()) {
        log.debug("parseBaseTypeValue() return value: {}", value);
      }

      map.put(key, value);
    }
  }

  private ObjectTemplate parseClassBeanToTemplate(ClassBeanInfo beanInfo, Class<?> cls)
      throws CloneNotSupportedException {

    if (TEMPLATE_CACHE.exist(cls.getName())) {
      return (ObjectTemplate) TEMPLATE_CACHE.get(cls.getName()).clone();
    }

    HashMap<String, Object> baseTypeValues = new HashMap<>();
    HashMap<String, ObjectTemplate> complexTypeValues = new HashMap<>();
    HashMap<String, Object> staticTypeValues = new HashMap<>();
    HashMap<String, String> enumNames = new HashMap<>();
    ImmutableMap<String, String> name2Alias = beanInfo.getName2Alias();
    ImmutableMap<String, Class<?>> allFieldCls = beanInfo.getAllFieldCls();

    // all field
    ClassFieldBean[] fieldBeans = beanInfo.getAllFieldBeans();
    for (ClassFieldBean fieldBean : fieldBeans) {

      if (fieldBean.isStatic() && fieldBean.isComplexType()) {
        ObjectTemplate template = parseComplexType(fieldBean);

        staticTypeValues.put(fieldBean.getName(), EMPTY_OBJECT);
        complexTypeValues.put(fieldBean.getName(), template);

      } else if (fieldBean.isStatic()) {
        staticTypeValues.put(fieldBean.getName(), EMPTY_OBJECT);
      } else if (fieldBean.isComplexType()) {
        ObjectTemplate template = parseComplexType(fieldBean);

        complexTypeValues.put(fieldBean.getName(), template);
      } else if (fieldBean.isEnum()) {
        enumNames.put(fieldBean.getName(), "");
      } else {

        baseTypeValues.put(fieldBean.getName(), EMPTY_OBJECT);
      }
    }
    ObjectTemplate objectTemplate = new ObjectTemplate();
    objectTemplate.setClassName(cls.getName());
    objectTemplate.setBeanInfo(beanInfo);

    objectTemplate.setBaseTypeValues(baseTypeValues);
    objectTemplate.setComplexTypeValues(complexTypeValues);
    objectTemplate.setEnumNames(enumNames);
    objectTemplate.setStaticModifierValues(staticTypeValues);
    objectTemplate.setName2Alias(name2Alias);
    objectTemplate.setAllFieldCls(allFieldCls);
    objectTemplate.setNotSerialized(beanInfo.getNotSerialized());
    objectTemplate.setNotDeserialize(beanInfo.getNotDeserialize());

    TEMPLATE_CACHE.put(cls.getName(), (ObjectTemplate) objectTemplate.clone());

    if (log.isDebugEnabled()) {
      log.debug("Bean field count: {}", beanInfo.getFieldBeans().length);

      log.debug("Object template BaseTypeValues: {}", objectTemplate.getBaseTypeValues());
      log.debug("Object template ComplexTypeValues: {}", objectTemplate.getComplexTypeValues());

      log.debug(
          "Object template StaticModifierValues: {}", objectTemplate.getStaticModifierValues());
      log.debug("Object template EnumNames: {}", objectTemplate.getEnumNames());
    }

    return objectTemplate;
  }

  private Object parseBaseTypeValue(Object obj, String name, Class<?> fieldType)
      throws NoSuchMethodException {

    String methodName = fieldType == Boolean.class ? "is" : "get" + StringUtils.capitalize(name);

    Method method;
    try {
      method = obj.getClass().getMethod(methodName);
    } catch (NoSuchMethodException e) {
      methodName = "get" + StringUtils.capitalize(name);
      method = obj.getClass().getMethod(methodName);

      if (method == null) {
        log.error("Not found [{}]'s getter function [{}]", name, methodName);
        throw new NoSuchMethodException();
      }
    }

    if (log.isDebugEnabled()) {
      log.debug("method name: [{}]", methodName);
    }

    try {
      return method.invoke(obj);
    } catch (IllegalAccessException | InvocationTargetException ex) {
      log.error("Function [{}] not access.", methodName);
      ex.printStackTrace();
    }

    return null;
  }

  private ObjectTemplate parseComplexType(ClassFieldBean fieldBean)
      throws CloneNotSupportedException {

    return parse(fieldBean.getFieldType());
  }
}
