package top.whq6.project.parser;

import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import top.whq6.project.bean.ClassBeanInfo;
import top.whq6.project.bean.ClassFieldBean;
import top.whq6.project.bean.Configuration;
import top.whq6.project.cache.TemplateCache;
import top.whq6.project.transition.ObjectTemplate;

@Slf4j
public class TemplateParser extends AbstractTemplateParser {

  private static final DefaultClassParser CLASS_PARSER = new DefaultClassParser();

  private static final TemplateCache TEMPLATE_CACHE = new TemplateCache();

  private final ThreadLocal<ObjectTemplate> templateLocal;


  public TemplateParser() {
    this(null);
  }

  public TemplateParser(Configuration configuration) {
    super(configuration);
    this.templateLocal = new ThreadLocal<>();
  }

  @Override
  public ObjectTemplate getTemplate() {
    return templateLocal.get();
  }

  @Override
  public boolean parseClassAndCreateTemplate(Object obj) {
    if (obj == null) {
      return true;
    }

    if (templateLocal.get() != null) {
      templateLocal.remove();
    }

    try {
      Class<?> cls = obj.getClass();
      ObjectTemplate template = parse(cls);
      templateLocal.set(template);
    } catch (CloneNotSupportedException e) {
      log.error("Clone not supported: {}", e.getMessage());
    }
    return false;
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

  private ObjectTemplate parseClassBeanToTemplate(ClassBeanInfo beanInfo, Class<?> cls)
      throws CloneNotSupportedException {

    if (TEMPLATE_CACHE.exist(cls.getName())) {
      return (ObjectTemplate) TEMPLATE_CACHE.get(cls.getName()).clone();
    }

    ObjectTemplate objectTemplate = createTemplate(beanInfo, cls);

    // cache
    TEMPLATE_CACHE.put(cls.getName(), (ObjectTemplate) objectTemplate.clone());

    if (log.isDebugEnabled()) {
      log.debug("Bean field count: {}", beanInfo.getFieldBeans().length);

      log.debug("Object template BaseTypeValues: {}", objectTemplate.getBaseTypeValues());
      log.debug("Object template ValueypeValues: {}", objectTemplate.getValueTypeNames());
      log.debug("Object template ComplexTypeValues: {}", objectTemplate.getComplexTypeValues());

      log.debug(
          "Object template StaticModifierValues: {}", objectTemplate.getStaticModifierValues());
      log.debug("Object template EnumNames: {}", objectTemplate.getEnumNames());
    }

    return objectTemplate;
  }

  private ObjectTemplate createTemplate(ClassBeanInfo beanInfo, Class<?> cls)
      throws CloneNotSupportedException {

    ObjectTemplate objectTemplate =
        new ObjectTemplate(cls.getName(), beanInfo, this.getConfiguration());

    // fill container
    classifyFieldType(objectTemplate);

    return objectTemplate;
  }

  private void classifyFieldType(ObjectTemplate template) throws CloneNotSupportedException {

    // all field
    ClassFieldBean[] fieldBeans = template.getBeanInfo().getAllFieldBeans();

    for (ClassFieldBean fieldBean : fieldBeans) {

      if (log.isDebugEnabled()) {
        log.debug("Field bean name: [{}]", fieldBean.getName());
        log.debug("Field bean info: [{}]", fieldBean);
      }

      if (fieldBean.isValueType()) {
        // int double long
        template.getValueTypeNames().add(fieldBean.getName());
      } else if (fieldBean.isDate()) {
        template.getDateNames().add(fieldBean.getName());
      } else if (fieldBean.isStatic() && fieldBean.isComplexType()) {
        ObjectTemplate temp = parseComplexType(fieldBean);

        template.getStaticModifierValues().add(fieldBean.getName());
        template.getComplexTypeValues().put(fieldBean.getName(), temp);

      } else if (fieldBean.isStatic()) {
        template.getStaticModifierValues().add(fieldBean.getName());
      } else if (fieldBean.isComplexType()) {
        ObjectTemplate temp = parseComplexType(fieldBean);

        template.getComplexTypeValues().put(fieldBean.getName(), temp);
      } else if (fieldBean.isEnum()) {
        template.getEnumNames().put(fieldBean.getName(), "");
      } else {
        // Integer Short Long
        template.getBaseTypeValues().add(fieldBean.getName());
      }
    }
  }

  private ObjectTemplate parseComplexType(ClassFieldBean fieldBean)
      throws CloneNotSupportedException {

    return parse(fieldBean.getFieldType());
  }
}
